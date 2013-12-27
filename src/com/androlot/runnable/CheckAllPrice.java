package com.androlot.runnable;

import com.androlot.dto.ChristmasResponseResumeDto;
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
			final ChristmasResponseResumeDto response = androlotHttp.resumenPremios(ChristmasResponseResumeDto.class);
			updateAllPrice.updateView(response);
		} catch (RespuestaErrorException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
