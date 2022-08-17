package com.pension.management.authorizationservice.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ExceptionDetails {
	private String message;
	private LocalDateTime timestamp;

	@JsonInclude(Include.NON_NULL)
	private List<String> fieldErrors;

	public ExceptionDetails() {
		super();
	}

	public ExceptionDetails(String message, LocalDateTime timestamp, List<String> fieldErrors) {
		super();
		this.message = message;
		this.timestamp = timestamp;
		this.fieldErrors = fieldErrors;
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
