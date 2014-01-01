package com.androlot.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.res.AssetManager;
import android.content.res.Resources;

import com.androlot.application.GameApplication;

public class KidProperties extends AbstractGamePropertiesLoadar {

	private final static String FILE_NAME = "kid.conf.properties";
	
	public void doLoad() {
		Resources resources = GameApplication.getContext().getResources();
		AssetManager assetManager = resources.getAssets();

		try {
			InputStream inputStream = assetManager.open(FILE_NAME);
			Properties properties = new Properties();
		    properties.load(inputStream);
		    
		    setDay(properties.getProperty("day"));
			setMonth(properties.getProperty("month"));
			setHour(properties.getProperty("hour"));
			setMin(properties.getProperty("min"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
