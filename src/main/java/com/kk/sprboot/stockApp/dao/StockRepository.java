package com.kk.sprboot.stockApp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kk.sprboot.stockApp.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {
	
	public Optional<Stock> findByName(String name);
	
	public Optional<Stock> findByStockCode(String name);

}
