package com.pension.management.pensionerdetailmicroservice.service;

import com.pension.management.pensionerdetailmicroservice.model.PensionerDetails;

public interface PensionerDetailsService {
	public PensionerDetails getPensionerDetailByAadhaarNumber(String aadhaarNumber);
}
