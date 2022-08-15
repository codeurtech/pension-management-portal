package com.pension.management.authorizationservice.repository.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pension.management.authorizationservice.model.User;
import com.pension.management.authorizationservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class UserRepositoryTest {
	
	private static Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("Method for Validation of Existing User (User present in Database)")
	void testValidationOfExistingUser() {
		log.info("TEST EXECUTION START - testValidationOfExistingUser()()");
		final String username = "sysadmin";
		Optional<User> userOptional = userRepository.findById(username);
		assertTrue(userOptional.isPresent());
		assertEquals(username, userOptional.get().getUsername());
		log.info("END - testValidationOfExistingUser()()");
	}

	@Test
	@DisplayName("Test for Validation for Non-Existing User (User not resent in database)")
	void testValidationOfNonExistingUser() {
		log.info("TEST EXECUTION START - testValidationOfNonExistingUser()");
		final String id = "admin1";
		Optional<User> userOptional = userRepository.findById(id);
		assertTrue(userOptional.isEmpty());
		log.info("TEST EXECUTION END - testValidationOfNonExistingUser()");
	}

}
