package com.pension.management.pensionerdetailmicroservice.controller.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import com.pension.management.pensionerdetailmicroservice.exception.NotFoundException;
import com.pension.management.pensionerdetailmicroservice.feign.AuthorizationClient;
import com.pension.management.pensionerdetailmicroservice.model.BankDetails;
import com.pension.management.pensionerdetailmicroservice.model.PensionerDetails;
import com.pension.management.pensionerdetailmicroservice.service.PensionerDetailsServiceImpl;
import com.pension.management.pensionerdetailmicroservice.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@WebMvcTest
@Slf4j
class PensionerDetailsControllerTest {
	
	private static Logger log = LoggerFactory.getLogger(PensionerDetailsControllerTest.class);
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PensionerDetailsServiceImpl pensionerDetailService;
	
	@MockBean
	private AuthorizationClient authorizationClient;
	
	@BeforeEach
	void setup() throws ParseException {
		
		// Mock Authorization Microservice Response
		when(authorizationClient.authorizeTheRequest(ArgumentMatchers.anyString())).thenReturn(true);
	}


	@Test
	@DisplayName("Test Aadhaar Card Number present in CSV")
	void testFetchPensionerDetailsIfAadhaarCardPresent() throws Exception {

		log.info("TEST EXECUTION START - testFetchPensionerDetailsIfAadhaarCardPresent()");
		final String aadhaarCardNumber = "848766419593";
		PensionerDetails pensionerDetail = new PensionerDetails("Caleb", DateUtil.parseDate("21-04-2012"), "K94ZHJ8ZXC",
				36004, 5919, "self", new BankDetails("New Edge Bank System", 97266291436L , "public"));
		when(pensionerDetailService.getPensionerDetailByAadhaarNumber(aadhaarCardNumber)).thenReturn(pensionerDetail);
		mockMvc.perform(
				get("/pensionerDetailByAadhaar/{aadhaarNumber}", aadhaarCardNumber).characterEncoding("utf-8")
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")		
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", Matchers.equalTo("Caleb")))
				.andExpect(jsonPath("$.pan", Matchers.equalTo("K94ZHJ8ZXC")))
				.andExpect(jsonPath("$.dateOfBirth", Matchers.equalTo("2012-04-21")))
				.andExpect(jsonPath("$.bank.accountNumber", Matchers.equalTo(97266291436L)));
		log.info("TEST EXECUTION END - testFetchPensionerDetailsIfAadhaarCardPresent()");
	}

	@Test
	@DisplayName("Test for Aadhaar Card Number not present in CSV")
	void testFetchPensionerDetailsIfAadhaarCardNotPresent() throws Exception {
		log.info("TEST EXECUTION START - testFetchPensionerDetailsIfAadhaarCardNotPresent()");
		final String aadhaarNumber = "999999999999";

		when(pensionerDetailService.getPensionerDetailByAadhaarNumber(ArgumentMatchers.any()))
				.thenThrow(new NotFoundException("AADHAAR CARD NUMBER NOT FOUND"));

		mockMvc.perform(
				get("/pensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.characterEncoding("utf-8")
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")	
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo("AADHAAR CARD NUMBER NOT FOUND")));
		log.info("TEST EXECUTION END - testFetchPensionerDetailsIfAadhaarCardNotPresent()");
	}

}
