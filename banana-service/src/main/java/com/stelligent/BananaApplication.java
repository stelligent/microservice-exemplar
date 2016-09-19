package com.stelligent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class BananaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BananaApplication.class, args);
	}
}
