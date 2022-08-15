package com.pension.management.pensionerdetailmicroservice.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ExceptionInfo {
	private String message;
	private LocalDateTime timestamp;
	@JsonInclude(Include.NON_NULL)
	private List<String> fieldErrors;

	public ExceptionInfo(String message, LocalDateTime timestamp, List<String> fieldErrors) {
		super();
		this.message = message;
		this.timestamp = timestamp;
		this.fieldErrors = fieldErrors;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public List<String> getFieldErrors() {
		return fieldErrors;
	}
}
