package com.pension.management.processpensionmicroservice.exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	private Logger log=LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@Autowired
	private ObjectMapper objectMapper;

	@PostConstruct
	public void setUp() {
		objectMapper.registerModule(new JavaTimeModule());
	}

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		log.error("Handling method argement not valid in Process pension microservice");
		ExceptionInfo response = new ExceptionInfo();
		response.setMessage("Invalid Credentials");
		response.setTimestamp(LocalDateTime.now());

		// Mechanism to store validation errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		// Add errors to the response map
		response.setFieldErrors(errors);
		log.error(errors.toString());
		return new ResponseEntity<>(response, headers, status);
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<ExceptionInfo> handleFeignStatusException(FeignException ex,
			HttpServletResponse response) {
		log.error("Handling Feign Client in Process Pension microservice...");
		log.debug("Message: {}", ex.getMessage());
		ExceptionInfo errorResponse;
		log.debug("UTF-8 Message: {}", ex.contentUTF8());
		if (ex.contentUTF8().isBlank()) {
			errorResponse = new ExceptionInfo("Invalid Request");
		} else {
			try {
				log.debug("Trying...");
				errorResponse = objectMapper.readValue(ex.contentUTF8(), ExceptionInfo.class);
				log.debug("Success in parsing the error...");
			} catch (JsonProcessingException e) {
				errorResponse = new ExceptionInfo(ex.contentUTF8());
				log.error("Processing Error {}", e.toString());
			}
		}
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ExceptionInfo> handleNotFoundException(NotFoundException ex,
			HttpServletResponse response) {
		log.error("Handling Details mismatch exception in Process Pension microservice");
		ExceptionInfo errorResponse = new ExceptionInfo();
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setFieldErrors(Collections.singletonList(ex.getMessage()));
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ExceptionInfo> handleInvalidTokenException(InvalidTokenException ex,
			HttpServletResponse response) {
		log.error("Handling Invalid Token exception in Process Pension microservice");
		ExceptionInfo errorResponse = new ExceptionInfo();
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setFieldErrors(Collections.singletonList(ex.getMessage()));
		return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	}

}
