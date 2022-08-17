package com.pension.management.pensionerdetailmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@EnableFeignClients(basePackages="com.pension.management.pensionerdetailmicroservice.feign")
public class PensionerDetailMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PensionerDetailMicroserviceApplication.class, args);
	}

}
