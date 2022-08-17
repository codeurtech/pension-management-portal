package com.pension.management.pensionerdetailmicroservice.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.pension.management.pensionerdetailmicroservice.exception.NotFoundException;
import com.pension.management.pensionerdetailmicroservice.feign.AuthorizationClient;
import com.pension.management.pensionerdetailmicroservice.model.BankDetails;
import com.pension.management.pensionerdetailmicroservice.model.PensionerDetails;
import com.pension.management.pensionerdetailmicroservice.service.PensionerDetailsService;
import com.pension.management.pensionerdetailmicroservice.service.PensionerDetailsServiceImpl;
import com.pension.management.pensionerdetailmicroservice.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class PensionDetailServiceTest {

	private static Logger log = LoggerFactory.getLogger(UserDetailsService.class);
	
	@Autowired
	PensionerDetailsServiceImpl pensionerDetailService;

	@Test
	@DisplayName("Test for Pensioner Detail Service Object being not null")
	void testPensionDetailServiceObject() {
		assertNotNull(pensionerDetailService);
	}

	@Test
	@DisplayName("Test for Valid Aadhaar Number")
	void testValidateCorrectAadhaarCardDetails()
			throws IOException, NotFoundException, NumberFormatException, ParseException, NullPointerException {

		log.info("TEST EXECUTION START - testValidateCorrectAadhaarCardDetails()");
		final String aadhaarCardNumber = "955522225724";
		PensionerDetails pensionerDetail = new PensionerDetails("955522225724", DateUtil.parseDate("20-08-1976"),
				"WIKVKH2JJO", 76280, 7137, "self", new BankDetails("SBI", 97266291436L , "public"));
		assertEquals(pensionerDetailService.getPensionerDetailByAadhaarNumber(aadhaarCardNumber).getPan(),
				pensionerDetail.getPan());
		assertEquals(pensionerDetailService.getPensionerDetailByAadhaarNumber(aadhaarCardNumber).getBank()
				.getAccountNumber(), pensionerDetail.getBank().getAccountNumber());
		log.info("TEST EXECUTION END - testValidateCorrectAadhaarCardDetails()");
	}

	@Test
	@DisplayName("Test For Invalid Aadhar Number")
	void testValidateInCorrectAadhaarCardDetails() {

		log.info("TEST EXECUTION START - testValidateInCorrectAadhaarCardDetails()");
		final String aadhaarCardNumber = "12345678";
		NotFoundException exception = assertThrows(NotFoundException.class,
				() -> pensionerDetailService.getPensionerDetailByAadhaarNumber(aadhaarCardNumber));
		assertEquals(exception.getMessage(), "Aadhaar Number not found");
		assertNotNull(exception);
		log.info("TEST EXECUTION END - testValidateInCorrectAadhaarCardDetails()");
	}
}
