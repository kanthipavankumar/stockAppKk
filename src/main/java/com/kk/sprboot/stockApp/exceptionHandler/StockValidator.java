package com.kk.sprboot.stockApp.exceptionHandler;

import org.springframework.stereotype.Component;

import com.kk.sprboot.stockApp.entity.Stock;

@Component
public class StockValidator {
	
	public void validateNewStockRequestData(Stock newStock) {
		//There should not be any id, creationDate, lastUpdate in the request body
		if(newStock.getId() != 0 || newStock.getCreationDate() != null || newStock.getLastUpdate() != null) {
			throw new StockInvalidDataException("Please provide only name, stockCode and currentPrice in the data");
		}
		
		//Stock name cannot be blank
		if(newStock.getName() == null || newStock.getName().isBlank()) {
			throw new StockMandatoryValuesException("Stock Name cannot be blank");
		}
		
		//Stock code cannot be blank
		if(newStock.getStockCode() == null || newStock.getStockCode().isBlank()) {
			throw new StockMandatoryValuesException("Stock Code cannot be blank");
		}

		//Current price should be a valid number
		if(newStock.getCurrentPrice() <= 0 ) {
			throw new StockMandatoryValuesException("Stock Current Price should exist and it should be a valid number");
		}
		
		
	}
	
	public void validateUpdateStockRequestData(Stock updateStock, int updateStockId) {
		if(updateStockId <= 0) {
			throw new StockInvalidDataException("Invalid stock ID for this request");
		}
		
		//There should not be any id, creationDate, lastUpdate in the request body
		if(updateStock.getCreationDate() != null || updateStock.getLastUpdate() != null) {
			throw new StockInvalidDataException("Please provide only name, stockCode and currentPrice in the data");
		}
		
		//Stock name cannot be blank
		if(updateStock.getName() == null || updateStock.getName().isBlank()) {
			throw new StockMandatoryValuesException("Stock Name cannot be blank");
		}
		
		//Stock code cannot be blank
		if(updateStock.getStockCode() == null || updateStock.getStockCode().isBlank()) {
			throw new StockMandatoryValuesException("Stock Code cannot be blank");
		}

		//Current price should be a valid number
		if(updateStock.getCurrentPrice() <= 0 ) {
			throw new StockMandatoryValuesException("Stock Current Price should exist and it should be a valid number");
		}
		
		
	}
	
	public void validateInputStockId(int stockId) {
		if(stockId <= 0) {
			throw new StockInvalidDataException("Invalid stock ID for this request");
		}
	}

}
