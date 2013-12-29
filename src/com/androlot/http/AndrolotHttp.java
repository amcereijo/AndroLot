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
public abstract class AndrolotHttp {
	
	/**
	 * 
	 * @return
	 */
	protected abstract String getApiUrl();
	
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
	public abstract RespuestaResumenDto resumenPremios() throws
		RespuestaErrorException, Exception; 
	

	protected String call(String peticion) throws Exception{
		String jsonRespuesta = "";
		URL url = new URL(getApiUrl()+"?"+peticion);
    	DefaultHttpClient client = new DefaultHttpClient();
    	HttpGet get = new HttpGet(url.toURI());
    	ResponseHandler<String> responseHandler = new BasicResponseHandler();
        jsonRespuesta = client.execute(get,responseHandler);
    	return jsonRespuesta;
	}
}