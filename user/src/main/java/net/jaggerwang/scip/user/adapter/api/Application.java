package net.jaggerwang.scip.user.adapter.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Jagger Wang
 */
@SpringBootApplication(scanBasePackages = "net.jaggerwang.scip.user")
@EntityScan("net.jaggerwang.scip.user.adapter.dao.jpa.entity")
@EnableJpaRepositories("net.jaggerwang.scip.user.adapter.dao.jpa")
@EnableDiscoveryClient
@EnableFeignClients
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
