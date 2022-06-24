package com.kk.sprboot.stockApp.exceptionHandler;

public class StockDuplicateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public StockDuplicateException(String message) {
		super(message);
	}
	

}
