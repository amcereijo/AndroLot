package com.androlot.manager;


public class ManagerPropertiesLoader <T extends PropertiesLoader>{

	public T loadProperties(T t){
		t.doLoad();
		return t;
	}
}
