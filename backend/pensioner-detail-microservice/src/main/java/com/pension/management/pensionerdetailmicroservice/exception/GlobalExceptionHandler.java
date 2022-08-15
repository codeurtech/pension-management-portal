package com.pension.management.pensionerdetailmicroservice.exception;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ExceptionInfo> handlesUserNotFoundException(NotFoundException ex) {
		ExceptionInfo response = new ExceptionInfo(ex.getMessage(), LocalDateTime.now(),
				Collections.singletonList(ex.getMessage()));
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

	}
}
