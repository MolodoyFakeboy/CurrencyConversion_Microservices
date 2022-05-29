package com.rabbit.study.service;

import com.rabbit.study.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.rabbit.study.service.TaskInitializer.SERVICE_ID;

@Component
@Slf4j
@RequiredArgsConstructor
public class TaskListener {

    protected static final String TASK_QUEUE = "task.queue";
    protected static final String TASK_EXCHANGE = "task.exchange";

    @SneakyThrows
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = TASK_QUEUE),
                    exchange = @Exchange(value = TASK_EXCHANGE)
            )
    )
    public void handleTask(Task task) {
        Thread.sleep(5000);
        log.info(String.format(
                "Service \"%s\" start process task \"%s\", from service \"%s\" ",
                SERVICE_ID,
                task.getId(),
                task.getFrom())
        );
    }
}
