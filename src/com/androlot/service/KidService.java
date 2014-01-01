package com.androlot.service;

import android.app.Service;

import com.androlot.game.GameTime;
import com.androlot.manager.KidProperties;

public class KidService extends AbstractGameService{

	@Override
	public GameTime getGameTime() {
		return new GameTime(new KidProperties());
	}

	@Override
	public AbstractRunnableService getRunnableService(Service s) {
		return new KIdRunnableService(s);
	}

}
