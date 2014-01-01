package com.androlot.runnable;

import android.app.Activity;

import com.androlot.dto.RespuestaResumenDto;

public abstract class UpdateAllPrice {

	protected Activity activity;
	
	public UpdateAllPrice(Activity activity) {
		this.activity = activity;
	}
	
	public abstract void updateView(final RespuestaResumenDto response);
}
