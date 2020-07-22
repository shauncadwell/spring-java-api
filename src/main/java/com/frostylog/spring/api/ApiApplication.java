package com.frostylog.spring.api;

import com.frostylog.spring.lib.models.Person;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		Person person = new Person();
		SpringApplication.run(ApiApplication.class, args);
	}

}
