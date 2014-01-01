package com.androlot.runnable;

import android.content.Context;

import com.androlot.R;
import com.androlot.application.GameApplication;
import com.androlot.db.GameDbHelper;
import com.androlot.dto.PeticionDto;
import com.androlot.dto.RespuestaNumeroDto;
import com.androlot.dto.TicketDto;
import com.androlot.exception.RespuestaErrorException;
import com.androlot.http.AndrolotFactory;

public class CheckNumberPrice implements Runnable {

	final TicketDto ticket;
	final UpdateMyNumbersView updateMyNumbersView;
	
	public CheckNumberPrice(TicketDto ticket, UpdateMyNumbersView updateMyNumbersView) {
		this.ticket = ticket;
		this.updateMyNumbersView = updateMyNumbersView;
	}
	
	@Override
	public void run() {
		PeticionDto peticionDto = new PeticionDto();
		peticionDto.setNumero(String.valueOf(ticket.getNumber()));
		try {
			final RespuestaNumeroDto respuestaNumero = AndrolotFactory.getInstance(ticket.getGameType()).premioNumero(peticionDto);
			
			Context context = GameApplication.getContext();
			String price = "";
			if(!"0".equals(respuestaNumero.getPremio()) && respuestaNumero.getPremio()!=null){
				int premio = (Integer.parseInt(respuestaNumero.getPremio()))/20;
				float  won = premio*ticket.getAmmount();
				
				price = String.format(context.getString(R.string.my_number_win), String.format("%.2f", won));
				price += context.getString(R.string.donnate);
				ticket.setPrice(ticket.getAmmount()*premio);
			}else{
				price = context.getString(R.string.my_number_no_price);
				ticket.setPrice(0);
			}
			
			updateMyNumbersView.updateView(price);
			
			new GameDbHelper(context).updateTicket(ticket);
				
		} catch (RespuestaErrorException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
