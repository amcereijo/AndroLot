package com.androlot.dto;

import java.io.Serializable;

/**
 * 
 * @author angelcereijo
 *
 */
public class PeticionDto implements Serializable{

	private static final long serialVersionUID = 2075048210509394622L;
	
	private int status = 1;
	private long timestamp;
	private String numero = SorteoDto.PETICION_NUMERO_RESUMEN;

	public PeticionDto(){}


	public void setTimeStamp(long timestamp){
		this.timestamp = timestamp;
	}
	public long getTimeStamp(){
		return this.timestamp;
	}

	public void setNumero(String numero){
		this.numero = numero;
	}
	public String getNumero(){
		return this.numero;
	}

	public int getStatus(){
		return this.status;
	}
}