package com.frostylog.spring.api;

import com.frostylog.spring.lib.models.Hair;
import com.frostylog.spring.lib.models.Person;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		Person person = new Person();
		person.setAddress("Address");
		Hair hair = new Hair("brown", "short");
		person.setHair(hair);
		System.out.println(person);
		SpringApplication.run(ApiApplication.class, args);
	}

}
