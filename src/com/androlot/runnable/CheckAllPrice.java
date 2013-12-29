package com.androlot.runnable;

import com.androlot.dto.RespuestaResumenDto;
import com.androlot.exception.RespuestaErrorException;
import com.androlot.http.AndrolotHttp;

public class CheckAllPrice implements Runnable{
	
	private AndrolotHttp androlotHttp;
	private UpdateAllPrice updateAllPrice;
	
	public CheckAllPrice(AndrolotHttp androlotHttp, UpdateAllPrice updateAllPrice) {
		this.androlotHttp = androlotHttp;
		this.updateAllPrice = updateAllPrice;
	}
	
	@Override
	public void run() {
		try {
			final RespuestaResumenDto response = androlotHttp.resumenPremios();
			updateAllPrice.updateView(response);
		} catch (RespuestaErrorException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
