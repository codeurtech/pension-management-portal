package com.pension.management.processpensionmicroservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pension.management.processpensionmicroservice.controller.ProcessPensionController;
import com.pension.management.processpensionmicroservice.feign.AuthorizationClient;
import com.pension.management.processpensionmicroservice.feign.PensionerDetailsClient;
import com.pension.management.processpensionmicroservice.model.PensionAmount;
import com.pension.management.processpensionmicroservice.model.PensionDetails;
import com.pension.management.processpensionmicroservice.model.PensionerDetails;
import com.pension.management.processpensionmicroservice.model.PensionerInput;
import com.pension.management.processpensionmicroservice.repository.PensionAmountRepository;
import com.pension.management.processpensionmicroservice.repository.PensionerDetailRepository;

import javassist.NotFoundException;



@Service
public class ProcessPensionServiceImpl {
	
	private static Logger log = LoggerFactory.getLogger(ProcessPensionServiceImpl.class);
	
	@Autowired
	private PensionerDetailsClient pensionerDetailClient;
	
	@Autowired
	AuthorizationClient authorizationClient;
		
	@Autowired
	private PensionAmountRepository pensionAmountRepository;

	@Autowired
	private PensionerDetailRepository pensionerDetailsRepository;

	public PensionDetails getPensionDetails(String requestTokenHeader,PensionerInput pensionerInput) throws NotFoundException {

		PensionerDetails pensionerDetail = pensionerDetailClient
				.getPensionerDetailByAadhaar(requestTokenHeader,pensionerInput.getAadhaarNumber());

		log.info("Details found");

		if (checkDetails(pensionerInput, pensionerDetail)) {
			pensionerDetailsRepository.save(pensionerInput);

			return calculatePensionAmount(pensionerDetail,pensionerInput);
		} else {
			throw new NotFoundException("INCORRECT DETAILS");
		}
	}

	public PensionDetails calculatePensionAmount(PensionerDetails pensionerDetail, PensionerInput pensionerInput) {
		double pensionAmount = 0;
		double bankCharges = 0;
		String bankType = pensionerDetail.getBank().getBankType();
		if (bankType.equalsIgnoreCase("public")) {
			bankCharges = 500;
		} else if (bankType.equalsIgnoreCase("private")) {
			bankCharges = 550;
		}
		if (pensionerDetail.getPensionType().equalsIgnoreCase("self"))
			pensionAmount = (pensionerDetail.getSalary() * 0.8 + pensionerDetail.getAllowance());
		else if (pensionerDetail.getPensionType().equalsIgnoreCase("family"))
			pensionAmount = (pensionerDetail.getSalary() * 0.5 + pensionerDetail.getAllowance());
		pensionAmountRepository.save(new PensionAmount(pensionerInput.getAadhaarNumber(),pensionAmount, bankCharges, (pensionAmount-bankCharges)));
		return new PensionDetails(pensionerDetail.getName(), pensionerDetail.getDateOfBirth(), pensionerDetail.getPan(),
				pensionerDetail.getPensionType(), pensionAmount, bankCharges);
	}

	public boolean checkDetails(PensionerInput pensionerInput, PensionerDetails pensionerDetail) {
		return (pensionerInput.getName().equalsIgnoreCase(pensionerDetail.getName())
				&& (pensionerInput.getDateOfBirth().compareTo(pensionerDetail.getDateOfBirth()) == 0)
				&& pensionerInput.getPan().equalsIgnoreCase(pensionerDetail.getPan())
				&& pensionerInput.getPensionType().equalsIgnoreCase(pensionerDetail.getPensionType()));
	}

}
