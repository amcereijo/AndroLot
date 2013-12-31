package com.androlot.service;

import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.androlot.AndroLotActivity;
import com.androlot.R;
import com.androlot.db.GameDbHelper;
import com.androlot.dto.PeticionDto;
import com.androlot.dto.RespuestaNumeroDto;
import com.androlot.dto.SorteoDto;
import com.androlot.dto.TicketDto;
import com.androlot.enums.GameTypeEnum;
import com.androlot.enums.NotificationActionsEnum;
import com.androlot.exception.RespuestaErrorException;
import com.androlot.http.AndrolotFactory;
import com.androlot.http.AndrolotHttp;
import com.androlot.util.SharedPreferencesUtil;


public abstract class AbstractRunnableService implements  Runnable {
	
	protected Thread t;
	protected Context c;
	protected Service s;
	
	public void setT(Thread t) {
		this.t = t;
	}
	public Thread getT() {
		return t;
	}
	
	public AbstractRunnableService(Service s) {
		this.s = s;
		this.c = s.getApplicationContext();
		this.t = Thread.currentThread();
		
		showNotification(c.getString(R.string.notification_title) , 
				R.drawable.ic_launcher, c.getString(R.string.notification_start_game));
	}
	
	
	@Override
	public void run() {
		AndrolotHttp http = AndrolotFactory.getInstance(GameTypeEnum.ChristMas);

		while(t!=null){
			Log.i("alServiceThread", "Execute...");
			
			SharedPreferencesUtil.saveLastCheck(c);
			
			List<TicketDto> tickets = findTickets();
			
			for(TicketDto ticket : tickets){
				PeticionDto peticionDto = new PeticionDto();
					peticionDto.setNumero(String.valueOf(ticket.getNumber()));
				try {
					final RespuestaNumeroDto respuestaNumero = http.premioNumero(peticionDto);
					if(!"0".equals(respuestaNumero.getPremio()) && respuestaNumero.getPremio()!=null){
						processPrice(ticket, respuestaNumero);
					}
					
					if(SorteoDto.STATUS_SORTEO_TERMINADO_NO_OFICIAL == respuestaNumero.getStatus()){
						stopServiceGame();
					}
					
				} catch (RespuestaErrorException e) {
					Log.e("", "Error looking for a number", e);
				} catch (Exception e) {
					Log.e("", "Error looking for a number", e);
				}
			}
			
			try {
				Thread.sleep(getTimeToWait());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	protected void processPrice(TicketDto ticket, final RespuestaNumeroDto respuestaNumero) {
		int premio = Integer.parseInt(respuestaNumero.getPremio());
		premio = premio/20;
		if(ticket.getPrice()<0){
			ticket.setPrice(ticket.getAmmount()*premio);
			
			new GameDbHelper(c).updateTicket(ticket);
			
			//create notification
			showNotification(c.getString(R.string.notification_game_price_title), R.drawable.ic_launcher, 
					c.getString(R.string.notification_game_price_text, ticket.getNumber(), ticket.getPrice()));
		}
	}


	protected void stopServiceGame() {
		showNotification(c.getString(R.string.notification_title), R.drawable.ic_launcher, "El sorteo ha finalizado.");
		t = null;
		s.stopSelf();
	}

	
	protected void showNotification(String title, int icon, String message) {
		
		Intent resultIntent = new Intent(c, AndroLotActivity.class);
		resultIntent.putExtra("action", NotificationActionsEnum.MyNumbers_Christmas.toString());
		
		NotificationCompat.Builder builder =  
	            new NotificationCompat.Builder(c)  
	            .setSmallIcon(icon)  
	            .setContentTitle(title)  
	            .setContentText(message)
	            .setAutoCancel(Boolean.TRUE);  
  
	    PendingIntent contentIntent = PendingIntent.getActivity(c, 0, resultIntent,   
	            PendingIntent.FLAG_UPDATE_CURRENT);  
	    builder.setContentIntent(contentIntent);  

	    
	    NotificationManager manager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);  
	    manager.notify(0, builder.build());
		
	}
	
	protected abstract List<TicketDto> findTickets();
	protected abstract int getServiceId();
	protected abstract int getTimeToWait();

}
