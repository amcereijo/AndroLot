package com.androlot.http;

import com.androlot.enums.GameTypeEnum;

public class AndrolotFactory {

	public static AndrolotHttp getInstance(GameTypeEnum gameType){
		AndrolotHttp androlotHttp = null;
		switch (gameType) {
			case ChristMas:
				androlotHttp = new ChristmasHttp();
				break;
			case Kid:
				androlotHttp =  new KIdHttp();
				break;
		}
		return androlotHttp;
	}
}
