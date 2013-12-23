package com.androlot.service;

import java.util.List;

import android.annotation.SuppressLint;
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
import com.androlot.dto.SorteoDto;
import com.androlot.dto.TicketDto;
import com.androlot.exception.RespuestaErrorException;
import com.androlot.http.AndrolotHttp;
import com.androlot.util.SharedPreferencesUtil;

@SuppressLint("NewApi")
public class AndroLotService extends Service {
	
	private volatile Thread t;
	private final static int TIME_TO_WAIT = 60000;// 120000; //2 minutes 
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
			showNotification(c.getString(R.string.notification_title) , R.drawable.ic_launcher, c.getString(R.string.notification_start_game));
		}
		
		
		@Override
		public void run() {
			Thread thisThread = Thread.currentThread();
			AndrolotHttp http = new AndrolotHttp();
			while(t == thisThread){
				Log.i("alServiceThread", "Execute...");
				
				SharedPreferencesUtil.saveLastCheck(c);
				
				final GameDbHelper gDbHelper = new GameDbHelper(c);
				List<TicketDto> tickets = gDbHelper.getTickets();
				
				for(TicketDto ticket : tickets){
					PeticionDto peticionDto = new PeticionDto();
						peticionDto.setNumero(String.valueOf(ticket.getNumber()));
					try {
						final RespuestaNumeroDto respuestaNumero = http.premioNumero(peticionDto);
						if(!"0".equals(respuestaNumero.getPremio()) && respuestaNumero.getPremio()!=null){
							int premio = Integer.parseInt(respuestaNumero.getPremio());
							premio = premio/20;
							if(ticket.getPrice()>0){
								ticket.setPrice(ticket.getAmmount()*premio);
								gDbHelper.updateTicket(ticket);
								//create notification
								showNotification(c.getString(R.string.notification_game_price_title), R.drawable.ic_launcher, 
										getString(R.string.notification_game_price_text, ticket.getNumber(), ticket.getPrice()));
							}
						}
						if(SorteoDto.STATUS_SORTEO_TERMINADO_NO_OFICIAL == respuestaNumero.getStatus()){
							showNotification(c.getString(R.string.notification_title), R.drawable.ic_launcher, "El sorteo ha finalizado.");
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

		
		protected void showNotification(String title, int icon, String message) {
			NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(c)
			    .setSmallIcon(icon)
			    .setContentTitle(title)
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
		
		
	}

}
