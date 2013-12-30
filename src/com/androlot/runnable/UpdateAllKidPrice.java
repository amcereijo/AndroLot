package com.androlot.runnable;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androlot.R;
import com.androlot.dto.KidResponseResumeDto;
import com.androlot.dto.RespuestaResumenDto;

public class UpdateAllKidPrice extends UpdateAllPrice {

	public UpdateAllKidPrice(Activity activity) {
		super(activity);
	}

	@Override
	public void updateView(final RespuestaResumenDto response) {
		activity.runOnUiThread(new Runnable() {
			LayoutInflater inflater = LayoutInflater.from(activity);
			@Override
			public void run() {
				TextView titulo = (TextView) activity.findViewById(R.id.lista_premios_lista_titulo);
				titulo.setVisibility(View.GONE);
				crearListaNumeros();
			}


			private void crearListaNumeros() {
				final KidResponseResumeDto kidResponse = (KidResponseResumeDto)response;
				LinearLayout lista = (LinearLayout) activity.findViewById(R.id.lista_premios_lista_numeros);
				
				lista.addView(createTitleElement(R.string.kid_principal_prizes_title));
				lista.addView(createListElement(R.string.kid_1_price, kidResponse.getPremio1()));
				lista.addView(createListElement(R.string.kid_2_price, kidResponse.getPremio2()));
				
				lista.addView(createTitleElement(R.string.kid_3_prizes_title));
				for(String s:kidResponse.getExtracciones5cifras()){
					lista.addView(createListElement(-1, s));
				}
			    
				lista.addView(createTitleElement(R.string.kid_special_prizes_title));
				for(String s:kidResponse.getExtracciones3cifras()){
					lista.addView(createListElement(-1, s));
				}
				for(String s:kidResponse.getExtracciones2cifras()){
					lista.addView(createListElement(-1, s));
				}
				
				lista.addView(createTitleElement(R.string.kid_r_prizes_title));
				for(String s:kidResponse.getReintegros()){
					lista.addView(createListElement(-1, s));
				}
				
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
				if("-1".equals(numero)){
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
