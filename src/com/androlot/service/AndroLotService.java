package com.androlot.service;

import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.androlot.AndroLotActivity;
import com.androlot.R;
import com.androlot.db.GameDbHelper;
import com.androlot.dto.PeticionDto;
import com.androlot.dto.RespuestaNumeroDto;
import com.androlot.dto.TicketDto;
import com.androlot.exception.RespuestaErrorException;
import com.androlot.http.AndrolotHttp;
import com.androlot.util.SharedPreferencesUtil;

public class AndroLotService extends Service {
	
	private volatile Thread t;
	private final static int TIME_TO_WAIT = 1000;// 120000; //2 minutes 
	private final static int MY_ID = 21091982;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("alServiceThread", "Start...");
		t = new Thread(new AndroLotServiceRunnable(getApplicationContext(), this));
		t.start();
		return Service.START_STICKY;
	}
	
	public void onDestroy() {
		stopSelf();
		t = null;
		Log.i("alServiceThread", "Stop...");
	};
	
	
	class AndroLotServiceRunnable implements  Runnable {
		private Context c;
		private Service s;
		
		public AndroLotServiceRunnable(Context c, Service s) {
			this.c = c;
			this.s = s;
			showNotification("Empieza el concurso y la monitorización de premios");
		}
		
		
		@Override
		public void run() {
			Thread thisThread = Thread.currentThread();
			while(t == thisThread){
				Log.i("alServiceThread", "Execute...");
				final GameDbHelper gDbHelper = new GameDbHelper(c);
				List<TicketDto> tickets = gDbHelper.getTickets();
				SharedPreferencesUtil.saveLastCheck(c);
				for(TicketDto ticket : tickets){
					PeticionDto peticionDto = new PeticionDto();
					peticionDto.setNumero(String.valueOf(ticket.getNumber()));
					try {
						final RespuestaNumeroDto respuestaNumero = new AndrolotHttp().premioNumero(peticionDto);
						if(!"0".equals(respuestaNumero.getPremio()) && respuestaNumero.getPremio()!=null){
							int premio = Integer.parseInt(respuestaNumero.getPremio());
							premio = premio/20;
							ticket.setPrice(ticket.getAmmount()*premio);
							gDbHelper.updateTicket(ticket);
							
							//create notification
							showNotification("Tu numero "+ticket.getNumber()+" ha sido premiado con "+ticket.getPrice() +"€");
							
						}
						
						if( 2 == respuestaNumero.getStatus()){
							showNotification("El sorteo ha finalizado.");
							t = null;
							s.stopSelf();
						}
						
						
					} catch (RespuestaErrorException e) {
						Log.e("", "Error looking for a number", e);
					} catch (Exception e) {
						Log.e("", "Error looking for a number", e);
					}
				}
				
				try {
					Thread.sleep(TIME_TO_WAIT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}


		protected void showNotification(String message) {
			NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(c)
			    .setSmallIcon(R.drawable.ic_launcher)
			    .setContentTitle("Aviso de premio")
			    .setContentText(message)
			    .setAutoCancel(Boolean.TRUE);
			Intent resultIntent = new Intent(c, AndroLotActivity.class);
			resultIntent.putExtra("action", AndroLotActivity.Actions.MyNumbers.toString());
			
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
			stackBuilder.addParentStack(AndroLotActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
			    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(MY_ID, mBuilder.build());
		}
		
		
	};

}
