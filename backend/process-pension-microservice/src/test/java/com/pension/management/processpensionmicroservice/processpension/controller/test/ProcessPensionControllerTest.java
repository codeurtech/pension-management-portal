package com.pension.management.processpensionmicroservice.processpension.controller.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.util.Collections;

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
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pension.management.processpensionmicroservice.controller.ProcessPensionController;
import com.pension.management.processpensionmicroservice.exception.ExceptionInfo;
import com.pension.management.processpensionmicroservice.exception.NotFoundException;
import com.pension.management.processpensionmicroservice.feign.AuthorizationClient;
import com.pension.management.processpensionmicroservice.feign.PensionerDetailsClient;
import com.pension.management.processpensionmicroservice.model.PensionDetails;
import com.pension.management.processpensionmicroservice.model.PensionerInput;
import com.pension.management.processpensionmicroservice.model.util.DateUtil;
import com.pension.management.processpensionmicroservice.service.ProcessPensionServiceImpl;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;
import lombok.extern.slf4j.Slf4j;

@WebMvcTest(ProcessPensionController.class)
@Slf4j
class ProcessPensionControllerTest {
	
	private static Logger log = LoggerFactory.getLogger(ProcessPensionControllerTest.class);
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthorizationClient authorizationClient;

	@MockBean
	private PensionerDetailsClient pensionerDetailsClient;

	@MockBean
	private ProcessPensionServiceImpl processPensionService;

	@Autowired
	private ObjectMapper objectMapper;
	
	private PensionerInput validPensionerInput;
	
	private PensionerInput invalidPensionerInput;
	
	private PensionDetails pensionDetail;

	@BeforeEach
	void setup() throws ParseException {

		//Valid Input
		validPensionerInput = new PensionerInput("Himanshu", DateUtil.parseDate("11-06-1996"), "ALPCM2286P",
				"525263634189", "self");

		//Invalid Input
		invalidPensionerInput = new PensionerInput("Himanshu", DateUtil.parseDate("11-06-1996"), "ALPCM2286P", "",
				"self");

		//Correct Pensioner Details
		pensionDetail = new PensionDetails("Himanshu", DateUtil.parseDate("11-06-1996"), "ALPCM2286P", "family", 50000,
				550);

		// Mock Authorization Microservice Response
		when(authorizationClient.authorizeTheRequest(ArgumentMatchers.anyString())).thenReturn(true);

	}

	@Test
	@DisplayName("Test for retrieving PensionDetailsv with Valid Input via /ProcessPension")
	void testGetPensionDetailsWithValidInput() throws Exception {
		log.info("TEST EXECUTION START - testGetPensionDetailsWithValidInput()");
		// Mock ProcessPensionSerive response
		when(processPensionService.getPensionDetails(ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(pensionDetail);

		// Test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.pensionAmount", Matchers.equalTo(50000.0)));
		log.info("TEST EXECUTION END - testGetPensionDetailsWithValidInput()");
	}

	@Test
	@DisplayName("Test for retrievng Pension Details with Invalid Token via /ProcessPension")
	void testGetPensionDetailsWithInvalidToken() throws Exception {
		log.info("TEST EXECUTION START - testGetPensionDetailsWithInvalidToken()");
		
		// Mock Authorization Microservice Response for Invalid Token
		when(authorizationClient.authorizeTheRequest(ArgumentMatchers.anyString())).thenReturn(false);

		// Test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "user1")).andExpect(status().isForbidden());
		log.info("TEST EXECUTION END - testGetPensionDetailsWithInvalidToken()");
	}

	@Test
	@DisplayName("Test for Response with Invalid Input via /ProcessPension")
	void testPensionInputwithInvalidInput() throws Exception {
		log.info("TEST EXECUTION START - testPensionInputwithInvalidInput()");
		
		// Mock ProcessPensionService Response
		when(processPensionService.getPensionDetails(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new NotFoundException("Details entered are incorrect"));

		// Test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Details entered are incorrect")));
		log.info("TEST EXECUTION END - testPensionInputwithInvalidInput()");
	}

	@Test
	@DisplayName("Test for Response When Feign Client Returns a Valid Error")
	void testPensionInputWithValidFeignResponse() throws JsonProcessingException, Exception {
		log.info("TEST EXECUTION START - testPensionInputWithValidFeignResponse()");
		
		// Mock ProcessPensionService getPensionDetails() to throw FeignException
		when(processPensionService.getPensionDetails(ArgumentMatchers.any(), ArgumentMatchers.any())).thenThrow(new FeignException.BadRequest(
				"Service is offline", Request.create(HttpMethod.GET, "", Collections.emptyMap(), null, null, null),
				objectMapper.writeValueAsBytes(new ExceptionInfo("Internal Server Error"))));

		// Test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Internal Server Error")));
		log.info("TEST EXECUTION END - testPensionInputWithValidFeignResponse()");

	}

	@Test
	@DisplayName("Test for Response when Feign Client returns Invalid Error")
	void testPensionInputWithInvalidFeignResponse() throws JsonProcessingException, Exception {
		log.info("TEST EXECUTION START - testPensionInputWithInvalidFeignResponse()");
		
		// Mock ProcessPensionService getPensionDetails() to throw FeignException
		when(processPensionService.getPensionDetails(ArgumentMatchers.any(),ArgumentMatchers.any())).thenThrow(new FeignException.BadRequest(
				"Invalid Response", Request.create(HttpMethod.GET, "", Collections.emptyMap(), null, null, null),
				"Unknown error response".getBytes()));

		// Test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Unknown error response")));
		log.info("TEST EXECUTION END - testPensionInputWithInvalidFeignResponse()");
	}

	@Test
	@DisplayName("Test for Empty Response from Feign Client")
	void testPensionInputWithEmptyFeignResponse() throws JsonProcessingException, Exception {
		log.info("TEST EXECUTION START - testPensionInputWithEmptyFeignResponse()");
		
		// Mock ProcessPensionService getPensionDetails() to throw FeignException
		when(processPensionService.getPensionDetails(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new FeignException.BadRequest("Invalid Response",
						Request.create(HttpMethod.GET, "", Collections.emptyMap(), null, null, null), "".getBytes()));

		// Test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Invalid Request")));
		log.info("TEST EXECUTION END - testPensionInputWithEmptyFeignResponse()");
	}

	

}
