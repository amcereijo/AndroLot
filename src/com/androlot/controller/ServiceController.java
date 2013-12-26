package com.androlot.controller;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.androlot.applicatioin.GameApplication;
import com.androlot.game.GameTime;

public class ServiceController<T extends Service> {
	
	private Class<T> serviceClass;
	
	public ServiceController(Class<T> serviceClass) {
		this.serviceClass = serviceClass;
	}
	
	
	public void startService(Context c){
		Intent intentService = new Intent(c, serviceClass);
		GameTime gameTime = new GameTime(c);
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
		Intent intentService = new Intent(c, serviceClass);
		cancelAlarm(c, intentService);
		
		if(GameApplication.isServiceRunning(c, serviceClass)){
			c.stopService(intentService);
		}
	}


	private void cancelAlarm(Context c, Intent intentService) {
		PendingIntent pintent = PendingIntent.getService(c, 0, intentService, 0);
		AlarmManager alarm = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pintent);
	}
	
}
