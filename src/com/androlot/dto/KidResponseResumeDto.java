package com.androlot.dto;

public class KidResponseResumeDto extends RespuestaResumenDto {

	private static final long serialVersionUID = -264994722088607151L;
	
	private String premio1; //:	1er. premio del sorteo o -1 si aún no ha salido.
	private String premio1Fraccion; // fracción del premio especial o -1 si no ha salido
	private String premio1Serie; // serie del premio especial o -1 si no ha salido
	private String premio2; //:	segundo premio o -1 si aún no ha salido.
	private String premio3; //:	tercer premio o -1 si aún no ha salido.
	
	private String[] extracciones3cifras = new String[14]; //	array con las 14 extracciones de 3 cifras o -1 si aún no ha salido.
	private String[] extracciones2cifras = new String[5]; //:	array con las 5 extracciones de 2 cifras o -1 si aún no ha salido.
	private String[] reintegros = new String[3]; //:	array con los 3 reintegros (2 extracciones especiales + última cifra del 1er. premio) o -1 si aún no ha salido.
	
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
	
	public String getPremio1() {
		return premio1;
	}
	public void setPremio1(String premio1) {
		this.premio1 = premio1;
	}
	public String getPremio2() {
		return premio2;
	}
	public void setPremio2(String premio2) {
		this.premio2 = premio2;
	}
	public String getPremio3() {
		return premio3;
	}
	public void setPremio3(String premio3) {
		this.premio3 = premio3;
	}
	public void setPremio1Fraccion(String premio1Fraccion) {
		this.premio1Fraccion = premio1Fraccion;
	}
	public String getPremio1Fraccion() {
		return premio1Fraccion;
	}
	public void setPremio1Serie(String premio1Serie) {
		this.premio1Serie = premio1Serie;
	}
	public String getPremio1Serie() {
		return premio1Serie;
	}
	public String[] getExtracciones3cifras() {
		return extracciones3cifras;
	}
	public void setExtracciones3cifras(String[] extracciones3cifras) {
		this.extracciones3cifras = extracciones3cifras;
	}
	public String[] getExtracciones2cifras() {
		return extracciones2cifras;
	}
	public void setExtracciones2cifras(String[] extracciones2cifras) {
		this.extracciones2cifras = extracciones2cifras;
	}
	public String[] getReintegros() {
		return reintegros;
	}
	public void setReintegros(String[] reintegros) {
		this.reintegros = reintegros;
	}
	
}
