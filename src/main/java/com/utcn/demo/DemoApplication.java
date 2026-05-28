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
@EnableAsync // permite execuția pe thread separat a metodelor @Async (notificările de ban)
public class DemoApplication {

	// Identificator unic al rulării aplicației. Cerință de specificație:
	// trebuie să existe în program ca string aleator, să fie tipărit o singură dată la pornire,
	// și să nu fie șters. `public static final` => nu poate fi reasignat.
	public static final String internalExecutionId = UUID.randomUUID().toString();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("internalExecutionId = " + internalExecutionId);
	}

}
