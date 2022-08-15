package com.pension.management.authorizationservice.controller.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.Collections;

import org.apache.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pension.management.authorizationservice.exception.InvalidTokenException;
import com.pension.management.authorizationservice.model.UserRequest;
import com.pension.management.authorizationservice.service.UserServiceImpl;
import com.pension.management.authorizationservice.util.JWTUtil;

import lombok.extern.slf4j.Slf4j;

@WebMvcTest
@Slf4j
class AuthorizationControllerTests {
	
	private static Logger log = LoggerFactory.getLogger(AuthorizationControllerTests.class);
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserServiceImpl userServiceImpl;

	@MockBean
	private JWTUtil jwtUtil;

	@MockBean
	private AuthenticationManager authenticationManager;

	@Value("${userDetails.errorMessage}")
	private String ERROR_MESSAGE;

	@Value("${userDetails.badCredentialsMessage}")
	private String BAD_CREDENTIALS_MESSAGE;

	@Value("${userDetails.disabledAccountMessage}")
	private String DISABLED_ACCOUNT_MESSAGE;

	@Value("${userDetails.lockedAccountMessage}")
	private String LOCKED_ACCOUNT_MESSAGE;

	private static ObjectMapper mapper = new ObjectMapper();
	private static SecurityUser validUser;
	private static SecurityUser invalidUser;

	@BeforeEach
	void generateUserCredentials() {
		validUser = new SecurityUser("admin", "$2a$10$aMMcsBB18R7dqzC7Wcg3z.oiVQnNhgFGD0WMTZVeVtFCMMnru25AO",
				Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
		invalidUser = new SecurityUser("admin1", "$2a$10$aMMcsBB18R7dqzC7Wcg3z.oiVQnNhgFGD0WMTZVeVtFCMMnru25AO",
				Collections.singletonList(new SimpleGrantedAuthority("USER")));
	}

	@Test
	@DisplayName("Test for authentication with Valid Credentials")
	void testAuthenticationWithValidCredentials() throws Exception {
		log.info("TEST EXECUTION START - testAuthenticationWithValidCredentials()");

		UserRequest user = new UserRequest("sysadmin", "sysadmin123");

		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
		log.info("token: {}", token);

		when(authenticationManager.authenticate(ArgumentMatchers.any()))
				.thenReturn(new TestingAuthenticationToken("sysadmin", "sysadmin123", "ADMIN"));
		when(jwtUtil.generateToken(ArgumentMatchers.any())).thenReturn(token);

		String json = mapper.writeValueAsString(user);
		log.info("Input data {}", json);

		MvcResult result = mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		System.out.println(contentAsString);

		assertNotNull(result);

		assertEquals(contentAsString,"{\"token\":\""+token+"\"}");

		log.info("TEST EXECUTION END - testAuthenticationWithValidCredentials()");
	}

	@Test
	@DisplayName("Test for Authentication with Invalid Credentials")
	void testAuthenticationRequestWithInvalidCredentials() throws Exception {
		log.info("TEST EXECUTION START - testAuthenticationRequestWithInvalidCredentials()");

		UserRequest user = new UserRequest("admin1", "admin1");

		when(authenticationManager.authenticate(ArgumentMatchers.any()))
				.thenThrow(new BadCredentialsException("Invalid Username or Password. Please enter correct username or password."));

		String json = mapper.writeValueAsString(user);
		log.info("Input data {}", json);

		mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Invalid Username or Password. Please enter correct username or password.")));

		log.info("TEST EXECUTION END - testAuthenticationRequestWithInvalidCredentials()");
	}

	@Test
	@DisplayName("Test for Authentication with Locked Credentials")
	void testAuthenticationWithLockedCredentials() throws Exception {
		log.info("TEST EXECUTION START - testAuthenticationWithLockedCredentials()");

		UserRequest user = new UserRequest("admin1", "admin1");

		when(authenticationManager.authenticate(ArgumentMatchers.any())).thenThrow(new LockedException(
				"Your account has been locked. Please contact your administrator for more details."));

		String json = mapper.writeValueAsString(user);
		log.info("Input data {}", json);

		mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers
						.equalTo("Your account has been locked. Please contact your administrator for more details.")));

		log.info("TEST EXECUTION END - testAuthenticationWithLockedCredentials()");
	}

	@Test
	@DisplayName("Test for Authentication with Disabled Account Credentials")
	void testAuthenticationWithDisabledAccountCredentials() throws Exception {
		log.info("TEST EXECUTION START - testAuthenticationWithDisabledAccountCredentials()");

		UserRequest user = new UserRequest("admin1", "admin1");

		when(authenticationManager.authenticate(ArgumentMatchers.any())).thenThrow(new DisabledException(
				"Your account has been disabled. Please contact your administrator for more details."));

		String json = mapper.writeValueAsString(user);
		log.info("Input data {}", json);

		mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo(
						"Your account has been disabled. Please contact your administrator for more details.")));

		log.info("TEST EXECUTION END - testAuthenticationWithDisabledAccountCredentials()");
	}
	
	@Test
	@DisplayName("Test to Authorization with Valid Token")
	void testAuthorizationWithValidToken() throws Exception {
		log.info("TEST EXECUTION START - testAuthorizationWithValidToken()");

		when(userServiceImpl.loadUserByUsername(ArgumentMatchers.any())).thenReturn(validUser);
		when(jwtUtil.isTokenExpiredOrInvalidFormat(ArgumentMatchers.any())).thenReturn(false);

		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
		log.info("Token: {}", token);

		mockMvc.perform(get("/authorize").header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk());

		log.info("TEST EXECUTION END - testAuthorizationWithValidToken()");
	}

	@Test
	@DisplayName("Test for Authorization with Invalid/Expired Token")
	void testAuthorizationWithInvalidOrExpiredToken() throws Exception {
		log.info("TEST EXECUTION START - testAuthorizationWithInvalidOrExpiredToken()");
		final String errorMessage = "TOKEN EXPIRED";

		when(userServiceImpl.loadUserByUsername(ArgumentMatchers.any())).thenReturn(validUser);
		when(jwtUtil.isTokenExpiredOrInvalidFormat(ArgumentMatchers.any()))
				.thenThrow(new InvalidTokenException(errorMessage));

		String token = "fyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
		log.info("Token: {}", token);

		mockMvc.perform(get("/authorize").header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isBadRequest());

		log.info("TEST EXECUTION END - testAuthorizationWithInvalidOrExpiredToken()");
	}

	@Test
	@DisplayName("Test for Authorization with Invalid Role")
	void testAuthorizationWithInvalidRole() throws Exception {
		log.info("TEST EXECUTION START - testAuthorizationWithInvalidRole()");

		// mock certain functionalities to load invalid user and have a valid token
		when(userServiceImpl.loadUserByUsername(ArgumentMatchers.any())).thenReturn(invalidUser);
		when(jwtUtil.isTokenExpiredOrInvalidFormat(ArgumentMatchers.any())).thenReturn(false);

		// set the invalid token
		String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
		log.info("Token: {}", token);

		// perform the mock
		mockMvc.perform(get("/authorize").header(HttpHeaders.AUTHORIZATION, token))
				.andExpect(status().isUnauthorized());

		log.info("TEST EXECUTION END - testAuthorizationWithInvalidRole()");
	}

	@Test
	@DisplayName("Method for HealthCheck")
	void testHealthCheck() throws Exception {
		log.info("TEST EXECUTION START - testHealthCheck()");

		MvcResult result = mockMvc.perform(get("/health-check")).andExpect(status().is2xxSuccessful()).andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		assertEquals("Authorization Microservice - OK", contentAsString);
		assertNotNull(result);

		log.info("TEST EXECUTION END - testHealthCheck()");
	}

	public class SecurityUser extends org.springframework.security.core.userdetails.User {
		 
		private static final long serialVersionUID = 1L;

		public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
			super(username, password, authorities);
		}

	}

	

}
