package com.rabbit.study.service;

import com.rabbit.study.config.RedisLock;
import com.rabbit.study.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskInitializer {

    private final RedisLock redisLock;
    private final TaskSender taskSender;

    @Value("${server.port}")
    public Integer servicePort;

    private static final long ONE_MIN_IN_MILLISECOND = 1_000L * 60;
    private static final String GENERATE_TASK = "generate:tasks";

    @Scheduled(cron = "*/15 * * * * *")
    public void generateTask() {
        if (redisLock.acquireLock(ONE_MIN_IN_MILLISECOND, GENERATE_TASK)) {

            log.info(Strings.repeat("-",100));
            log.info(String.format("Service \"%s\" start generate task", servicePort));

            for (int i = 0; i < 5; i++) {
                taskSender.sendTask(Task.builder()
                        .id(generateRandomID())
                        .from(servicePort)
                        .number(i)
                        .build());
            }

            log.info(String.format("Service \"%s\" stop generate task", servicePort));
            log.info(Strings.repeat("-",100));

            redisLock.releaseLock(GENERATE_TASK);
        } else {
            log.info("Blocked");
        }
    }

    private static String generateRandomID() {
        return UUID.randomUUID().toString().substring(0, 4);
    }


}
