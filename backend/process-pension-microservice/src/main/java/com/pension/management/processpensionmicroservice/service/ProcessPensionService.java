package com.pension.management.processpensionmicroservice.service;

import com.pension.management.processpensionmicroservice.model.PensionDetails;
import com.pension.management.processpensionmicroservice.model.PensionerDetails;
import com.pension.management.processpensionmicroservice.model.PensionerInput;

public interface ProcessPensionService {
	public PensionDetails getPensionDetails(PensionerInput pensionerInput);

	public PensionDetails calculatePensionAmount(PensionerDetails pensionDetail);

	public boolean checkDetails(PensionerInput pensionerInput, PensionerDetails pensionerDetail);
}
