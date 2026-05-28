package com.utcn.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.UUID;

@SpringBootApplication
@EnableJpaRepositories
@EnableAsync
public class DemoApplication {

	// Cerinta de specificatie: id aleator tiparit o singura data la pornire; nu trebuie sters.
	public static final String internalExecutionId = UUID.randomUUID().toString();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("internalExecutionId = " + internalExecutionId);
	}

}
