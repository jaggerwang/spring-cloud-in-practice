package net.jaggerwang.scip.gateway.adapter.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * @author Jagger Wang
 */
@SpringBootApplication(scanBasePackages = "net.jaggerwang.scip.gateway")
@EnableDiscoveryClient
@EnableReactiveFeignClients(basePackages = "net.jaggerwang.scip.gateway.usecase.port.service")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
