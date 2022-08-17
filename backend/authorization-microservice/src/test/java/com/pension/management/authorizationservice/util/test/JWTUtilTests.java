package com.pension.management.authorizationservice.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pension.management.authorizationservice.exception.InvalidTokenException;
import com.pension.management.authorizationservice.util.JWTUtil;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class JWTUtilTests {

	private static Logger log = LoggerFactory.getLogger(JWTUtilTests.class);

	@Autowired
	JWTUtil jwtUtil;

	@Test
	void jwtUtilNotNull() {
		assertNotNull(jwtUtil);
	}

	@Test
	@DisplayName("Test for Valid Token")
	void testValidToken() {
		log.info("TEST EXECUTION START - testValidToken()");

		final String username = "admin";

		final String token = jwtUtil.generateToken(username);
		log.info("Token: {}", token);

		assertFalse(jwtUtil.isTokenExpiredOrInvalidFormat(token));

		assertNotNull(token);

		log.info("TEST EXECUTION END - testValidToken()");
	}


	@Test
	@DisplayName("Test for Invalid Token")
	void testInvalidToken() {
		log.info("TEST EXECUTION START - testInvalidToken()");

		final String token = "eyJhbGOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzE4MTgsInN1YiI6ImFkbWluMSIsImV4cCI6MTYyNzAzMTg3OH0.iBDf8UvcnHKa-TVHHxjOQUiC3oEVGgsYrJSvD5LhUQc";
		log.info("Malformed Token: {}", token);

		InvalidTokenException thrownException = assertThrows(InvalidTokenException.class,
				() -> jwtUtil.isTokenExpiredOrInvalidFormat(token));
		assertTrue(thrownException.getMessage().contains("INVALID TOKEN FORMAT"));

		assertNotNull(token);

		log.info("TEST EXECUTION END- testInvalidToken()");
	}

	@Test
	@DisplayName("Test for Null Token")
	void testTokenForNullToken() {
		log.info("TEST EXECUTION START - testTokenForNullToken()");

		final String token = null;
		log.info("Null Token: {}", token);

		InvalidTokenException thrownException = assertThrows(InvalidTokenException.class,
				() -> jwtUtil.isTokenExpiredOrInvalidFormat(token));
		assertTrue(thrownException.getMessage().contains("NULL/EMPTY TOKEN"));

		assertNull(token);

		log.info("TEST EXECUTION END - testTokenForNullToken()");
	}

	@Test
	@DisplayName("Test for Token with Invalid Signature")
	void testTokenForInvalidSignature() {
		log.info("TEST EXECUTION START - testTokenForInvalidSignature()");

		final String token = "eyJhbGciOiJIUzI1NiJ91.eyJpYXQiOjE2MjczMjA2ODIsInN1YiI6ImFkbWluMSIsImV4cCI6MTYyNzMyMDc0Mn0.tiQjNTsiLwo7Q2EyuJeV9p187jUZVr7PCTZMs9gvBgk";
		log.info("Invalid token signature Token: {}", token);

		InvalidTokenException thrownException = assertThrows(InvalidTokenException.class,
				() -> jwtUtil.isTokenExpiredOrInvalidFormat(token));
		assertTrue(thrownException.getMessage().contains("INVALID TOKEN SIGNATURE"));

		assertNotNull(token);

		log.info("TEST EXECUTION END - testTokenForInvalidSignature()");
	}

	@Test
	@DisplayName("Test for getUsernameFromToken() to fetch Username")
	void testGetUsernameFromToken() {
		log.info("TEST EXECUTION START - testGetUsernameFromToken()");

		final String username = "admin1";

		String token = jwtUtil.generateToken(username);
		log.info("Token: {}", token);

		assertEquals(username, jwtUtil.getUsernameFromToken(token));

		log.info("TEST EXECUTION END - testGetUsernameFromToken()");
	}

}
