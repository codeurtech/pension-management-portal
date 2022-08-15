package com.pension.management.processpensionmicroservice.processpension.repository.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pension.management.processpensionmicroservice.model.PensionAmount;
import com.pension.management.processpensionmicroservice.model.PensionerInput;
import com.pension.management.processpensionmicroservice.model.util.DateUtil;
import com.pension.management.processpensionmicroservice.repository.PensionAmountRepository;
import com.pension.management.processpensionmicroservice.repository.PensionerDetailRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class ProcessPensionServiceRepositoryTest {
	
	private static Logger log = LoggerFactory.getLogger(ProcessPensionServiceRepositoryTest.class);

	@Autowired
	private PensionerDetailRepository pensionerDetailRepository;
	
	@Autowired
	private PensionAmountRepository pensionAmountRepository;

	@Test
	@DisplayName("Test for save() for Pension Amount")
	void testSaveForPensionAmount() {
		log.info("TEST EXECUTION START - testSave()");

		PensionAmount pensionAmount = new PensionAmount();
		pensionAmount.setAadhaarNumber("801993153377");
		pensionAmount.setBankServiceCharge(500.00);
		pensionAmount.setPensionAmount(21600.00);
		pensionAmount.setFinalAmount(21000.00);

		PensionAmount savedDetails = pensionAmountRepository.save(pensionAmount);
		assertEquals(savedDetails.getAadhaarNumber(), pensionAmount.getAadhaarNumber());
		assertEquals(savedDetails.getBankServiceCharge(), pensionAmount.getBankServiceCharge());
		assertEquals(savedDetails.getPensionAmount(), pensionAmount.getPensionAmount());
		assertEquals(savedDetails.getFinalAmount(), pensionAmount.getFinalAmount());

		assertNotNull(savedDetails);
		log.info("TEST EXECUTION END - testSave()");
	}

	@Test
	@DisplayName("Test save() for Pensioner Details")
	void testSaveForPensionerDetails() throws ParseException {
		log.info("TEST EXECUTION START - testSaveForPensionerDetails()");

		PensionerInput pensionerInput = new PensionerInput("Himanshu", DateUtil.parseDate("11-06-1996"), "Q3ERR3ELLH",
				"873006088777", "family");

		PensionerInput savedDetails = pensionerDetailRepository.save(pensionerInput);
		assertEquals(savedDetails.getAadhaarNumber(), pensionerInput.getAadhaarNumber());
		assertNotNull(savedDetails);

		log.info("TEST EXECUTION END - testSaveForPensionerDetails()");
	}
}
