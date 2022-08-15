package com.pension.management.authorizationservice.controller;

import javax.validation.Valid;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pension.management.authorizationservice.exception.InvalidCredentialsException;
import com.pension.management.authorizationservice.model.UserRequest;
import com.pension.management.authorizationservice.util.JWTUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
//@CrossOrigin("*")
public class AuthorizationController {
	
	private static Logger log = LoggerFactory.getLogger(AuthorizationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserDetailsService userService;

	@RequestMapping(value="/authenticate", method=RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> authenticateTheRequest(@RequestBody @Valid UserRequest userRequest)
			throws InvalidCredentialsException {
		log.info("EXECUTION START -> authenticateTheRequest()");
		try {
			Authentication authenticate = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
			if (authenticate.isAuthenticated()) {
				log.info("USER LOCATED SUCCESSFULLY -> Login() successful.");
			}
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException(
					"Invalid Username or Password. Please enter correct username or password.");
		} catch (DisabledException e) {
			throw new InvalidCredentialsException(
					"Your account has been disabled. Please contact your administrator for more details.");
		} catch (LockedException e) {
			throw new InvalidCredentialsException(
					"Your account has been locked. Please contact your administrator for more details.");
		}

		String token = jwtUtil.generateToken(userRequest.getUsername());
		JSONObject resp = new JSONObject();
		resp.put("token",token);
		log.info("EXECUTION END -> authenticateTheRequest()");
		return new ResponseEntity<String>(resp.toString(), HttpStatus.OK);
	}

	@GetMapping("/authorize")
	public ResponseEntity<Boolean> authorizeTheRequest(
			@RequestHeader(name = "Authorization", required = true) String requestTokenHeader) {
		log.info("EXECUTION START -> authorizeTheRequest()");

		jwtUtil.isTokenExpiredOrInvalidFormat(requestTokenHeader);

		String userName = jwtUtil.getUsernameFromToken(requestTokenHeader);
		UserDetails user = userService.loadUserByUsername(userName);
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
			log.info("EXECUTION END -> authorizeTheRequest()");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}

		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);

	}

	@GetMapping("/health-check")
	public ResponseEntity<String> healthCheck() {
		return new ResponseEntity<>("Authorization Microservice - OK", HttpStatus.OK);
	}
}
