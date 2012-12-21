package com.androlot.dto;


/**
 * 
 * @author angelcereijo
 *
 */
public class RespuestaNumeroDto extends RespuestaBaseDto 
	implements RespuestaConTimestamp,RespuestaConStatus{

	private static final long serialVersionUID = 1010099669168006534L;
	
	private String numero;
	private String premio;
	

	public void setNumero(String numero){
		this.numero = numero;
	}
	public String getNumero(){
		return this.numero;
	}

	public void setPremio(String premio){
		this.premio = premio;
	}
	public String getPremio(){
		return this.premio;
	}	

	public void setTimestamp(long timestamp){
		this.timestamp = timestamp;
	}
	public long getTimestamp(){
		return this.timestamp;
	}

	public int getStatus(){
		return this.status;
	}
	public void setStatus(int status){
		this.status = status;
	}
}