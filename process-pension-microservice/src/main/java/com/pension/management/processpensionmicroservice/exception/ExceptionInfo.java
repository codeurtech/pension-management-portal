package com.pension.management.processpensionmicroservice.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ExceptionInfo {

	private String message;
	private LocalDateTime timestamp;

	public ExceptionInfo(String message) {
		this.timestamp = LocalDateTime.now();
		this.message = message;
	}

	@JsonInclude(Include.NON_NULL)
	private List<String> fieldErrors;

	public ExceptionInfo() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public List<String> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<String> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}
