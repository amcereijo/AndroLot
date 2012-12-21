package com.androlot.json;

import com.google.gson.Gson;

/**
 * 
 * @author angelcereijo
 *
 * @param <T>
 */
public class JsonUtil<T extends Object>{

	private Gson gson = new Gson();

	public T fromJsonToObject(String json, Class<T> clas) throws Exception{
		return gson.fromJson(json, clas);
	}

	public String fromObjectoToJson(T object) throws Exception{
		return gson.toJson(object);
	}

}