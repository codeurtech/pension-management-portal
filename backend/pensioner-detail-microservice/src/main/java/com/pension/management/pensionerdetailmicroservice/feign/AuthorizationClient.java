package com.pension.management.pensionerdetailmicroservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("authorization-service")
public interface AuthorizationClient {

	@GetMapping("/authorize")
	public boolean authorizeTheRequest(
			@RequestHeader(name = "Authorization", required = true) String requestTokenHeader);

}