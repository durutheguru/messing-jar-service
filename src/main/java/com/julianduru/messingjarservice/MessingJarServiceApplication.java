package com.julianduru.messingjarservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class MessingJarServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessingJarServiceApplication.class, args);
	}

}
