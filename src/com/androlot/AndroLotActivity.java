package com.androlot;

import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androlot.application.GameApplication;
import com.androlot.controller.ServiceController;
import com.androlot.db.GameDbHelper;
import com.androlot.dto.TicketDto;
import com.androlot.enums.GameTypeEnum;
import com.androlot.enums.NotificationActionsEnum;
import com.androlot.http.AndrolotFactory;
import com.androlot.http.AndrolotHttp;
import com.androlot.runnable.CheckAllPrice;
import com.androlot.runnable.CheckNumberPrice;
import com.androlot.runnable.UpdateAllPrice;
import com.androlot.runnable.UpdateAllPriceFactory;
import com.androlot.runnable.UpdateButton;
import com.androlot.runnable.UpdateMyNumbersView;
import com.androlot.service.ChristmasService;
import com.androlot.service.KidService;
import com.androlot.util.SharedPreferencesUtil;

/**
 * 
 * @author angelcereijo
 *
 */
public class AndroLotActivity extends BaseActivity {
	
	private static final String MY_NUMBER_TIME_CHECKED = "Última comprobación a las <b>%s</b>";

	private GameTypeEnum gameType;
	private ServiceController<?> serviceController;
	private String gameTitle;
	private Class<?> classGameType;
	
	private boolean principalShow = true;
	private List<TicketDto> tickets;
	private final static String TITLE_FORMAT = "%s - %s";
	
	private GameDbHelper gameDbHelper;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_androlot);
		
		prepareGameConfiguration();
		checkServiceRunning();
		setGameTitle();
		
		gameDbHelper = new GameDbHelper(this);
		
		doInitAction();
	}

	private void doInitAction() {
		if(getIntent().getExtras()!=null && 
				NotificationActionsEnum.MyNumbers_Christmas.toString().equals(getIntent().getStringExtra("action"))){
			initializeMyNumbers();
			((Button)findViewById(R.id.check_prices_button)).requestFocus();
		}
	}

	private void prepareGameConfiguration(){
		switch(GameApplication.getGameType()){
			case ChristMas: 
				serviceController = new ServiceController<ChristmasService>(new ChristmasService());
				gameType = GameTypeEnum.ChristMas;
				gameTitle = getResources().getString(R.string.main_text_christmas_string);
				classGameType = ChristmasService.class;
				break;
			case Kid:
				serviceController = new ServiceController<KidService>(new KidService());
				gameType = GameTypeEnum.Kid;
				gameTitle = getResources().getString(R.string.main_text_kid_string);
				classGameType = KidService.class;
				break;
		}
	}


	private void setGameTitle() {
		setTitle(String.format(TITLE_FORMAT, getResources().getString(R.string.app_name),gameTitle));
	}


	private void checkServiceRunning(){
		if(GameApplication.isServiceRunning(classGameType)){
			((ToggleButton)findViewById(R.id.service_button)).setChecked(Boolean.TRUE);
		}
	}

	protected void initializeMyNumbers() {
		consultarNumero(null);
	}


	@Override
	public void onBackPressed() {
		if(principalShow){
			//finish();
			super.onBackPressed();
		}else{
			setContentView(R.layout.activity_androlot);
			principalShow = true;
			setGameTitle();
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
		tickets = gameDbHelper.getTickets(gameType);
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
		
		String numberText = numero.getText().toString();
		String ammountText = cantidad.getText().toString();
		
		if(numberText==null || "".equals(numberText) || numberText.length()>5 || 
			ammountText==null || "".equals(ammountText) ){
			Toast.makeText(getApplicationContext(), R.string.error_aniadir_numero_string, Toast.LENGTH_LONG).show();
		}else{
			TicketDto ticket = new TicketDto();
				ticket.setNumber(Integer.parseInt(numero.getText().toString()));
				ticket.setAmmount(Float.parseFloat(cantidad.getText().toString()));
				ticket.setGameType(gameType);
			
			gameDbHelper.addNumber(ticket);
			
			aniadirNumeroALista(ticket);
			tickets.add(ticket);
			
			numero.setText("");
			cantidad.setText("");
		}
	}

	/**
	 * 
	 * @param v
	 */
	public void deleteNumber (View v){
		LinearLayout numberLayout = (LinearLayout) v.getParent();
		
		TextView numero = (TextView) numberLayout.findViewById(R.id.check_number_element_number);
		gameDbHelper.removeTicket(numero.getText().toString(), gameType);
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
		
		LinearLayout layoutElementoNumero =(LinearLayout)LayoutInflater.from(this).inflate(R.layout.consulta_numero_elemento, aniadirNumeroLista, false);
		
		TextView numberTextText = (TextView)layoutElementoNumero.findViewById(R.id.check_number_element_text);
		TextView numberText = (TextView)layoutElementoNumero.findViewById(R.id.check_number_element_number);
		TextView priceText = (TextView)layoutElementoNumero.findViewById(R.id.check_number_element_result);
		
		numberTextText.setText(Html.fromHtml(String.format(getString(R.string.my_number_play), ticket.getAmmount())));
		numberText.setText(ticket.getNumberComplete());
		String price = "";
		if(ticket.getPrice()>0){
			price = String.format(getString(R.string.my_number_win), String.format("%.2f", ticket.getPrice()));
			price += getString(R.string.donnate);
		}else if(ticket.getPrice()!=-1){
			price = getString(R.string.my_number_no_price); 
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
			final LinearLayout numberElement = (LinearLayout)aniadirNumeroLista.getChildAt(pos);
			
			final Button checkButton = (Button)findViewById(R.id.check_prices_button);
			checkButton.setText(R.string.checking_prices_string);
			
			UpdateButton updateButtom = null;
			if(pos == tickets.size()-1) { 
				updateButtom = new UpdateButton(checkButton, R.string.check_prices_string); 
			}
			
			TextView priceText = (TextView)numberElement.findViewById(R.id.check_number_element_result);
			UpdateMyNumbersView updateMyNumbersView = new UpdateMyNumbersView(this, priceText, updateButtom);
			new Thread(new CheckNumberPrice(ticket,updateMyNumbersView) ).start();
			
			String moment =  SharedPreferencesUtil.saveLastCheck(this);
			showLastCheck(moment);
		}
	}

	
	protected void showLastCheck(String moment) {
		if(moment != null && !"".equals(moment)){
			TextView lastUpdateText = (TextView)findViewById(R.id.lastCheclView);
			lastUpdateText.setText(Html.fromHtml(String.format(MY_NUMBER_TIME_CHECKED, moment)));
			lastUpdateText.setVisibility(View.VISIBLE);
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
		
		AndrolotHttp androlotHttp = AndrolotFactory.getInstance(gameType);
		UpdateAllPrice updateAllPrice = UpdateAllPriceFactory.getInstance(this, gameType);
		
		new Thread(new CheckAllPrice(androlotHttp, updateAllPrice)).start();
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
	
}
