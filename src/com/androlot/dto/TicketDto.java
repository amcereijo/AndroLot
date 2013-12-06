package com.androlot.dto;

import java.io.Serializable;

public class TicketDto implements Serializable {

	private static final long serialVersionUID = 1403034203392709014L;
	
	private int number;
	private float ammount;
	private float price;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public float getAmmount() {
		return ammount;
	}
	public void setAmmount(float ammount) {
		this.ammount = ammount;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
}
