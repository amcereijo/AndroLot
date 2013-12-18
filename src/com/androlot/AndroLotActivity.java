package com.androlot;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androlot.controller.ServiceController;
import com.androlot.db.GameDbHelper;
import com.androlot.dto.PeticionDto;
import com.androlot.dto.RespuestaNumeroDto;
import com.androlot.dto.RespuestaResumenDto;
import com.androlot.dto.TicketDto;
import com.androlot.exception.RespuestaErrorException;
import com.androlot.http.AndrolotHttp;
import com.androlot.service.AndroLotService;
import com.androlot.util.SharedPreferencesUtil;

/**
 * 
 * @author angelcereijo
 *
 */
public class AndroLotActivity extends Activity {
	
	private static final String MY_NUMBER_TIME_CHECKED = "Comprobado a las <b>%s</b>";


	public enum Actions{
		MyNumbers
	}
	private ServiceController serviceController;
	private boolean principalShow = true;
	private List<TicketDto> tickets;
	private final static String TITLE_FORMAT = "%s - %s";
	private final static String MY_NUMBER = "Juegas <b>%s€</b> al numero";
	private final static String MY_NUMBER_WIN = "<b>Has ganado %s€</b>";
	private final static String MY_NUMBER_NO_PRICE = "No hay premio";
	private GameDbHelper gameDbHelper;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_androlot);
		checkServiceRunning();
		
		serviceController = new ServiceController();
		setTitle(String.format(TITLE_FORMAT, getResources().getString(R.string.app_name), 
				getResources().getString(R.string.main_text_titulo_string)));
		
		gameDbHelper = new GameDbHelper(this);
		if(getIntent().getExtras()!=null && Actions.MyNumbers.toString().equals(getIntent().getStringExtra("action"))){
			initializeMyNumbers();
			((Button)findViewById(R.id.check_prices_button)).requestFocus();
		}
	}

	
	private void checkServiceRunning(){
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (AndroLotService.class.getName().equals(service.service.getClassName())) {
	            ((ToggleButton)findViewById(R.id.service_button)).setChecked(Boolean.TRUE);
	        }
	    }
	}

	protected void initializeMyNumbers() {
		consultarNumero(null);
	}


	@Override
	public void onBackPressed() {
		if(principalShow){
			finish();
		}else{
			setContentView(R.layout.activity_androlot);
			principalShow = true;
			setTitle(String.format(TITLE_FORMAT, getResources().getString(R.string.app_name),
					getResources().getString(R.string.main_text_titulo_string)));
			checkServiceRunning();
		}
	}
	
	/**
	 * 
	 * @param v
	 */
	public void consultarNumero(View v){
		setContentView(R.layout.consulta_numero);
		principalShow = false;
		loadSavedNumbers();
		setTitle(String.format(TITLE_FORMAT, getResources().getString(R.string.app_name),
				getResources().getString(R.string.main_btn_numero_string)));
		
		String moment = SharedPreferencesUtil.readLastCheck(this);
		showLastCheck(moment);
	}
	
	
	private void loadSavedNumbers() {
		tickets = gameDbHelper.getTickets();
		for(TicketDto ticket : tickets){
			aniadirNumeroALista(ticket);
		}
	}
	
	/**
	 * 
	 * @param v
	 */
	public void aniadirNumero(View v){
		EditText numero = (EditText)findViewById(R.id.aniadir_numero_numero);
		EditText cantidad = (EditText)findViewById(R.id.aniadir_numero_cantidad);
		
		if("".equals(numero.getText().toString()) || numero.getText().toString()==null ||
				"".equals(cantidad.getText().toString()) || cantidad.getText().toString()==null){
			Toast.makeText(getApplicationContext(), R.string.error_aniadir_numero_string, Toast.LENGTH_LONG).show();
		}else{
			TicketDto ticket = new TicketDto();
			ticket.setNumber(Integer.parseInt(numero.getText().toString()));
			ticket.setAmmount(Float.parseFloat(cantidad.getText().toString()));
			
			numero.setText("");
			cantidad.setText("");
			aniadirNumeroALista(ticket);
			
			gameDbHelper.addNumber(ticket);
			tickets.add(ticket);
		}
	}

	/**
	 * 
	 * @param v
	 */
	public void deleteNumber (View v){
		RelativeLayout numberLayout = (RelativeLayout) v.getParent();
		
		TextView numero = (TextView) numberLayout.findViewById(R.id.check_number_element_number);
		gameDbHelper.removeTicket(numero.getText().toString());
		deleteTicketFromList(numero.getText().toString());
		
		((LinearLayout) numberLayout.getParent()).removeView(numberLayout);
	}
	
	private void deleteTicketFromList(String number){
		Iterator<TicketDto> it = tickets.iterator();
		boolean done = false;
		while(it.hasNext() && !done){
			TicketDto ticket = it.next();
			if(ticket.getNumber() == Integer.parseInt(number)){
				it.remove();
				done = true;
			}
		}
	}
	

	private void aniadirNumeroALista(TicketDto ticket) {
		LinearLayout aniadirNumeroLista = (LinearLayout)findViewById(R.id.aniadir_numero_lista);
		
		RelativeLayout layoutElementoNumero =(RelativeLayout)LayoutInflater.from(this).inflate(R.layout.consulta_numero_elemento, aniadirNumeroLista, false);
		
		TextView numberTextText = (TextView)layoutElementoNumero.findViewById(R.id.check_number_element_text);
		TextView numberText = (TextView)layoutElementoNumero.findViewById(R.id.check_number_element_number);
		TextView priceText = (TextView)layoutElementoNumero.findViewById(R.id.check_number_element_result);
		
		numberTextText.setText(Html.fromHtml(String.format(MY_NUMBER, ticket.getAmmount())));
		numberText.setText(ticket.getNumberComplete());
		String price = "";
		if(ticket.getPrice()>0){
			price = String.format(MY_NUMBER_WIN, ticket.getPrice());
		}else{
			price = MY_NUMBER_NO_PRICE; 
		}
		
		priceText.setText(Html.fromHtml(price));
		
		aniadirNumeroLista.addView(layoutElementoNumero);
	}
	
	
	/**
	 * 
	 * @param v
	 */
	public void comprobarNumeros(View v){
		LinearLayout aniadirNumeroLista = (LinearLayout) findViewById(R.id.aniadir_numero_lista);
		for(int pos=0;pos<tickets.size();pos++){
			final TicketDto ticket = tickets.get(pos);
			final RelativeLayout numberElement = (RelativeLayout)aniadirNumeroLista.getChildAt(pos);
			
			final Button checkButton = (Button)findViewById(R.id.check_prices_button);
			checkButton.setText(R.string.checking_prices_string);
			
			UpdateButton updateButtom = null;
			if(pos == tickets.size()-1) { updateButtom = new UpdateButton(checkButton, R.string.check_prices_string); }
			
			new Thread(new CheckNumberPrice(ticket, numberElement, updateButtom) ).start();
			
			String moment =  SharedPreferencesUtil.saveLastCheck(this);
			showLastCheck(moment);
		}
	}

	

	protected void showLastCheck(String moment) {
		TextView lastUpdateText = (TextView)findViewById(R.id.lastCheclView);
		lastUpdateText.setText(Html.fromHtml(String.format(MY_NUMBER_TIME_CHECKED, moment)));
		lastUpdateText.setVisibility(View.VISIBLE);
	}


	private class CheckNumberPrice implements Runnable{
		final TicketDto ticket;
		final View numberElement;
		final UpdateButton updateButtom;
		
		public CheckNumberPrice(TicketDto ticket, View layout, UpdateButton updateButtom) {
			this.ticket = ticket;
			this.numberElement = layout;
			this.updateButtom = updateButtom;
		}
		
		@Override
		public void run() {
			PeticionDto peticionDto = new PeticionDto();
			peticionDto.setNumero(String.valueOf(ticket.getNumber()));
			try {
				final RespuestaNumeroDto respuestaNumero = new AndrolotHttp().premioNumero(peticionDto);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						TextView priceText = (TextView)numberElement.findViewById(R.id.check_number_element_result);
						
						String price = "";
						if(!"0".equals(respuestaNumero.getPremio()) && respuestaNumero.getPremio()!=null){
							int premio = Integer.parseInt(respuestaNumero.getPremio());
							premio = premio/20;
							price = String.format(MY_NUMBER_WIN, String.valueOf(ticket.getAmmount()*premio));
							ticket.setPrice(ticket.getAmmount()*premio);
						}else{
							price = MY_NUMBER_NO_PRICE;
							ticket.setPrice(0);
						}
						
						priceText.setText(Html.fromHtml(price));
						
						gameDbHelper.updateTicket(ticket);
						
						if(updateButtom!=null){
							updateButtom.changeMessage();
						}
					}
				});
			} catch (RespuestaErrorException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param v
	 */
	public void resumenPremios(View v){
		setContentView(R.layout.lista_premios_layout);
		principalShow = false;
		
		setTitle(String.format(TITLE_FORMAT, getResources().getString(R.string.app_name),
				getResources().getString(R.string.main_btn_resumen_string)));
		
		new Thread(new CheckAllPrice()).start();
	}

	
	private class CheckAllPrice implements Runnable {
		@Override
		public void run() {
			try{
				final RespuestaResumenDto respuesta = new AndrolotHttp().resumenPremios();
				runOnUiThread(new Runnable() {
					LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
					@Override
					public void run() {
						TextView titulo = (TextView) findViewById(R.id.lista_premios_lista_titulo);
						titulo.setVisibility(View.GONE);
						
						
						crearListaNumeros(respuesta);
					}


					private void crearListaNumeros( final RespuestaResumenDto respuesta) {
						LinearLayout lista = (LinearLayout)findViewById(R.id.lista_premios_lista_numeros);
						lista.addView(crearElementoLista(R.string.premio_gordo_string,respuesta.getNumero1()));
						lista.addView(crearElementoLista(R.string.premio_2_string,respuesta.getNumero2()));
						lista.addView(crearElementoLista(R.string.premio_3_string,respuesta.getNumero3()));
						lista.addView(crearElementoLista(R.string.premio_4_string,respuesta.getNumero4()));
						lista.addView(crearElementoLista(R.string.premio_4_1_string,respuesta.getNumero5()));
						lista.addView(crearElementoLista(R.string.premio_5_string,respuesta.getNumero6()));
						lista.addView(crearElementoLista(R.string.premio_5_1_string,respuesta.getNumero7()));
						lista.addView(crearElementoLista(R.string.premio_5_2_string,respuesta.getNumero8()));
						lista.addView(crearElementoLista(R.string.premio_5_3_string,respuesta.getNumero9()));
						lista.addView(crearElementoLista(R.string.premio_5_4_string,respuesta.getNumero10()));
						lista.addView(crearElementoLista(R.string.premio_5_5_string,respuesta.getNumero11()));
						lista.addView(crearElementoLista(R.string.premio_5_6_string,respuesta.getNumero12()));
						lista.addView(crearElementoLista(R.string.premio_5_7_string,respuesta.getNumero13()));
					}	
					
					
					private LinearLayout crearElementoLista(int premio, String numero){
						LinearLayout elementoLista = (LinearLayout) inflater.inflate(R.layout.lista_elemento_numero, null);
						TextView textoNumero = (TextView)elementoLista.findViewById(R.id.lista_numero);
						TextView textoPremio = (TextView)elementoLista.findViewById(R.id.lista_premio);
						if("-1".equals(numero)){
							textoNumero.setText("-");
						}else{
							textoNumero.setText(numero);	
						}
						textoPremio.setText(premio);
						return elementoLista;
					}
					
				});
			}catch(Exception e){
				Log.e("", e.getMessage(),e);
			}
		}
	}
	
	/**
	 * 
	 * @param v
	 */
	public void toggleService(View v){
		boolean on = ((ToggleButton) v).isChecked();
	    if (on) {
	    	serviceController.startService(this);
	    } else {
	    	serviceController.stopService(this);
	    }
	}
	
	/**
	 * 
	 * @param v
	 */
	public void showHelp(View v){
		setContentView(R.layout.help);
		setTitle(String.format(TITLE_FORMAT, getResources().getString(R.string.app_name), 
				getResources().getString(R.string.help_text)));
		principalShow = false;
	}
	
	class UpdateButton {
		private Button button;
		private int message;
		public UpdateButton(Button button, int message){
			this.button = button;
			this.message = message;
		}
		public void changeMessage(){
			this.button.setText(message);
		}
	}
}
