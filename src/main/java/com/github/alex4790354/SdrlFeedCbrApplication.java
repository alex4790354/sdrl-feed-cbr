package com.github.alex4790354;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableRetry
@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class SdrlFeedCbrApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdrlFeedCbrApplication.class, args);
	}
}
