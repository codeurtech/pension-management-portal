package com.pension.management.processpensionmicroservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.pension.management.processpensionmicroservice.model.PensionerDetails;

@FeignClient("pensioner-detail-service")
public interface PensionerDetailsClient {
	@GetMapping("/pensionerDetailByAadhaar/{aadhaarNumber}")
	public PensionerDetails getPensionerDetailByAadhaar(
			@RequestHeader(name = "Authorization", required = true) String requestTokenHeader,
			@PathVariable String aadhaarNumber);
}
