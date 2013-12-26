package com.androlot.manager;

public class KidProperties extends AbstractGamePropertiesLoadar {

	private final static String FILE_NAME = "kid.conf.properties";
	
	@Override
	public String getFilePropertiesName() {
		return KidProperties.FILE_NAME;
	}
}
