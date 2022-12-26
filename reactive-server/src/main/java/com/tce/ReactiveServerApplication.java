package com.tce;

import io.micrometer.observation.ObservationTextPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReactiveServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveServerApplication.class, args);
	}

	@Bean
	ObservationTextPublisher otp() {
		return new ObservationTextPublisher();
	}

}
