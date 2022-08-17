package com.pension.management.pensionerdetailmicroservice.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.pension.management.pensionerdetailmicroservice.exception.NotFoundException;
import com.pension.management.pensionerdetailmicroservice.model.BankDetails;
import com.pension.management.pensionerdetailmicroservice.model.PensionerDetails;
import com.pension.management.pensionerdetailmicroservice.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PensionerDetailsServiceImpl {
	
	private Logger log;
	
	public PensionerDetails getPensionerDetailByAadhaarNumber(String aadhaarCardNumber) {

		String line = "";
		BufferedReader br = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream("/pensionerdetails.csv")));
		try {
			while ((line = br.readLine()) != null)
			{
				String[] person = line.split(",");
				String name = person[1];
				String pan = person[3];
				double salary = Double.parseDouble(person[4]);
				double allowance = Double.parseDouble(person[5]);
				String pensionType = person[6];
				String bankName = person[7];
				long accountNumber = Long.parseLong(person[8]);
				String bankType = person[9];

				if (aadhaarCardNumber.contentEquals(person[0])) {
					//log.info("Details found");
					return new PensionerDetails(name, DateUtil.parseDate(person[2]), pan, salary, allowance, pensionType,
							new BankDetails(bankName, accountNumber, bankType));
				}
			}
		} catch (NumberFormatException | IOException | ParseException e) {
			throw new NotFoundException("Aadhaar Number not found");
		}
		throw new NotFoundException("Aadhaar Number not found");
	}


}
