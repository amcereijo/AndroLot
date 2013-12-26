package com.androlot.game;

import java.util.Calendar;

import android.content.Context;

import com.androlot.manager.AbstractGamePropertiesLoadar;
import com.androlot.manager.ManagerPropertiesLoader;

public class GameTime {

	private AbstractGamePropertiesLoadar cP;
	
	public GameTime(Context c, AbstractGamePropertiesLoadar propertiesLoader){
		cP = new ManagerPropertiesLoader<AbstractGamePropertiesLoadar>().loadProperties(propertiesLoader);
	}
	
	public Calendar getGameDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DECEMBER, Integer.parseInt(cP.getMonth())-1);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(cP.getDay()));
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cP.getHour()));
		calendar.set(Calendar.MINUTE, Integer.parseInt(cP.getMin()));
		return calendar;
	}
	
	public boolean isNotTimeToStart() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DECEMBER)+1 != Integer.parseInt(cP.getMonth()) ||
				c.get(Calendar.DAY_OF_MONTH) != Integer.parseInt(cP.getDay()) ||
				c.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(cP.getHour()) ||
				c.get(Calendar.MINUTE) < Integer.parseInt(cP.getMin());
	}
	 
}
