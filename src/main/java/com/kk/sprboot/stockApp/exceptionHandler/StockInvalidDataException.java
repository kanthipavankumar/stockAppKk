package com.kk.sprboot.stockApp.exceptionHandler;

public class StockInvalidDataException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public StockInvalidDataException(String message) {
		super(message);
	}
	
	

}
