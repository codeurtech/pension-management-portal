package com.pension.management.pensionerdetailmicroservice.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PensionerDetails {
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd", timezone = "IST")
	private Date dateOfBirth;
	private String pan;
	private double salary;
	private double allowance;
	private String pensionType;
	private BankDetails bank;

	public PensionerDetails(String name, Date dateOfBirth, String pan, double salary, double allowance,
			String pensionType, com.pension.management.pensionerdetailmicroservice.model.BankDetails bank) {
		super();
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.pan = pan;
		this.salary = salary;
		this.allowance = allowance;
		this.pensionType = pensionType;
		this.bank = bank;
	}

	public String getName() {
		return name;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public String getPan() {
		return pan;
	}

	public double getSalary() {
		return salary;
	}

	public double getAllowance() {
		return allowance;
	}

	public String getPensionType() {
		return pensionType;
	}

	public BankDetails getBank() {
		return bank;
	}
}
