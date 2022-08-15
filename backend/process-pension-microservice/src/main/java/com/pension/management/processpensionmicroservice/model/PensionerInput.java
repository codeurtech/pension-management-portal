package com.pension.management.processpensionmicroservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
public class PensionerInput {
	@Column
	@NotBlank(message = "Name required")
	private String name;

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd", iso = ISO.DATE)
	private Date dateOfBirth;

	@Column
	@NotNull(message = "PAN Number cannot be null")
	@NotBlank(message = "PAN Number required")
	private String pan;

	@Id
	@Pattern(regexp = "[0-9]{12}", message = "Invalid Aadhar Number")
	private String aadhaarNumber;

	@Column
	@NotBlank(message = "Pension Type required")
	private String pensionType;

	public PensionerInput() {
		super();
	}

	public PensionerInput(@NotBlank(message = "Name required") String name, Date dateOfBirth,
			@NotNull(message = "PAN Number cannot be null") @NotBlank(message = "PAN Number required") String pan,
			@Pattern(regexp = "[0-9]{12}", message = "Invalid Aadhar Number") String aadhaarNumber,
			@NotBlank(message = "Pension Type required") String pensionType) {
		super();
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.pan = pan;
		this.aadhaarNumber = aadhaarNumber;
		this.pensionType = pensionType;
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

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public String getPensionType() {
		return pensionType;
	}

}
