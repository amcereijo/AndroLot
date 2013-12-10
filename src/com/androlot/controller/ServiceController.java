package com.androlot.controller;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.androlot.manager.ChristmasProperties;
import com.androlot.manager.ManagerPropertiesLoader;
import com.androlot.service.AndroLotService;

public class ServiceController {
	
	private Intent intentService;
	private PendingIntent pintent;
	private AlarmManager alarm;
	
	
	public ServiceController() { }
	
	
	public void startService(Context c){
		intentService = new Intent(c, AndroLotService.class);
		ChristmasProperties cP = new ManagerPropertiesLoader<ChristmasProperties>().loadProperties(c, new ChristmasProperties());
		
		Calendar calendar = Calendar.getInstance();
		if(isTimeToStart(calendar, cP)){
			
			pintent = PendingIntent.getService(c, 0, intentService, 0);
			alarm = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
			
			setGameDate(cP, calendar);

			alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent);
		}else{
			c.startService(intentService);
		}
	}


	protected void setGameDate(ChristmasProperties cP, Calendar calendar) {
		calendar.set(Calendar.DECEMBER, Integer.parseInt(cP.getMonth())-1);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(cP.getDay()));
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cP.getHour()));
		calendar.set(Calendar.MINUTE, Integer.parseInt(cP.getMin()));
	}

	
	public void stopService(Context c){
		if(alarm !=null && pintent !=null){
			alarm.cancel(pintent);
		}
		if(intentService!=null){
			c.stopService(intentService);
			intentService = null;
		}
	}
	
	
	protected boolean isTimeToStart(Calendar c, ChristmasProperties cP) {
		return c.get(Calendar.DECEMBER)+1 != Integer.parseInt(cP.getMonth()) ||
				c.get(Calendar.DAY_OF_MONTH) != Integer.parseInt(cP.getDay()) ||
				c.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(cP.getHour()) ||
				c.get(Calendar.MINUTE) < Integer.parseInt(cP.getMin());
	}
}
