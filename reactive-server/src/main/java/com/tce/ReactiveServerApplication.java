package com.tce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class ReactiveServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveServerApplication.class, args);
		Hooks.enableAutomaticContextPropagation();
	}

}
