package com.androlot.manager;

public class ChristmasProperties implements PropertiesLoader{

	private final static String FILE_NAME = "christmas.conf.properties";
	
	private String day;
	private String month;
	private String hour;
	private String min;
	
	@Override
	public String getFilePropertiesName() {
		return ChristmasProperties.FILE_NAME;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}
	
	
	
}
