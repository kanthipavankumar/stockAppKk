package com.kk.sprboot.stockApp.exceptionHandler;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StockExceptionHandler {
	
	@ExceptionHandler
	public ResponseEntity<StockErrorResponse> handleException(StockNotFoundException e){
		//create a StockErrorResponse
		StockErrorResponse error = new StockErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(e.getMessage());
		error.setTimeStamp(new Timestamp(System.currentTimeMillis()));
		
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<StockErrorResponse> handleException(StockDuplicateException e){
		//create a StockErrorResponse
		StockErrorResponse error = new StockErrorResponse();
		error.setStatus(HttpStatus.CONFLICT.value());
		error.setMessage(e.getMessage());
		error.setTimeStamp(new Timestamp(System.currentTimeMillis()));
		
		return new ResponseEntity<>(error,HttpStatus.CONFLICT);
	}
	

	
	//Add a generic exception handler - to catch all exceptions
	@ExceptionHandler
	public ResponseEntity<StockErrorResponse> handleException(Exception e){
		//create a StockErrorResponse
		StockErrorResponse error = new StockErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(e.getMessage());
		error.setTimeStamp(new Timestamp(System.currentTimeMillis()));
		
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}

}
