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
	}
}
