package com.androlot.manager;

public class KidProperties extends ChristmasProperties {

	private final static String FILE_NAME = "kid.conf.properties";
	
	@Override
	public String getFilePropertiesName() {
		return KidProperties.FILE_NAME;
	}
}
