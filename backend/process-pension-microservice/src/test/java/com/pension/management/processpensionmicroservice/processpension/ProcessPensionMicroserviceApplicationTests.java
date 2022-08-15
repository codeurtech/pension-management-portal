package com.pension.management.processpensionmicroservice.processpension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.pension.management.processpensionmicroservice.ProcessPensionMicroserviceApplication;

@SpringBootTest
class ProcessPensionMicroserviceApplicationTests {

	@Test
	void contextLoads() {
		ProcessPensionMicroserviceApplication.main(new String[] {});
		assertNotNull(ProcessPensionMicroserviceApplication.class);
	}

}
