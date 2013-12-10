package com.androlot.http;

import java.net.URL;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.androlot.dto.PeticionDto;
import com.androlot.dto.RespuestaNumeroDto;
import com.androlot.dto.RespuestaResumenDto;
import com.androlot.dto.SorteoDto;
import com.androlot.exception.RespuestaErrorException;
import com.androlot.json.JsonUtil;

/**
 * 
 * @author angelcereijo
 *
 */
public class AndrolotHttp {
	
	private final static String API_URL = "http://api.elpais.com/ws/LoteriaNavidadPremiados";

	/**
	 * 
	 * @param peticionDto
	 * @return
	 * @throws RespuestaErrorException
	 * @throws Exception
	 */
	public RespuestaNumeroDto premioNumero(PeticionDto peticionDto) throws
		RespuestaErrorException, Exception{
		RespuestaNumeroDto respuestaNumeroDto;
		String peticion = "n="+peticionDto.getNumero();
		//call
		String jsonRespuesta = call(peticion);
		jsonRespuesta = jsonRespuesta.replaceAll("busqueda=", "");
		//fromJson to base
		JsonUtil<RespuestaNumeroDto> jsonUtilResp = new JsonUtil<RespuestaNumeroDto>();
		respuestaNumeroDto = (RespuestaNumeroDto)jsonUtilResp.fromJsonToObject(jsonRespuesta, RespuestaNumeroDto.class);
			//--error?
		if(respuestaNumeroDto.getError() == SorteoDto.RESPUESTA_ERROR){
			//exception
			Log.e("", "Respesta error:"+jsonRespuesta);
			throw new RespuestaErrorException();
		}
		return respuestaNumeroDto;
	}
	
	/**
	 * 
	 * @param peticionDto
	 * @return
	 * @throws RespuestaErrorException
	 * @throws Exception
	 */
	public RespuestaResumenDto resumenPremios() throws
		RespuestaErrorException, Exception{
		RespuestaResumenDto respuestaResumenDto;
		String peticion = "n="+SorteoDto.PETICION_NUMERO_RESUMEN;
		//call
		String jsonRespuesta = call(peticion);
		jsonRespuesta = jsonRespuesta.replaceAll("premios=", "");
		//fromJson to base
		JsonUtil<RespuestaResumenDto> jsonUtilResp = new JsonUtil<RespuestaResumenDto>();
		respuestaResumenDto = (RespuestaResumenDto)jsonUtilResp.fromJsonToObject(jsonRespuesta, RespuestaResumenDto.class);
			//--error?
		if(respuestaResumenDto.getError() == SorteoDto.RESPUESTA_ERROR){
			//exception
			throw new RespuestaErrorException();
		}
		return respuestaResumenDto;
	}


	private String call(String peticion) throws Exception{
		String jsonRespuesta = "";
		URL url = new URL(API_URL+"?"+peticion);
    	DefaultHttpClient client = new DefaultHttpClient();
    	HttpGet get = new HttpGet(url.toURI());
    	ResponseHandler<String> responseHandler = new BasicResponseHandler();
        jsonRespuesta = client.execute(get,responseHandler);
    	return jsonRespuesta;
	}
}