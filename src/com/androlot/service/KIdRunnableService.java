package com.androlot.service;

import java.util.List;

import android.app.Service;

import com.androlot.dto.TicketDto;

public class KIdRunnableService extends AbstractRunnableService {

	protected final static int TIME_TO_WAIT = 1000; 
	protected final static int MY_ID = 21092014;

	public KIdRunnableService(Service s) {
		super(s);
	}
	
	@Override
	protected List<TicketDto> findTickets() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected int getServiceId() {
		return MY_ID;
	}
	
	@Override
	protected int getTimeToWait() {
		return TIME_TO_WAIT;
	}
	
	

}
