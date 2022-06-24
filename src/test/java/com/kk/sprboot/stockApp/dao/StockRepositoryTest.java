package com.kk.sprboot.stockApp.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.kk.sprboot.stockApp.dao.StockRepository;
import com.kk.sprboot.stockApp.entity.Stock;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
public class StockRepositoryTest {
	
	@Autowired
	private StockRepository stockRepository;
	
	@Test
	@Order(1)
	public void testSaveStockCreate() {
		Stock stock = new Stock("Yext Tech","YEXT",23.45);		
		
		Stock savedStock = stockRepository.save(stock);
		assertNotNull(savedStock);	
	}
	
	@Test
	@Order(2)
	public void testFindStockByIdExists() {
		int stockId = 2;
		
		Optional<Stock> stockFromRepo = stockRepository.findById(stockId);

		assertTrue(stockFromRepo.isPresent());
		
	}
	
	@Test
	@Order(3)
	public void testFindStockByIdNotExists() {
		int stockId = 10000;
		
		Optional<Stock> stockFromRepo = stockRepository.findById(stockId);

		assertFalse(stockFromRepo.isPresent());
		
	}
	
	@Test
	@Order(4)
	public void testFindStockByNameExist() {
		String stockName = "Amazon";
		
		Optional<Stock> stockFromRepo = stockRepository.findByName(stockName);
		
		assertTrue(stockFromRepo.isPresent());
	}
	
	@Test
	@Order(5)
	public void testSaveStockUpdate() {
		int stockId = 3;

		Optional<Stock> stockFromRepoBeforeUpdate = stockRepository.findById(stockId);
		String stockCodeBeforeUpdate = stockFromRepoBeforeUpdate.get().getStockCode();
		Stock stock = new Stock("ApplePlus","AAPLPLUS",23.25);		
		stock.setId(stockId);
		
		stockRepository.save(stock);
		Optional<Stock> stockFromRepoAfterUpdate = stockRepository.findById(stockId);
		String stockCodeAfterUpdate = stockFromRepoAfterUpdate.get().getStockCode();
		
		assertNotEquals(stockCodeBeforeUpdate, stockCodeAfterUpdate);	
	}
	
	@Test
	@Order(6)
	public void testFindAllStocks() {
		Pageable paging = PageRequest.of(0, 5, Sort.by("stockCode"));
		Page<Stock> stocks = stockRepository.findAll(paging);
		List<Stock> stocksList = stocks.getContent();
		
		assertThat(stocksList).size().isEqualTo(5);
	}
	
	@Test
	@Order(7)
	public void testDeleteStock() {
		int stockId = 3;
		
		boolean existsBeforeDel = stockRepository.findById(stockId).isPresent();
		stockRepository.deleteById(stockId);
		boolean notExistsAfterDel = stockRepository.findById(stockId).isPresent();
		
		assertTrue(existsBeforeDel);
		assertFalse(notExistsAfterDel);
	}
	
	@Test
	@Order(8)
	public void testFindStockByCodeExist() {
		String stockCode = "AMZN";
		
		Optional<Stock> stockFromRepo = stockRepository.findByStockCode(stockCode);
		
		assertTrue(stockFromRepo.isPresent());
	}


}
