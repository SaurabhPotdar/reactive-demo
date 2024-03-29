package com.tce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class ReactiveClientApplication {

	public static void main(String[] args) {
		Hooks.enableAutomaticContextPropagation();
//		Hooks.enableContextLossTracking();
		SpringApplication.run(ReactiveClientApplication.class, args);
	}

}
