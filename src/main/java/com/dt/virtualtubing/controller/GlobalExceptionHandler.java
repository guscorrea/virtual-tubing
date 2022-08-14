package com.dt.virtualtubing.controller;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dt.virtualtubing.exception.PdgNotFoundException;
import com.dt.virtualtubing.exception.TubingNotFoundException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(TubingNotFoundException.class)
	public ResponseEntity<List<String>> handleNotFoundException(TubingNotFoundException e) {
		return new ResponseEntity<>(toListMessages(e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PdgNotFoundException.class)
	public ResponseEntity<List<String>> handlePdgNotFoundException(PdgNotFoundException e) {
		return new ResponseEntity<>(toListMessages(e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<List<String>> handleDateTimeParseException(DateTimeParseException e) {
		return new ResponseEntity<>(toListMessages(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidFormatException.class)
	protected ResponseEntity<List<String>> handleInvalidFormatException(InvalidFormatException e) {
		return new ResponseEntity<>(toListMessages(e.getOriginalMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<List<String>> handleIllegalArgumentException(IllegalArgumentException e) {
		return new ResponseEntity<>(toListMessages(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<List<String>> handleJsonMappingExceptionException(JsonMappingException e) {
		return new ResponseEntity<>(toListMessages(e.getOriginalMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException e) {
		BindingResult result = e.getBindingResult();
		List<String> errorMessages = new ArrayList<>();
		for (FieldError fieldError : result.getFieldErrors()) {
			errorMessages.add(String.format("Field %s: %s", fieldError.getField(), fieldError.getDefaultMessage()));
		}
		return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
	}

	private List<String> toListMessages(String message) {
		return Collections.singletonList(message);
	}

}
