package com.effigo.learningportalv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaAuditing
@EnableJpaRepositories
@EnableTransactionManagement
//@EntityScan(basePackages = { "com.effigo.learningportalv2.entity" })
@SpringBootApplication
public class LearningPortalV2Application {

	public static void main(String[] args) {
		SpringApplication.run(LearningPortalV2Application.class, args);
	}

}
