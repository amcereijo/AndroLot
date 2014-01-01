package com.androlot.http;

import com.androlot.dto.ChristmasResponseResumeDto;
import com.androlot.dto.RespuestaResumenDto;
import com.androlot.dto.SorteoDto;
import com.androlot.exception.RespuestaErrorException;
import com.androlot.json.JsonUtil;

public class ChristmasHttp extends AndrolotHttp {

	private final static String API_URL = "http://api.elpais.com/ws/LoteriaNavidadPremiados";
	
	@Override
	protected String getApiUrl() {
		return API_URL;
	}


	@Override
	public RespuestaResumenDto resumenPremios() throws RespuestaErrorException, Exception {
		ChristmasResponseResumeDto response;
		String peticion = "n="+SorteoDto.PETICION_NUMERO_RESUMEN;
		//call
		String jsonRespuesta = call(peticion);
		jsonRespuesta = jsonRespuesta.replaceAll("premios=", "");
		//fromJson to base
		JsonUtil<ChristmasResponseResumeDto> jsonUtilResp = new JsonUtil<ChristmasResponseResumeDto>();
		response = (ChristmasResponseResumeDto)jsonUtilResp.fromJsonToObject(jsonRespuesta, ChristmasResponseResumeDto.class);
			//--error?
		if(response.getError() == SorteoDto.RESPUESTA_ERROR){
			//exception
			throw new RespuestaErrorException();
		}
		return response;
	}
	
}
