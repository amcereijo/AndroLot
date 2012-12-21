package com.androlot;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androlot.dto.PeticionDto;
import com.androlot.dto.RespuestaNumeroDto;
import com.androlot.dto.RespuestaResumenDto;
import com.androlot.exception.RespuestaErrorException;
import com.androlot.http.AndrolotHttp;

/**
 * 
 * @author angelcereijo
 *
 */
public class AndroLotActivity extends Activity {

	private boolean principalShow = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_androlot);
	}

	
	@Override
	public void onBackPressed() {
		if(principalShow){
			finish();
		}else{
			setContentView(R.layout.activity_androlot);
			principalShow = true;
		}
	}
	
	/**
	 * 
	 * @param v
	 */
	public void consultarNumero(View v){
		setContentView(R.layout.consulta_numero);
		principalShow = false;
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
			aniadirNumeroALista(numero, cantidad);
		}
	}


	private void aniadirNumeroALista(EditText numero, EditText cantidad) {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		LinearLayout layoutElementoNumero =(LinearLayout) inflater.inflate(R.layout.consulta_numero_elemento, null);
		TextView textoNumero = (TextView)layoutElementoNumero.findViewById(R.id.consulta_numero_elemento_numero);
		TextView textoCantidad = (TextView)layoutElementoNumero.findViewById(R.id.consulta_numero_elemento_cantidad);
		
		textoNumero.setText(numero.getText());
		textoCantidad.setText(cantidad.getText());
		
		LinearLayout aniadirNumeroLista = (LinearLayout)findViewById(R.id.aniadir_numero_lista);
		aniadirNumeroLista.addView(layoutElementoNumero);
		
		numero.setText("");
		cantidad.setText("");
	}
	
	
	/**
	 * 
	 * @param v
	 */
	public void comprobarNumeros(View v){
		LinearLayout aniadirNumeroLista = (LinearLayout) findViewById(R.id.aniadir_numero_lista);
		for(int i= 1;i<aniadirNumeroLista.getChildCount();i++){
			final LinearLayout elementoNumero = (LinearLayout)aniadirNumeroLista.getChildAt(i);
			new Thread(new Runnable() {
				@Override
				public void run() {
					final TextView textoNumero = (TextView)elementoNumero.findViewById(R.id.consulta_numero_elemento_numero);
					final TextView textoCantidad = (TextView)elementoNumero.findViewById(R.id.consulta_numero_elemento_cantidad);
					PeticionDto peticionDto = new PeticionDto();
						peticionDto.setNumero(textoNumero.getText().toString());
					try {
						final RespuestaNumeroDto respuestaNumero = new AndrolotHttp().premioNumero(peticionDto);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								TextView textoPremio = (TextView)elementoNumero.findViewById(R.id.consulta_numero_elemento_premio);
								if(!"0".equals(respuestaNumero.getPremio()) && respuestaNumero.getPremio()!=null){
									int premio = Integer.parseInt(respuestaNumero.getPremio());
									premio = ((premio/10)/20);
									int cantidad = Integer.parseInt(textoCantidad.getText().toString());
									textoPremio.setText(cantidad*premio);
								}else{
									textoPremio.setText(respuestaNumero.getPremio());
								}
							}
						});
					} catch (RespuestaErrorException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	
	
	public void resumenPremios(View v){
		setContentView(R.layout.lista_premios_layout);
		principalShow = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					final RespuestaResumenDto respuesta = new AndrolotHttp().resumenPremios();
					runOnUiThread(new Runnable() {
						LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
						@Override
						public void run() {
							TextView titulo = (TextView) findViewById(R.id.lista_premios_lista_titulo);
							titulo.setText(R.string.titulo_premios_string);
							((LinearLayout)findViewById(R.id.lista_premios_lista_cabecera)).setVisibility(View.VISIBLE);
							
							crearListaNumeros(respuesta);
						}


						private void crearListaNumeros(
								final RespuestaResumenDto respuesta) {
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
							textoNumero.setText(numero);
							textoPremio.setText(premio);
							return elementoLista;
						}
						
					});
				}catch(Exception e){
					Log.e("", e.getMessage(),e);
				}
			}
		}).start();
	}
	
}
