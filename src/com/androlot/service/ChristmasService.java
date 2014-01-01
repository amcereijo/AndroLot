package com.androlot.service;

import android.app.Service;

import com.androlot.game.GameTime;
import com.androlot.manager.ChristmasProperties;


public class ChristmasService extends AbstractGameService {
	
	
	@Override
	public AbstractRunnableService getRunnableService(Service s) {
		return new  ChristmasRunnableService(s);
	}
	

	@Override
	public GameTime getGameTime() {
		return new GameTime(new ChristmasProperties());
	}

}
