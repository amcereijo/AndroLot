package com.androlot.runnable;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androlot.R;
import com.androlot.dto.ChristmasResponseResumeDto;
import com.androlot.dto.RespuestaResumenDto;

public class UpdateAllChristmasPrice extends UpdateAllPrice{

	
	public UpdateAllChristmasPrice(Activity activity) {
		super(activity);
	}

	@Override
	public void updateView(final RespuestaResumenDto response){
		
		activity.runOnUiThread(new Runnable() {
			LayoutInflater inflater = LayoutInflater.from(activity);
			@Override
			public void run() {
				TextView titulo = (TextView) activity.findViewById(R.id.lista_premios_lista_titulo);
				titulo.setVisibility(View.GONE);
				crearListaNumeros((ChristmasResponseResumeDto)response);
			}


			private void crearListaNumeros( final ChristmasResponseResumeDto respuesta) {
				LinearLayout lista = (LinearLayout) activity.findViewById(R.id.lista_premios_lista_numeros);
				
				lista.addView(createTitleElement(R.string.premio_gordo_string));
				lista.addView(createTitleElement(R.string.premio_gordo_string_2));
				lista.addView(createListElement(R.string.premio_gordo_string_amount,respuesta.getNumero1()));
				
				lista.addView(createTitleElement(R.string.premio_2_string));
				lista.addView(createTitleElement(R.string.premio_2_string_2));
				lista.addView(createListElement(R.string.premio_2_string_amount,respuesta.getNumero2()));
				
				
				lista.addView(createTitleElement(R.string.premio_3_string));
				lista.addView(createTitleElement(R.string.premio_3_string_2));
				lista.addView(createListElement(R.string.premio_3_string_amount,respuesta.getNumero3()));
		
				
				lista.addView(createTitleElement(R.string.premio_4_string));
				lista.addView(createTitleElement(R.string.premio_4_string_2));
				lista.addView(createListElement(R.string.premio_4_string_amount,respuesta.getNumero4()));
				lista.addView(createListElement(R.string.premio_4_string_amount,respuesta.getNumero5()));
				
				
				lista.addView(createTitleElement(R.string.premio_5_string));
				lista.addView(createTitleElement(R.string.premio_5_string_2));
				lista.addView(createListElement(R.string.premio_5_string_amount,respuesta.getNumero6()));
				lista.addView(createListElement(R.string.premio_5_string_amount,respuesta.getNumero7()));
				lista.addView(createListElement(R.string.premio_5_string_amount,respuesta.getNumero8()));
				lista.addView(createListElement(R.string.premio_5_string_amount,respuesta.getNumero9()));
				lista.addView(createListElement(R.string.premio_5_string_amount,respuesta.getNumero10()));
				lista.addView(createListElement(R.string.premio_5_string_amount,respuesta.getNumero11()));
				lista.addView(createListElement(R.string.premio_5_string_amount,respuesta.getNumero12()));
				lista.addView(createListElement(R.string.premio_5_string_amount,respuesta.getNumero13()));
				
			}	
			
			
			private LinearLayout createTitleElement(int titleText){
				LinearLayout titleElement = (LinearLayout) inflater.inflate(R.layout.list_number_title, null);
				TextView titleView = (TextView)titleElement.findViewById(R.id.title);
				titleView.setText(titleText);
				return titleElement;
			}
			
			private LinearLayout createListElement(int premio, String numero){
				LinearLayout elementoLista = (LinearLayout) inflater.inflate(R.layout.lista_elemento_numero, null);
				TextView textoNumero = (TextView)elementoLista.findViewById(R.id.lista_numero);
				TextView textoPremio = (TextView)elementoLista.findViewById(R.id.lista_premio);
				if(null== numero || "".equals(numero) || "-1".equals(numero)){
					textoNumero.setText("-");
				}else{
					textoNumero.setText(numero);	
				}
				if(premio != -1){
					textoPremio.setText(premio);
				}
				return elementoLista;
			}
			
		});
	}
}
