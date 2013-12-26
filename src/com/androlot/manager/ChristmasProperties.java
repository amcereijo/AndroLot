package com.androlot.manager;

public class ChristmasProperties extends AbstractGamePropertiesLoadar{

	private final static String FILE_NAME = "christmas.conf.properties";
	
	@Override
	public String getFilePropertiesName() {
		return ChristmasProperties.FILE_NAME;
	}
	
}
