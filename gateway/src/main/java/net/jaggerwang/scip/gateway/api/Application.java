package net.jaggerwang.scip.gateway.api;

import graphql.kickstart.tools.boot.GraphQLJavaToolsAutoConfiguration;
import graphql.kickstart.spring.webflux.boot.GraphQLSpringWebfluxAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "net.jaggerwang.scip.gateway",
		exclude = {GraphQLJavaToolsAutoConfiguration.class, GraphQLSpringWebfluxAutoConfiguration.class})
@EnableDiscoveryClient
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
