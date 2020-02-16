package net.jaggerwang.scip.gateway.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "net.jaggerwang.scip.gateway")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
