package com.kk.sprboot.stockApp.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kk.sprboot.stockApp.entity.Stock;
import com.kk.sprboot.stockApp.exceptionHandler.StockValidator;
import com.kk.sprboot.stockApp.service.StockService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
public class StockController {
	
	Logger logger = LoggerFactory.getLogger(StockController.class);

	@Autowired
	private StockService stockService;
	
	@Autowired
	private StockValidator stockValidator;
	
	@Operation(summary = "Get All Stocks with Pagination and Sorting features")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Found Stocks", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = Page.class)) })})
	@GetMapping("/stocks")
	public Page<Stock> findAll(@Parameter(description = "page number to search, starts from 0") @RequestParam(defaultValue = "0") int pageNo,
							   @Parameter(description = "page size - number of stocks per page") @RequestParam(defaultValue = "10") int pageSize,
							   @Parameter(description = "sort by - stockCode or name or currentPrice") @RequestParam(defaultValue = "id") String sortBy ){
		
		logger.info("Finding All Stocks: pageNo="+pageNo+",pageSize="+pageSize+",sortBy"+sortBy);
		
		return stockService.findAll(pageNo, pageSize, sortBy);
	}
	
	@Operation(summary = "Get Stock by its ID")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Found the Stock", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = Stock.class)) }),
			  @ApiResponse(responseCode = "400", description = "Invalid ID supplied", 
			    content = @Content), 
			  @ApiResponse(responseCode = "404", description = "Stock not found", 
			    content = @Content) })
	@GetMapping("/stocks/{stockId}")
	public ResponseEntity<Stock> getStock(@PathVariable int stockId) {
		
		logger.info("In getStock method, stockId:"+stockId);
		
		stockValidator.validateInputStockId(stockId);
		
		Stock retrievedStock = stockService.findById(stockId);
		
		return new ResponseEntity<Stock>(retrievedStock,HttpStatus.OK);
	}
	
	@Operation(summary = "Create New Stock")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "201", description = "Created the Stock", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = Stock.class)) }),
			  @ApiResponse(responseCode = "400", description = "Invalid Data, please refer request body example", 
			    content = @Content), 
			  @ApiResponse(responseCode = "409", description = "Duplicate Stock Code/Name", 
			    content = @Content)})
	@ResponseStatus(code=HttpStatus.CREATED)
	@PostMapping("/stocks")
	public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
		
		logger.info("In addStock method");
		
		stockValidator.validateNewStockRequestData(stock);
		stock.setId(0);		
		Stock newStock =  stockService.save(stock,0);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newStock.getId()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);
		
		return new ResponseEntity<Stock>(newStock,headers,HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update Stock")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Updated the Stock", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = Stock.class)) }),
			  @ApiResponse(responseCode = "400", description = "Invalid ID supplied", 
			    content = @Content), 
			  @ApiResponse(responseCode = "404", description = "Stock not found", 
			    content = @Content),
			  @ApiResponse(responseCode = "409", description = "Duplicate Stock Code/Name", 
			    content = @Content)})
	@PatchMapping("/stocks/{stockId}")
	public ResponseEntity<Stock> updateStock(@RequestBody Stock stock,  @PathVariable int stockId) {
		
		logger.info("In updateStock method, stockId:"+stockId);
		
		stockValidator.validateInputStockId(stockId);		
		stockValidator.validateUpdateStockRequestData(stock, stockId);
		
		Stock newStock =  stockService.save(stock,stockId);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(newStock.getId()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);
		
		return new ResponseEntity<Stock>(newStock,headers,HttpStatus.OK);
	}
	
	@Operation(summary = "Delete Stock by its ID")
	@ApiResponses(value = { 
			 @ApiResponse(responseCode = "204", description = "Deleted the Stock", 
				    content = { @Content(mediaType = "application/json", 
				      schema = @Schema(implementation = String.class)) }),
			  @ApiResponse(responseCode = "400", description = "Invalid ID supplied", 
			    content = @Content), 
			  @ApiResponse(responseCode = "404", description = "Stock not found", 
			    content = @Content)})
	@DeleteMapping("/stocks/{stockId}")
	public ResponseEntity<Void> deleteStock(@PathVariable int stockId) {
		
		logger.info("In deleteStock method, stockId:"+stockId);
		
		stockValidator.validateInputStockId(stockId);		
		stockService.deleteById(stockId);
		
		logger.info("Delete Stock is successful");
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
}
