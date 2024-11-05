package com.itwillbs.bookjuk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//스케줄링 활성화
@EnableScheduling
@SpringBootApplication
public class BookjukProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookjukProjectApplication.class, args);
	}

}
