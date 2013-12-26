package com.androlot.applicatioin;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;

import com.androlot.enums.GameTypeEnum;

public class GameApplication extends Application {

	private static GameTypeEnum gameType;
	private static GameApplication gameApplication;
	
	public static GameTypeEnum getGameType() {
		return gameType;
	}
	public static void setGameType(GameTypeEnum gameType) {
		GameApplication.gameType = gameType;
	}
	
	public static boolean isServiceRunning(Class<?> classType){
		ActivityManager manager = (ActivityManager) getInstance().getSystemService(Context.ACTIVITY_SERVICE);
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
}
