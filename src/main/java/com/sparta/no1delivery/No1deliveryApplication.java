package com.sparta.no1delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class No1deliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(No1deliveryApplication.class, args);
	}

}
