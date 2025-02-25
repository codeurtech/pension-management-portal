package com.pension.management.authorizationservice;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pension.management.authorizationservice.service.UserServiceImpl;

@SpringBootTest
class AuthorizationMicroserviceApplicationTests {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Test
	void contextLoads() {
		AuthorizationMicroserviceApplication.main(new String[] {});
		assertThat(userServiceImpl).isNotNull();
	}

}
