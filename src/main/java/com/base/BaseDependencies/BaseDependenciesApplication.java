package com.base.BaseDependencies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
@EnableAspectJAutoProxy
public class BaseDependenciesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseDependenciesApplication.class, args);
		log.info("Apllication Started Successfully");
	}

}
 