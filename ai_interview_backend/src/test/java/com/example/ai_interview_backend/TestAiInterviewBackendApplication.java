package com.example.ai_interview_backend;

import org.springframework.boot.SpringApplication;

public class TestAiInterviewBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(AiInterviewBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
