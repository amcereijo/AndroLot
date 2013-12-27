package com.androlot.service;

import java.util.List;

import android.app.Service;

import com.androlot.db.GameDbHelper;
import com.androlot.dto.TicketDto;
import com.androlot.enums.GameTypeEnum;

public class ChristmasRunnableService extends AbstractRunnableService {

	protected final static int TIME_TO_WAIT = 1000; 
	protected final static int MY_ID = 21091982;
	
	public ChristmasRunnableService(Service s) {
		super(s);
	}

	@Override
	protected List<TicketDto> findTickets(){
		final GameDbHelper gDbHelper = new GameDbHelper(c);
		List<TicketDto> tickets = gDbHelper.getTickets(GameTypeEnum.ChristMas);
		return tickets;
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
