package com.pension.management.processpensionmicroservice.processpension.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.pension.management.processpensionmicroservice.exception.NotFoundException;
import com.pension.management.processpensionmicroservice.feign.PensionerDetailsClient;
import com.pension.management.processpensionmicroservice.model.Bank;
import com.pension.management.processpensionmicroservice.model.PensionDetails;
import com.pension.management.processpensionmicroservice.model.PensionerDetails;
import com.pension.management.processpensionmicroservice.model.PensionerInput;
import com.pension.management.processpensionmicroservice.model.util.DateUtil;
import com.pension.management.processpensionmicroservice.repository.PensionerDetailRepository;
import com.pension.management.processpensionmicroservice.service.ProcessPensionServiceImpl;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class ProcessPensionServiceTest {
	
	private static Logger log = LoggerFactory.getLogger(ProcessPensionServiceTest.class);
	
	@Autowired
	private ProcessPensionServiceImpl processPensionService;

	@MockBean
	private PensionerDetailsClient pensionerDetailClient;

	@MockBean
	private PensionerDetailRepository pensionDetailsRepository;

	@Test
	@DisplayName("Test for Pensioner Input Detail for Pension Type - Self")
	void testCheckDetailsForCorrectPensionerInputForSelf() throws ParseException {
		log.info("TEST EXECUTION START - testCheckDetailsForCorrectPensionerInputForSelf()");
		PensionerInput input = new PensionerInput("Deep", DateUtil.parseDate("11-06-1996"), "LKJHG1996",
				"914253867543", "self");
		Bank bank = new Bank("SBI", 852258, "public");

		PensionerDetails details = new PensionerDetails("Deep", DateUtil.parseDate("11-06-1996"), "LKJHG1996", 100000,
				10000, "self", bank);

		assertTrue(processPensionService.checkDetails(input, details));
		assertEquals(852258, bank.getAccountNumber());
		assertNotNull(details);
		log.info("TEST EXECUTION END - testCheckDetailsForCorrectPensionerInputForSelf()");
	}

	@Test
	@DisplayName("Test Pensioner Input Detail for Pension Type - Family")
	void testCheckDetailsForCorrectPensionerInputForFamily() throws ParseException {
		log.info("TEST EXECUTION START - testCheckDetailsForCorrectPensionerInputForFamily()");
		PensionerInput input = new PensionerInput("Deep", DateUtil.parseDate("11-06-1996"), "QSCZS2233",
				"917346821937", "family");
		Bank bank = new Bank("SBI", 753951, "private");

		PensionerDetails details = new PensionerDetails("Deep", DateUtil.parseDate("11-06-1996"), "QSCZS2233", 100000,
				10000, "family", bank);

		assertTrue(processPensionService.checkDetails(input, details));
		log.info("TEST EXECUTION END - testCheckDetailsForCorrectPensionerInputForFamily()");
	}

	@Test
	@DisplayName("Test Pensioner Input Detail for incorrect Self Pension Type")
	void testCheckDetailsForIncorrectPensionerInputForSelf() throws ParseException {
		log.info("TEST EXECUTION START - testCheckDetailsForIncorrectPensionerInputForSelf()");
		PensionerInput input = new PensionerInput("Deep", DateUtil.parseDate("11-06-1996"), "GHTY1593",
				"917346821937", "self");
		Bank bank = new Bank("SBI", 789987, "public");
		PensionerDetails details = new PensionerDetails("Deep", DateUtil.parseDate("11-06-1996"), "ERWE1421", 100000,
				10000, "self", bank);

		assertFalse(processPensionService.checkDetails(input, details));
		log.info("TEST EXECUTION END - testCheckDetailsForIncorrectPensionerInputForSelf()");
	}

	@Test
	@DisplayName("Test for Wrong Pensioner Input Detail")
	void testCheckDetailsForIncorrectPensionerInput() throws ParseException {
		log.info("TEST EXECUTION START - testCheckDetailsForIncorrectPensionerInput()");
		PensionerInput input = new PensionerInput("Deep", DateUtil.parseDate("11-06-1996"), "GHTY1593",
				"917346821937", "family");
		Bank bank = new Bank("SBI", 789987, "public");
		PensionerDetails details = new PensionerDetails("Deep", DateUtil.parseDate("11-06-1996"), "ERWE1421", 100000,
				10000, "family", bank);

		assertFalse(processPensionService.checkDetails(input, details));
		log.info("TEST EXECUTION END - testCheckDetailsForIncorrectPensionerInput()");
	}

	@Test
	@DisplayName("Test for Pension detail via PensionerDetail for Pension Type - Self")
	void testCheckPensionDetailByPassingPensionerDetailsForSelf() throws ParseException {
		log.info("TEST EXECUTION START - testCheckPensionDetailByPassingPensionerDetailsForSelf()");
		PensionerInput input = new PensionerInput("Deep", DateUtil.parseDate("11-06-1996"), "LKJHG1996",
				"914253867543", "self");
		Bank bank = new Bank("SBI", 852258, "public");
		
		PensionerDetails details = new PensionerDetails("Deep", DateUtil.parseDate("11-06-1996"), "ERWE1421", 100000,
				10000, "self", bank);

		PensionDetails actualDetail = processPensionService.calculatePensionAmount(details, input);

		assertEquals(90000, actualDetail.getPensionAmount());
		log.info("TEST EXECUTION END - testCheckPensionDetailByPassingPensionerDetailsForSelf()");
	}

	@Test
	@DisplayName("Test for Pension detail via PensionerDetail for Pension Type - Family")
	void testCheckPensionDetailByPassingPensionerDetalisForFamily() throws ParseException {
		log.info("TEST EXECUTION START - testCheckPensionDetailByPassingPensionerDetalisForFamily()");
		PensionerInput input = new PensionerInput("Deep", DateUtil.parseDate("11-06-1996"), "LKJHG1996",
				"914253867543", "self");
		Bank bank = new Bank("SBI", 789987, "public");
		PensionerDetails details = new PensionerDetails("Deep", DateUtil.parseDate("11-06-1996"), "ERWE1421", 100000,
				10000, "family", bank);

		PensionDetails actualDetail = processPensionService.calculatePensionAmount(details, input);

		assertEquals(60000, actualDetail.getPensionAmount());
		log.info("TEST EXECUTION END - testCheckPensionDetailByPassingPensionerDetalisForFamily()");
	}

	@Test
	@DisplayName("Test for getPensionDetails()")
	void testCheckGetPensionDetails() throws ParseException, javassist.NotFoundException {
		log.info("TEST EXECUTION START - testCheckGetPensionDetails()");
		PensionerInput pensionerInput = new PensionerInput("Deep", DateUtil.parseDate("11-06-1996"), "ERWE1421",
				"917346821937", "family");

		Bank bank = new Bank("SBI", 789987, "public");

		PensionerDetails details_family = new PensionerDetails("Deep", DateUtil.parseDate("11-06-1996"), "ERWE1421",
				100000, 10000, "family", bank);

		// mock the feign client
		when(pensionerDetailClient.getPensionerDetailByAadhaar("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",pensionerInput.getAadhaarNumber()))
				.thenReturn(details_family);

		// get the actual result
		PensionDetails pensionDetailFamily = processPensionService.getPensionDetails("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",pensionerInput);

		// test cases
		assertEquals(60000, pensionDetailFamily.getPensionAmount());
		assertNotNull(pensionDetailFamily);
		log.info("TEST EXECUTION END - testCheckGetPensionDetails()");
	}

	@Test
	@DisplayName("Test getPensionDetails() for Pension type - Self")
	void testCheckGetPensionDetailsforSelf() throws ParseException, NotFoundException, Exception {
		log.info("TEST EXECUTION START - testCheckGetPensionDetailsforSelf()");
		PensionerInput input = new PensionerInput("Deep", DateUtil.parseDate("11-06-1996"), "ERWE1421",
				"917346821937", "self");

		Bank bank = new Bank("SBI", 789987, "public");

		PensionerDetails details_family = new PensionerDetails("Deep", DateUtil.parseDate("11-06-1996"), "ERWE1421",
				100000, 10000, "self", bank);

		// Mock Feign Client
		when(pensionerDetailClient.getPensionerDetailByAadhaar("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",input.getAadhaarNumber()))
				.thenReturn(details_family);

		// Fetch Actual Result
		PensionDetails pensionDetailFamily = processPensionService.getPensionDetails("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",input);

		// Tests
		assertEquals(90000, pensionDetailFamily.getPensionAmount());
		assertNotNull(pensionDetailFamily);
		log.info("TEST EXECUTION END - testCheckGetPensionDetailsforSelf()");
	}
	
}
