package com.androlot.manager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import android.content.res.AssetManager;
import android.content.res.Resources;

import com.androlot.applicatioin.GameApplication;


public class ManagerPropertiesLoader <T extends PropertiesLoader>{

	public T loadProperties(T t){
		Resources resources = GameApplication.getInstance().getResources();
		AssetManager assetManager = resources.getAssets();

		try {
		    InputStream inputStream = assetManager.open(t.getFilePropertiesName());
		    Properties properties = new Properties();
		    properties.load(inputStream);
		    
		    Field[] fields = t.getClass().getDeclaredFields();
		    for(Field f :fields){
		    	f.setAccessible(Boolean.TRUE);
		    	String name = f.getName();
		    	Object value = properties.get(name);
		    	if(value!=null){
		    		f.set(t, value);
		    	}
		    }
		    
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
		return t;
	}
}
