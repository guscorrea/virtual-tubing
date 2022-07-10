package com.dt.virtualtubing.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dt.virtualtubing.exception.PdgNotFoundException;
import com.dt.virtualtubing.exception.TubingNotFoundException;

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

	//TODO add one for DateTimeParseException

	private List<String> toListMessages(String message) {
		return Collections.singletonList(message);
	}

}
