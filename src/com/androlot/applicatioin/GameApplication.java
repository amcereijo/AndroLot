package com.androlot.applicatioin;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;

import com.androlot.enums.GameTypeEnum;

public class GameApplication extends Application {

	private static GameTypeEnum gameType;
	
	public static GameTypeEnum getGameType() {
		return gameType;
	}
	public static void setGameType(GameTypeEnum gameType) {
		GameApplication.gameType = gameType;
	}
	
	
	public static boolean isServiceRunning(Context context,  Class classType){
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (classType.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
}
