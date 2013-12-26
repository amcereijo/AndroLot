package com.androlot.controller;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.androlot.applicatioin.GameApplication;
import com.androlot.game.GameTime;
import com.androlot.service.AbstractGameService;

public class ServiceController<T extends AbstractGameService> {
	
	private T serviceClass;
	
	public ServiceController(T serviceClass) {
		this.serviceClass = serviceClass;
	}
	
	
	public void startService(Context c){
		Intent intentService = new Intent(c, serviceClass.getClass());
		GameTime gameTime = serviceClass.getGameTime();
		if(gameTime.isNotTimeToStart()){
			PendingIntent pintent = PendingIntent.getService(c, 0, intentService, 0);
			AlarmManager alarm = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
			
			Calendar calendar = gameTime.getGameDate();

			alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent);
		}else{
			c.startService(intentService);
		}
		
	}

	
	public void stopService(Context c){
		Intent intentService = new Intent(c, serviceClass.getClass());
		cancelAlarm(c, intentService);
		
		if(GameApplication.isServiceRunning(serviceClass.getClass())){
			c.stopService(intentService);
		}
	}


	private void cancelAlarm(Context c, Intent intentService) {
		PendingIntent pintent = PendingIntent.getService(c, 0, intentService, 0);
		AlarmManager alarm = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pintent);
	}
	
}
