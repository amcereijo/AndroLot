package com.androlot.application;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

import com.androlot.enums.GameTypeEnum;

public class GameApplication extends Application {

	private static GameTypeEnum gameType;
	private static GameApplication gameApplication;
	private static Context context;
	
	public static GameTypeEnum getGameType() {
		return gameType;
	}
	public static void setGameType(GameTypeEnum gameType) {
		GameApplication.gameType = gameType;
	}
	
	public static boolean isServiceRunning(Class<?> classType){
		ActivityManager manager = (ActivityManager) GameApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (classType.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public static GameApplication getInstance(){
		if(gameApplication == null){
			gameApplication = new GameApplication();
		}
		return gameApplication;
	}
	
	public static void setContext(Context context) {
		GameApplication.context = context;
	}
	public static Context getContext() {
		return context;
	}
}
