package com.pension.management.authorizationservice.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.pension.management.authorizationservice.exception.InvalidCredentialsException;
import com.pension.management.authorizationservice.model.User;
import com.pension.management.authorizationservice.repository.UserRepository;
import com.pension.management.authorizationservice.service.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class UserServiceTests {
	
	private static Logger log = LoggerFactory.getLogger(UserServiceTests.class);

	@Autowired
	private UserServiceImpl userServiceImpl;

	@MockBean
	private UserRepository userRepository;

	@Test
	@DisplayName("Test for loadUserByUsername() when Username is valid")
	void testLoadUserByUsernameWithValidUsername() {
		log.info("TEST EXECUTION START - testLoadUserByUsernameWithValidUsername()");

		User user = new User("sysadmin", "$2a$10$K1dncf7hu5eb7qrf33t1RenzzWx32krLr87pbBQQ6kGEgKlw4j/1e");

		Optional<User> userOptional = Optional.of(user);

		final String id = "sysadmin";

		SecurityUser securityUser = new SecurityUser(id, user.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));

		when(userRepository.findById(id)).thenReturn(userOptional);

		log.info("Executing the test case...");

		assertEquals(userServiceImpl.loadUserByUsername(id), securityUser);
		assertNotNull(securityUser);

		log.info("TEST EXECUTION END - testLoadUserByUsernameWithValidUsername()");
	}

	@Test
	@DisplayName("Test for loadUserByUsername() when Username is Invalid")
	void testLoadUserByUsernameWithInvalidUsername() {
		log.info("TEST EXECUTION START - testLoadUserByUsernameWithInvalidUsername()");

		User user = new User("admin", "$2a$12$t.1BTGVYIkKe8S5H6BC9N.sAmNp0DytK03BqEnZc63fObgeGqvQh.");

		Optional<User> userOptional = Optional.empty();

		final String id = "admin";

		SecurityUser securityUser = new SecurityUser(id, user.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));

		when(userRepository.findById(id)).thenReturn(userOptional);

		log.info("Running the test case...");

		InvalidCredentialsException thrownException = assertThrows(InvalidCredentialsException.class,
				() -> userServiceImpl.loadUserByUsername(id));

		assertTrue(thrownException.getMessage().contains("USER NOT PRESENT"));
		assertNotNull(securityUser);

		log.info("TEST EXECUTION END - testLoadUserByUsernameWithInvalidUsername()");
	}

	@MockBean
	public class SecurityUser extends org.springframework.security.core.userdetails.User {

		private static final long serialVersionUID = 1L;

		public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
			super(username, password, authorities);
		}

	}
}
