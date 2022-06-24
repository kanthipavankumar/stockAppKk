package com.kk.sprboot.stockApp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kk.sprboot.stockApp.entity.Stock;
import com.kk.sprboot.stockApp.exceptionHandler.StockValidator;
import com.kk.sprboot.stockApp.service.StockService;



@RunWith(SpringRunner.class)
@WebMvcTest(value=StockController.class)
@TestMethodOrder(OrderAnnotation.class)
public class StockControllerUnitTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StockService stockService;
	
	@MockBean
	private StockValidator stockValidator;
	
	@Test
	@Order(1)
	public void testAddStock() throws Exception{
		
		Stock mockStock = new Stock("Yext Tech","YEXT",23.45);	
		mockStock.setId(1);
		String inputInJson = this.mapToJson(mockStock);		
		String URI = "/api/stocks";
		
		doNothing().when(stockValidator).validateNewStockRequestData(Mockito.any(Stock.class));
		Mockito.when(stockService.save(Mockito.any(Stock.class), Mockito.anyInt())).thenReturn(mockStock);		
		
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(URI)
				.accept(MediaType.APPLICATION_JSON).content(inputInJson)
				.contentType(MediaType.APPLICATION_JSON);
		
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();		
		String outputInJson = response.getContentAsString();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());	
		assertEquals(inputInJson, outputInJson);	
		
	}
	
	
	private String mapToJson(Object object) throws JsonProcessingException{
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
	
	@Test
	@Order(2)
	public void testGetStock() throws Exception{
		
		Stock mockStock = new Stock("Yext Tech","YEXT",23.45);	
		mockStock.setId(1);
		
		Mockito.when(stockService.findById(Mockito.anyInt())).thenReturn(mockStock);
		
		String URI = "/api/stocks/1";
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(URI).accept(MediaType.APPLICATION_JSON);
		
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();		
		String expectedJson = this.mapToJson(mockStock);
		MockHttpServletResponse response = result.getResponse();		
		String outputInJson = response.getContentAsString();
		
		assertThat(outputInJson).isEqualTo(expectedJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());	
		
	}
	
	@Test
	@Order(3)
	public void testFindAll() throws Exception{
		
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
		
		Mockito.when(stockService.findAll(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString())).thenReturn(pageStock);
		
		String URI = "/api/stocks";	
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(URI).accept(MediaType.APPLICATION_JSON);
		
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();		
		MockHttpServletResponse response = result.getResponse();
		String outputJsonString = response.getContentAsString();
		
		int totalElementsIndex = outputJsonString.indexOf("totalElements",-1);
		int totalElementsColIndex = outputJsonString.indexOf(":",totalElementsIndex+1);
		int totalElementsComIndex = outputJsonString.indexOf(",",totalElementsIndex+1);
		String totalElements = outputJsonString.substring(totalElementsColIndex+1, totalElementsComIndex);
		
		assertEquals(mockStocks.size(),Integer.parseInt(totalElements));		
		assertEquals(HttpStatus.OK.value(), response.getStatus());	
		
		
	}
	
	@Test
	@Order(4)
	public void testUpdateStock() throws Exception{
		
		Stock mockStock = new Stock("Yext Tech","YEXT",23.45);	
		mockStock.setId(1);
		String inputInJson = this.mapToJson(mockStock);		
		String URI = "/api/stocks/1";
		
		doNothing().when(stockValidator).validateInputStockId(Mockito.anyInt());
		doNothing().when(stockValidator).validateUpdateStockRequestData(Mockito.any(Stock.class),Mockito.anyInt());
		Mockito.when(stockService.save(Mockito.any(Stock.class), Mockito.anyInt())).thenReturn(mockStock);				
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.patch(URI)
				.accept(MediaType.APPLICATION_JSON).content(inputInJson)
				.contentType(MediaType.APPLICATION_JSON);
		
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();		
		String outputInJson = response.getContentAsString();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());	
		assertEquals(inputInJson, outputInJson);	
		
	}
	
	@Test
	@Order(5)
	public void testDeleteStock() throws Exception{
		
		String URI = "/api/stocks/1";
		
		doNothing().when(stockValidator).validateInputStockId(Mockito.anyInt());
		doNothing().when(stockService).deleteById(Mockito.anyInt());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete(URI).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();		
		
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());	
		
	}

}
