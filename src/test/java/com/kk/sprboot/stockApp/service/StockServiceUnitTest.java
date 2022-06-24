package com.kk.sprboot.stockApp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.kk.sprboot.stockApp.dao.StockRepository;
import com.kk.sprboot.stockApp.entity.Stock;
import com.kk.sprboot.stockApp.service.StockService;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class StockServiceUnitTest {
	
	@MockBean
	private StockRepository stockRepository;
		
	@Autowired
	private StockService stockService;
		
	@Test
	@Order(1)
    public void testSaveStockCreate() {
		Stock mockStock = new Stock("Yext Tech","YEXT",23.45);	
		mockStock.setId(1);

		Mockito.when(stockRepository.save(Mockito.any(Stock.class))).thenReturn(mockStock);

        Stock savedStock = stockService.save(mockStock,0);
        assertNotNull(savedStock);
    }
	
	@Test
	@Order(2)
    public void testGetStock() {
		Stock stock = new Stock("Yext Tech","YEXT",23.45);
		stock.setId(1);
		Optional<Stock> mockStock = Optional.of(stock);

        Mockito.when(stockRepository.findById(Mockito.anyInt())).thenReturn(mockStock);
        
        Stock stockFromService =  stockService.findById(1);
        assertEquals(stockFromService, mockStock.get());
        
    }
	
	@Test
	@Order(3)
    public void testfindAllStocks() {
		Stock mockStock1 = new Stock("Yext Tech","YEXT",23.45);	
		mockStock1.setId(1);
		Stock mockStock2 = new Stock("Xylan","XYL",11.45);	
		mockStock2.setId(2);
		Stock mockStock3 = new Stock("Moderna","MRNA",22.25);	
		mockStock3.setId(3);
		
		List<Stock> mockStocks = new ArrayList<>();
		mockStocks.add(mockStock1);
		mockStocks.add(mockStock2);
		mockStocks.add(mockStock3);
		
		PageRequest pageRequest = PageRequest.of(0, 10,Sort.by("id"));
		Page<Stock> pageStock = new PageImpl<Stock>(mockStocks,pageRequest,mockStocks.size());
		
        Mockito.when(stockRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pageStock);
        
        Page<Stock> allStocksFromService =  stockService.findAll(0,10,"id");
        assertEquals(mockStocks.size(), allStocksFromService.getTotalElements());
        
    }
	
	@Test
	@Order(4)
    public void testDeleteStock() {
		Stock stock = new Stock("Yext Tech","YEXT",23.45);
		stock.setId(1);
		Optional<Stock> mockStock = Optional.of(stock);

        Mockito.when(stockRepository.findById(Mockito.anyInt())).thenReturn(mockStock);        
        doNothing().when(stockRepository).deleteById(Mockito.anyInt());
        Mockito.when(stockRepository.existsById(Mockito.anyInt())).thenReturn(false);
        
        stockService.deleteById(1);
        assertFalse(stockRepository.existsById(stock.getId()));
        
    }
	
	@Test
	@Order(5)
    public void testUpdateStock() {
		Stock stock = new Stock("Yext Tech","YEXT",23.45);
		stock.setId(1);
		Optional<Stock> mockStock = Optional.of(stock);

        Mockito.when(stockRepository.findById(Mockito.anyInt())).thenReturn(mockStock);        
        mockStock.get().setStockCode("YEXTT");
        
        Mockito.when(stockRepository.save(Mockito.any(Stock.class))).thenReturn(mockStock.get());
        
        assertEquals(stockService.save(stock, 1),stock);
        
    }

}
