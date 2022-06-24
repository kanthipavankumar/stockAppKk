package com.kk.sprboot.stockApp.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kk.sprboot.stockApp.controller.StockController;
import com.kk.sprboot.stockApp.dao.StockRepository;
import com.kk.sprboot.stockApp.entity.Stock;
import com.kk.sprboot.stockApp.exceptionHandler.StockDuplicateException;
import com.kk.sprboot.stockApp.exceptionHandler.StockNotFoundException;

@Service
public class StockServiceImpl implements StockService {
	
	Logger logger = LoggerFactory.getLogger(StockController.class);
	
	@Autowired
	private StockRepository stockRepository;
	
	@Override
	public Page<Stock> findAll(int pageNo, int pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return stockRepository.findAll(paging);
	}

	@Override
	public Stock findById(int stockId) {
		Optional<Stock> result = stockRepository.findById(stockId);
		Stock stock=null;
		if(result.isPresent()){
			stock = result.get();
		}
		else {
			throw new StockNotFoundException("Could not find stock id:"+stockId);
		}
		return stock;
	}

	@Override
	public Stock save(Stock stock, int stockId) {
		//while creating stock
		if(stockId == 0) {
			//checking duplicate stock name
			Optional<Stock> result = stockRepository.findByName(stock.getName());
			if(result.isPresent()){
				throw new StockDuplicateException("Duplicate stock name: "+stock.getName());
			}
			//checking duplicate stock code
			result = stockRepository.findByStockCode(stock.getStockCode());
			if(result.isPresent()){
				throw new StockDuplicateException("Duplicate stock code: "+stock.getStockCode());
			}		
			
			stockRepository.save(stock);
			return stock;
		}
		//Updating stock
		else {
			//checking whether stock id is valid or not
			Optional<Stock> resultExists = stockRepository.findById(stockId);
			if(!resultExists.isPresent()){
				throw new StockNotFoundException("Could not find stock id:"+stockId);
			}
			
			//checking whether new stock name is duplicate
			Optional<Stock> result = stockRepository.findByName(stock.getName());
			if(result.isPresent() && result.get().getId() != stockId){
				throw new StockDuplicateException("Duplicate stock name: "+stock.getName());
			}
			//checking whether new stock code is duplicate
			result = stockRepository.findByStockCode(stock.getStockCode());
			if(result.isPresent() && result.get().getId() != stockId){
				throw new StockDuplicateException("Duplicate stock code: "+stock.getStockCode());
			}
			Stock updateStock = resultExists.get();
			updateStock.setName(stock.getName());
			updateStock.setStockCode(stock.getStockCode());
			updateStock.setCurrentPrice(stock.getCurrentPrice());
			stockRepository.save(updateStock);
			return updateStock;
		}
		
		
	}

	@Override
	public void deleteById(int stockId) {
		Optional<Stock> result = stockRepository.findById(stockId);
		if(!result.isPresent()){
			throw new StockNotFoundException("Could not find stock id:"+stockId);
		}
		stockRepository.deleteById(stockId);
	}

}
