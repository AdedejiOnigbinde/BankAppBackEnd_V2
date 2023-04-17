package com.base.BaseDependencies;

import org.jboss.jandex.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BaseDependenciesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseDependenciesApplication.class, args);
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.info("Apllication Started Successfully");
	}

}
 