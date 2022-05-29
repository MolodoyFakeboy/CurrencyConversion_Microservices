package com.rabbit.study.service;

import com.rabbit.study.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.rabbit.study.service.TaskListener.TASK_EXCHANGE;



@Slf4j
@Component
@RequiredArgsConstructor
public class TaskSender {

    private final RabbitMessagingTemplate rabbitMessagingTemplate;

    public void sendTask(Task task) {
        rabbitMessagingTemplate.convertAndSend(TASK_EXCHANGE, null, task);
    }
}
