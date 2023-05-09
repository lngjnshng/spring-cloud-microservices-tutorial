package org.rubisemi.micro.order;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(
		title = "Order Service API",
		version = "1.0.0",
		description = "Order Service Documentation API V1.0.0"
	), servers = {
		@Server(url="/api/order", description = "Microservice Order API URL"),
		@Server(url = "/", description = "Monolithic Order API URL")
	}
)
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
