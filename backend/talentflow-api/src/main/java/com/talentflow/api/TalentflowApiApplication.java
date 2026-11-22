package com.talentflow.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TalentflowApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalentflowApiApplication.class, args);
	}

}
