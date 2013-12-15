package com.androlot;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androlot.controller.ServiceController;
import com.androlot.db.GameDbHelper;
import com.androlot.dto.PeticionDto;
import com.androlot.dto.RespuestaNumeroDto;
import com.androlot.dto.RespuestaResumenDto;
import com.androlot.dto.TicketDto;
import com.androlot.exception.RespuestaErrorException;
import com.androlot.http.AndrolotHttp;
import com.androlot.util.SharedPreferencesUtil;

/**
 * 
 * @author angelcereijo
 *
 */
public class AndroLotActivity extends Activity {
	
	public enum Actions{
		MyNumbers
	}
	private ServiceController serviceController;
	private boolean principalShow = true;
	private List<TicketDto> tickets;
	private final static String TITLE_FORMAT = "%s - %s";
	private GameDbHelper gameDbHelper;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_androlot);
		serviceController = new ServiceController();
		setTitle(String.format(TITLE_FORMAT, getResources().getString(R.string.app_name), 
				getResources().getString(R.string.main_text_titulo_string)));
		
		gameDbHelper = new GameDbHelper(this);
		if(getIntent().getExtras()!=null && Actions.MyNumbers.toString().equals(getIntent().getStringExtra("action"))){
			initializeMyNumbers();
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
		LinearLayout numberLayout = (LinearLayout) v.getParent();
		
		TextView numero = (TextView) numberLayout.findViewById(R.id.consulta_numero_elemento_numero);
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
		
		TextView textoNumero = (TextView)layoutElementoNumero.findViewById(R.id.consulta_numero_elemento_numero);
		
		String text = String.format("Juegas %s€ al numero %s.", ticket.getAmmount(),ticket.getNumberComplete());
		if(ticket.getPrice()>0){
			text += String.format(" Has ganado %s€", ticket.getPrice());
		}else{
			text += " No hay premio"; 
		}
		
		textoNumero.setText(text);
		
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
			new Thread(new CheckNumberPrice(ticket, numberElement)).start();
			
			String moment =  SharedPreferencesUtil.saveLastCheck(this);
			showLastCheck(moment);
		}
	}


	protected void showLastCheck(String moment) {
		TextView lastUpdateText = (TextView)findViewById(R.id.lastCheclView);
		lastUpdateText.setText("Comprobado a las "+moment);
		lastUpdateText.setVisibility(View.VISIBLE);
	}


	private class CheckNumberPrice implements Runnable{
		final TicketDto ticket;
		final View numberElement;
		
		public CheckNumberPrice(TicketDto ticket, View layout) {
			this.ticket = ticket;
			this.numberElement = layout;
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
						//TextView textoPremio = (TextView)numberElement.findViewById(R.id.consulta_numero_elemento_premio);
						TextView textoNumero = (TextView)numberElement.findViewById(R.id.consulta_numero_elemento_numero);
						
						String valueTextoNumero = String.format("Juegas %s€ al numero %s.", ticket.getAmmount(),ticket.getNumberComplete());
						
						if(!"0".equals(respuestaNumero.getPremio()) && respuestaNumero.getPremio()!=null){
							int premio = Integer.parseInt(respuestaNumero.getPremio());
							premio = premio/20;
							//textoPremio.setText(String.valueOf(ticket.getAmmount()*premio));
							valueTextoNumero += String.format(" Has ganado %s€", String.valueOf(ticket.getAmmount()*premio));
							ticket.setPrice(ticket.getAmmount()*premio);
						}else{
							//textoPremio.setText(respuestaNumero.getPremio());
							valueTextoNumero += " No hay premio"; 
							ticket.setPrice(0);
						}
						
						textoNumero.setText(valueTextoNumero);
						
						gameDbHelper.updateTicket(ticket);
						
					}
				});
			} catch (RespuestaErrorException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
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
	
	
	public void startService(View v){
		serviceController.startService(this);
	}
	
	public void stopService(View v){
		serviceController.stopService(this);
	}
}
