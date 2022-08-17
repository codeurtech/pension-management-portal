package com.pension.management.pensionerdetailmicroservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.pension.management.pensionerdetailmicroservice.exception.InvalidTokenException;
import com.pension.management.pensionerdetailmicroservice.feign.AuthorizationClient;
import com.pension.management.pensionerdetailmicroservice.model.PensionerDetails;
import com.pension.management.pensionerdetailmicroservice.service.PensionerDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
//@CrossOrigin("*")
public class PensionDetailController {

	private Logger log=LoggerFactory.getLogger(PensionDetailController.class);
	
	@Autowired
	private AuthorizationClient authorizationClient;

	@Autowired
	private PensionerDetailsServiceImpl pensionerDetailService;

	
	@GetMapping("/pensionerDetailByAadhaar/{aadhaarNumber}")
	public PensionerDetails getPensionerDetailByAadhaar(
			@RequestHeader(name = "Authorization", required = true) 
			String requestTokenHeader,
			@PathVariable String aadhaarNumber) {
		if (!authorizationClient.authorizeTheRequest(requestTokenHeader)) {
			throw new InvalidTokenException("ACCESS RESTRICTED");
		}
		log.info("EXECUTION START -> getPensionerDetailByAadhaar()");
		return pensionerDetailService.getPensionerDetailByAadhaarNumber(aadhaarNumber);
	}


}
