package com.androlot.dto;

public class KidResponseResumeDto extends RespuestaResumenDto {

	private static final long serialVersionUID = -264994722088607151L;
	
	private String error; //:	cero, por tanto, todo ha ido correctamente.
	private String premio1; //:	1er. premio del sorteo o -1 si aún no ha salido.
	private String premio2; //:	segundo premio o -1 si aún no ha salido.
	private String numero3; //:	tercer premio o -1 si aún no ha salido.
	private String[] extracciones5cifras = new String[12];//:	array con las 12 extracciones de 5 cifras o -1 si aún no ha salido.
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

}
