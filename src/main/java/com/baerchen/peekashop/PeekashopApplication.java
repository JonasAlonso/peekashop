package com.baerchen.peekashop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.baerchen.peekashop.order.control")
public class PeekashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeekashopApplication.class, args);
		log.info("Im here");
	}

}
