package com.pension.management.processpensionmicroservice.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.pension.management.processpensionmicroservice.exception.InvalidTokenException;
import com.pension.management.processpensionmicroservice.feign.AuthorizationClient;
import com.pension.management.processpensionmicroservice.model.PensionDetails;
import com.pension.management.processpensionmicroservice.model.PensionerDetails;
import com.pension.management.processpensionmicroservice.model.PensionerInput;
import com.pension.management.processpensionmicroservice.service.ProcessPensionServiceImpl;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ProcessPensionController {
	
	private static Logger log = LoggerFactory.getLogger(ProcessPensionController.class);
	
	@Autowired
	ProcessPensionServiceImpl processPensionService;
	
	private PensionerDetails pensionerDetail;
			
	@Autowired
	AuthorizationClient authorizationClient;

	@PostMapping("/ProcessPension")
	public ResponseEntity<PensionDetails> getPensionDetails(
			@RequestHeader(name = "Authorization", required = true) String requestTokenHeader,
			@RequestBody @Valid PensionerInput pensionerInput) throws NotFoundException {
		log.info("EXECUTION START ->  getPensionDetails()");
		if (!authorizationClient.authorizeTheRequest(requestTokenHeader)) {
			throw new InvalidTokenException("ACCESS RESTRICTED");
		}
		
		log.info("EXECUTION END -> getPensionDetails()");
		return new ResponseEntity<>(processPensionService.getPensionDetails(requestTokenHeader,pensionerInput), HttpStatus.OK);
	}
}
