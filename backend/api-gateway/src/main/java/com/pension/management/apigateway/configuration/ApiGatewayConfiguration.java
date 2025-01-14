package com.pension.management.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
	
	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/get")
						.uri("http://httpbin.org:80"))
				.route(p -> p.path("/pensionerDetailByAadhaar/**")
						.uri("lb://pensioner-detail-service"))
				.route(p -> p.path("/authorize/**")
						.uri("lb://authorization-service"))
				.route(p -> p
						.path("/post")
						.uri("http://httpbin.org:80"))
				.route(p -> p.path("/ProcessPension/**")
						.uri("lb://process-pension-service"))
				.route(p -> p.path("/authenticate/**")
						.uri("lb://authorization-service"))
				.build();
	}

}
