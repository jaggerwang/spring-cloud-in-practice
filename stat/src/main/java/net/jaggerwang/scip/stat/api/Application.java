package net.jaggerwang.scip.stat.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "net.jaggerwang.scip.stat")
@EntityScan("net.jaggerwang.scip.stat.adapter.repository.jpa.entity")
@EnableJpaRepositories("net.jaggerwang.scip.stat.adapter.repository.jpa")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
