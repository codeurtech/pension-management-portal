package com.pension.management.processpensionmicroservice.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PensionDetails {
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date dateOfBirth;
	private String pan;
	private String pensionType;
	private double pensionAmount;
	private double bankCharges;

	public PensionDetails(String name, Date dateOfBirth, String pan, String pensionType, double pensionAmount,
			double bankCharges) {
		super();
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.pan = pan;
		this.pensionType = pensionType;
		this.pensionAmount = pensionAmount;
		this.bankCharges = bankCharges;
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

	public String getPensionType() {
		return pensionType;
	}

	public double getPensionAmount() {
		return pensionAmount;
	}

	public double getBankCharges() {
		return bankCharges;
	}
}
