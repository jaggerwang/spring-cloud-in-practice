package net.jaggerwang.scip.auth.adapter.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Jagger Wang
 */
@SpringBootApplication(scanBasePackages = "net.jaggerwang.scip.auth")
@EntityScan("net.jaggerwang.scip.auth.adapter.dao.jpa.entity")
@EnableJpaRepositories("net.jaggerwang.scip.auth.adapter.dao.jpa")
@EnableDiscoveryClient
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
