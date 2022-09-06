package com.ciandt.summit.bootcamp2022;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SummitBootcampApplication {

	public static void main(String[] args) {
		SpringApplication.run(SummitBootcampApplication.class, args);
	}

}
