package com.kk.sprboot.stockApp.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;

import com.kk.sprboot.stockApp.entity.Stock;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class StockControllerIntegTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Before
	public void setup() {
		testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	}

	@LocalServerPort
	private int port;

	HttpHeaders headers = new HttpHeaders();
	
	@Test
	@Order(1)
	public void testFindAllStocks() {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/stocks",HttpMethod.GET, entity, String.class);
		
		String outputJsonString = response.getBody();
		
		int totalElementsIndex = outputJsonString.indexOf("totalElements",-1);
		int totalElementsColIndex = outputJsonString.indexOf(":",totalElementsIndex+1);
		int totalElementsComIndex = outputJsonString.indexOf(",",totalElementsIndex+1);
		String totalElements = outputJsonString.substring(totalElementsColIndex+1, totalElementsComIndex);
		
		assertEquals(31, Integer.parseInt(totalElements));
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	@Order(2)
	public void testGetStock() {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/stocks/5",HttpMethod.GET, entity, String.class);
		
		JSONObject outputJson;
		int outputId = 0;
		try {
			outputJson = new JSONObject(response.getBody());
			outputId = (int)(outputJson.get("id"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		assertEquals(5, outputId);
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}
	
	@Test
	@Order(3)
	public void testAddStock() {
		Stock stock = new Stock("Yext Tech","YEXT",23.45);
		
		HttpEntity<Stock> entity = new HttpEntity<Stock>(stock, headers);

		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/stocks",HttpMethod.POST, entity, String.class);
		String outputLocation = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		
		assertTrue(outputLocation.contains("/api/stocks"));
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

	}
	
	
	
	@Test
	@Order(4)
	public void testUpdateStock() {
		Stock stock = new Stock("Yext Techno","YEXT1",23.45);	
		//testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		
		HttpEntity<Stock> entity = new HttpEntity<Stock>(stock, headers);

		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/stocks/3",HttpMethod.PATCH, entity, String.class);
		String outputLocation = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		
		assertTrue(outputLocation.contains("/api/stocks/3"));
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}
	
	@Test
	@Order(6)
	public void testUpdateStockDuplicateStock() {
		Stock stock = new Stock("Google","YEXT",23.45);	
		//testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		
		HttpEntity<Stock> entity = new HttpEntity<Stock>(stock, headers);

		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/stocks/3",HttpMethod.PATCH, entity, String.class);
		
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

	}
	
	@Test
	@Order(5)
	public void testDeleteStock() {

		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/stocks/31",HttpMethod.DELETE, null, String.class);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

	}
	
	@Test
	@Order(7)
	public void testAddStockInvalidData() {
		Stock stock = new Stock("Yext Tech2","",23.45);
		
		HttpEntity<Stock> entity = new HttpEntity<Stock>(stock, headers);

		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/stocks",HttpMethod.POST, entity, String.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

	}
	
	@Test
	@Order(8)
	public void testAddStockInvalidStockId() {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/api/stocks/40",HttpMethod.GET, entity, String.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}
}
