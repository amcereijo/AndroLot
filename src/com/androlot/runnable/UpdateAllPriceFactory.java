package com.androlot.runnable;

import android.app.Activity;

import com.androlot.enums.GameTypeEnum;

public class UpdateAllPriceFactory {

	public static UpdateAllPrice getInstance(Activity activity, GameTypeEnum gameType){
		UpdateAllPrice updateAllPrice = null;
		switch (gameType) {
			case ChristMas:
				updateAllPrice = new UpdateAllChristmasPrice(activity);
				break;
			case Kid:
				updateAllPrice = new UpdateAllKidPrice(activity);
				break;
		}
		return updateAllPrice;
	}
}
