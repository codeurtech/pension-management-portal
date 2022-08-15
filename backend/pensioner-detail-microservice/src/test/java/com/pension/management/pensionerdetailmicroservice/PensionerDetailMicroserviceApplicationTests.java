package com.pension.management.pensionerdetailmicroservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pension.management.pensionerdetailmicroservice.service.PensionerDetailsServiceImpl;


@SpringBootTest
class PensionerDetailMicroserviceApplicationTests {

	@Autowired
	PensionerDetailsServiceImpl pensionerDetailService;
	
	@Test
	void contextLoads() {
		PensionerDetailMicroserviceApplication.main(new String[] {});
		assertThat(pensionerDetailService).isNotNull();
	}

}
