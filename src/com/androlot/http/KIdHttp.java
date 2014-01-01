package com.androlot.http;

import com.androlot.dto.KidResponseResumeDto;
import com.androlot.dto.RespuestaResumenDto;
import com.androlot.dto.SorteoDto;
import com.androlot.exception.RespuestaErrorException;
import com.androlot.json.JsonUtil;

public class KIdHttp extends AndrolotHttp {

	private final static String KID_API_URL = "http://api.elpais.com/ws/LoteriaNinoPremiados";
	
	@Override
	protected String getApiUrl() {
		return KID_API_URL;
	}

	@Override
	public RespuestaResumenDto  resumenPremios() throws RespuestaErrorException, Exception {
		KidResponseResumeDto response;
		String peticion = "n="+SorteoDto.PETICION_NUMERO_RESUMEN;
		//call
		String jsonRespuesta = call(peticion);
		jsonRespuesta = jsonRespuesta.replaceAll("premios=", "");
		//fromJson to base
		JsonUtil<KidResponseResumeDto> jsonUtilResp = new JsonUtil<KidResponseResumeDto>();
		response = (KidResponseResumeDto)jsonUtilResp.fromJsonToObject(jsonRespuesta, KidResponseResumeDto.class);
			//--error?
		if(response.getError() == SorteoDto.RESPUESTA_ERROR){
			//exception
			throw new RespuestaErrorException();
		}
		return response;
	}

}
