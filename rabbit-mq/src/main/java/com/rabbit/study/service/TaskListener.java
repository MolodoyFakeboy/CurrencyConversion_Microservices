package com.rabbit.study.service;

import com.rabbit.study.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TaskListener {

    protected static final String TASK_QUEUE = "task.queue";
    protected static final String TASK_EXCHANGE = "task.exchange";

    @Value("${server.port}")
    public Integer servicePort;

    @SneakyThrows
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = TASK_QUEUE),
                    exchange = @Exchange(value = TASK_EXCHANGE)
            )
    )
    public void handleTask(Task task) {
        Thread.sleep(15000);

        log.info(String.format(
                "Service \"%s\" start process task \"%s\", from service \"%s\" ",
                servicePort,
                task.getId(),
                task.getFrom())
        );
    }
}
