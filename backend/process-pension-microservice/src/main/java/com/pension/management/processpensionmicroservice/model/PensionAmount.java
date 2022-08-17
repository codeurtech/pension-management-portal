package com.pension.management.processpensionmicroservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PensionAmount {
	@Id
	private String aadhaarNumber;

	@Column
	private Double pensionAmount;

	@Column
	private Double bankServiceCharge;

	@Column
	private Double finalAmount;

	public PensionAmount() {
		super();
	}

	public PensionAmount(String aadhaarNumber, Double pensionAmount, Double bankServiceCharge, Double finalAmount) {
		super();
		this.aadhaarNumber = aadhaarNumber;
		this.pensionAmount = pensionAmount;
		this.bankServiceCharge = bankServiceCharge;
		this.finalAmount = finalAmount;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public Double getPensionAmount() {
		return pensionAmount;
	}

	public void setPensionAmount(Double pensionAmount) {
		this.pensionAmount = pensionAmount;
	}

	public Double getBankServiceCharge() {
		return bankServiceCharge;
	}

	public void setBankServiceCharge(Double bankServiceCharge) {
		this.bankServiceCharge = bankServiceCharge;
	}

	public Double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Double finalAmount) {
		this.finalAmount = finalAmount;
	}
}
