package com.kk.sprboot.stockApp.exceptionHandler;

public class StockMandatoryValuesException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public StockMandatoryValuesException(String message) {
		super(message);
	}

}
