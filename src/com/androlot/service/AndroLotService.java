package com.androlot.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AndroLotService extends Service {
	
	private volatile Thread t;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("alServiceThread", "Start...");
		t = new Thread(alServiceThread);
		t.start();		
		return Service.START_STICKY;
	}
	
	public void onDestroy() {
		stopSelf();
		t = null;
		Log.i("alServiceThread", "Stop...");
	};
	
	
	Runnable alServiceThread = new Runnable() {
		
		@Override
		public void run() {
			Thread thisThread = Thread.currentThread();
			while(t == thisThread){
			Log.i("alServiceThread", "Execute...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
		}
		
		
	};

}
