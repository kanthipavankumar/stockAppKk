package com.kk.sprboot.stockApp.service;

import org.springframework.data.domain.Page;

import com.kk.sprboot.stockApp.entity.Stock;

public interface StockService {
	
	public Page<Stock> findAll(int pageNo, int pageSize, String sortBy);
	
	public Stock findById(int stockId);
	
	public Stock save(Stock stock, int stockId);
	
	public void deleteById(int stockId);
	

}
