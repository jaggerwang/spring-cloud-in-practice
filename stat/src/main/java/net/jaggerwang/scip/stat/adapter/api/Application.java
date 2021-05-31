package net.jaggerwang.scip.stat.adapter.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "net.jaggerwang.scip.stat")
@EntityScan("net.jaggerwang.scip.stat.adapter.dao.jpa.entity")
@EnableJpaRepositories("net.jaggerwang.scip.stat.adapter.dao.jpa")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
