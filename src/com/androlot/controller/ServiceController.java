package com.androlot.controller;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.androlot.applicatioin.GameApplication;
import com.androlot.game.GameTime;
import com.androlot.service.AndroLotService;

public class ServiceController {
	
	private Intent intentService;
	private PendingIntent pintent;
	private AlarmManager alarm;
	
	
	public ServiceController() { }
	
	
	public void startService(Context c){
		intentService = new Intent(c, AndroLotService.class);
		
		
		GameTime gameTime = new GameTime(c);
		if(gameTime.isNotTimeToStart()){
			
			pintent = PendingIntent.getService(c, 0, intentService, 0);
			alarm = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
			
			Calendar calendar = gameTime.getGameDate();

			alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent);
		}else{
			c.startService(intentService);
		}
	}

	
	public void stopService(Context c){
		if(alarm !=null && pintent !=null){
			alarm.cancel(pintent);
		}
		if(intentService!=null){
			c.stopService(intentService);
			intentService = null;
		}else if(GameApplication.isServiceRunning(c, AndroLotService.class)){
			intentService = new Intent(c, AndroLotService.class);
        	c.stopService(intentService);
        	intentService = null;
		}
	}
	
}
