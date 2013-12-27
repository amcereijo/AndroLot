package com.androlot.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.androlot.game.GameTime;
import com.androlot.manager.KidProperties;

public class KidService extends AbstractGameService{

	@Override
	public GameTime getGameTime() {
		return new GameTime(new KidProperties());
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractRunnableService getRunnableService(Service s) {
		// TODO Auto-generated method stub
		return null;
	}

}
