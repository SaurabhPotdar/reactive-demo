package com.tce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveClientApplication.class, args);
	}

//	@Bean
//	ObservationTextPublisher otp() {
//		return new ObservationTextPublisher();
//	}

}
