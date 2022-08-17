package com.pension.management.processpensionmicroservice.model;

public class Bank {
	private String bankName;
	private long accountNumber;
	private String bankType;

	public Bank(String bankName, long accountNumber, String bankType) {
		super();
		this.bankName = bankName;
		this.accountNumber = accountNumber;
		this.bankType = bankType;
	}

	public String getBankName() {
		return bankName;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public String getBankType() {
		return bankType;
	}
}
