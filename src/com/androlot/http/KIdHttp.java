package com.androlot.http;

import com.androlot.dto.RespuestaResumenDto;
import com.androlot.exception.RespuestaErrorException;

public class KIdHttp extends AndrolotHttp {

	private final static String KID_API_URL = "http://api.elpais.com/ws/LoteriaNinoPremiados";
	
	@Override
	protected String getApiUrl() {
		return KID_API_URL;
	}

	@Override
	public <T extends RespuestaResumenDto> T resumenPremios(Class<T> c)
			throws RespuestaErrorException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
