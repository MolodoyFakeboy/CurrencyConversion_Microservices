package com.education.microservices.broker.api.service;

import com.education.microservices.broker.api.dto.ShareDto;
import com.education.microservices.broker.api.mapper.ShareMapper;
import com.education.microservices.broker.api.repository.ShareRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RabbitMQService {

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    @Qualifier("rabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${share.queue}")
    private String queryName;

    @SneakyThrows
    public List<ShareDto> sendNeedShare() {
        var shares = shareRepository.findAll()
                .stream()
                .map(shareMapper::toDto)
                .collect(Collectors.toList());
        log.info("Send info about shares");

        String answer = (String) rabbitTemplate.convertSendAndReceive(queryName, shares);

        return objectMapper.readValue(answer, new TypeReference<>() {});
    }

}
