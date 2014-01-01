package com.androlot.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.androlot.game.GameTime;

public abstract class AbstractGameService extends Service {
	
	protected volatile Thread t;
	protected AbstractRunnableService runnableService;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("alServiceThread", "Start...");
		runnableService = getRunnableService(this);
		t = new Thread(runnableService);
		runnableService.setT(t);
		t.start();
		return Service.START_STICKY;
	}
	
	public void onDestroy() {
		stopSelf();
		t = null;
		runnableService.setT(t);
		Log.i("alServiceThread", "Stop...");
	};
	
	public abstract GameTime getGameTime(); 
	
	public abstract AbstractRunnableService getRunnableService(Service s);
}
