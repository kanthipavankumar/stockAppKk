package com.kk.sprboot.stockApp.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


@Entity
public class Stock {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "stockSequenceGenerator")
	@SequenceGenerator(name="stockSequenceGenerator",sequenceName="STOCK_SEQ",initialValue = 1,allocationSize = 1)
	@JsonProperty(access = Access.READ_ONLY)
	private int id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String stockCode;
	
	@Max(1000000)
	private double currentPrice;
	
	@UpdateTimestamp
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonIgnore
	private Timestamp lastUpdate;
	
	@CreationTimestamp
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonIgnore
	private Timestamp creationDate;
	
	
	
	public Stock() {
		super();
	}

	public Stock(@NotEmpty String name, @NotEmpty String stockCode, @Max(1000000) double currentPrice) {
		this.name = name;
		this.stockCode = stockCode;
		this.currentPrice = currentPrice;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the currentPrice
	 */
	public double getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * @param currentPrice the currentPrice to set
	 */
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	/**
	 * @return the lastUpdate
	 */
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	

	/**
	 * @return the stockCode
	 */
	public String getStockCode() {
		return stockCode;
	}

	/**
	 * @param stockCode the stockCode to set
	 */
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	
	

	/**
	 * @return the creationDate
	 */
	public Timestamp getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "Stock [id=" + id + ", name=" + name + ", stockCode=" + stockCode + ", currentPrice=" + currentPrice
				+ ", lastUpdate=" + lastUpdate + ", creationDate=" + creationDate + "]";
	}

	
	
	
}
