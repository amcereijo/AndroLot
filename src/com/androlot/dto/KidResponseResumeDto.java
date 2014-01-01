package com.androlot.dto;

public class KidResponseResumeDto extends RespuestaResumenDto {

	private static final long serialVersionUID = -264994722088607151L;
	
	private String premio1; //:	1er. premio del sorteo o -1 si aún no ha salido.
	private String premio2; //:	segundo premio o -1 si aún no ha salido.
	private String numero3; //:	tercer premio o -1 si aún no ha salido.
	private String[] extracciones5cifras = new String[12];//:	terceros array con las 12 extracciones de 5 cifras o -1 si aún no ha salido.
	
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
	public String getNumero3() {
		return numero3;
	}
	public void setNumero3(String numero3) {
		this.numero3 = numero3;
	}
	public String[] getExtracciones5cifras() {
		return extracciones5cifras;
	}
	public void setExtracciones5cifras(String[] extracciones5cifras) {
		this.extracciones5cifras = extracciones5cifras;
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
