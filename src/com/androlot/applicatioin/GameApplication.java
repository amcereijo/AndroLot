package com.androlot.applicatioin;

import android.app.Application;

import com.androlot.enums.GameTypeEnum;

public class GameApplication extends Application {

	private static GameTypeEnum gameType;
	
	public static GameTypeEnum getGameType() {
		return gameType;
	}
	public static void setGameType(GameTypeEnum gameType) {
		GameApplication.gameType = gameType;
	}
}
