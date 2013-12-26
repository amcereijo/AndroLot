package com.androlot.service;

import android.app.Service;

import com.androlot.game.GameTime;

public abstract class AbstractGameService extends Service {

	public abstract GameTime getGameTime(); 
	
}
