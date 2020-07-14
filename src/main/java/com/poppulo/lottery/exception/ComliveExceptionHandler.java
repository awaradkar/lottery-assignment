package com.poppulo.lottery.exception;

import com.poppulo.lottery.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class ComliveExceptionHandler extends ResponseStatusExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorDetails> errorDetails(EntityNotFoundException ex, WebRequest request) {
		String message = "Record not found for the id:" + ex.getMessage();
		ErrorDetails errorDetails = new ErrorDetails(new Date(), message, request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> genErrorDetails(Exception ex, WebRequest request) {
		String message = "Error in processing request. Following error occured:" + ex.getMessage();
		ErrorDetails errorDetails = new ErrorDetails(new Date(), message, request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
