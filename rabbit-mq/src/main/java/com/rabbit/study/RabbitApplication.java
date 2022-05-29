package com.rabbit.study;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRabbit
@EnableScheduling
@SpringBootApplication
public class RabbitApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitApplication.class, args);
	}
}
