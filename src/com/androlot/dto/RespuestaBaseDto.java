package com.androlot.dto;

import java.io.Serializable;

/**
 * 
 * @author angelcereijo
 *
 */
public class RespuestaBaseDto implements Serializable{

	private static final long serialVersionUID = -1915224463794515699L;
	
	private int error;
	protected int status;
	protected long timestamp;

	public void setError(int error){
		this.error = error;
	}
	public int getError(){
		return this.error;
	}
}