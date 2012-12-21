package com.androlot.dto;


/**
 * 
 * @author angelcereijo
 *
 */
public final class SorteoDto{
	/*
	- s	status	 (estado del sorteo). El parámetro pasado sólo puede tomar el 
	valor 1 y, en la contestación, se informará de la fase en la que se está 
	en el sorteo.
	Valores:
	A) 0	El sorteo no ha comenzado aún. Todos los números aparecerán como no premiados.
	B) 1	El sorteo ha empezado. La lista de números premiados se va cargando poco a poco. Un número premiado podría llegar a tardar unos minutos en aparecer.
	C) 2	El sorteo ha terminado y la lista de números y premios debería ser la correcta aunque, tomada al oído, no podemos estar seguros de ella.
	D) 3	El sorteo ha terminado y existe una lista oficial en PDF.
	E) 4	El sorteo ha terminado y la lista de números y premios está basada en la oficial. De todas formas, recuerda que la única lista oficial es la que publica la ONLAE y deberías comprobar todos tus números contra ella.
	- t	timestamp	(fecha POSIX) de la última actualización.
	- n	Número consultado	
		Posibles valores:
	 A) <número>	 número del que se desea consultar si fue premiado o no. Debe 
	pasarse un número entero entre 0 y 84999, sin ceros a la izquierda y 
	sin puntos.
	B) resumen	 el literal "resumen" (sin comillas), dará la lista de los 
	principales números premiados (gordo, segundo, tercero, etc.)
*/

	public final static String PETICION_NUMERO_RESUMEN = "resumen";

	public final static int STATUS_SORTEO_NO_COMENZADO = 0;
	public final static int STATUS_SORTEO_COMENZADO = 1;
	public final static int STATUS_SORTEO_TERMINADO_NO_OFICIAL = 2;
	public final static int STATUS_SORTEO_TERMINADO_PDF_OFICIAL = 3;
	public final static int STATUS_SORTEO_TERMINADO_OFICIAL = 4;

	public final static int RESPUESTA_OK = 0;
	public final static int RESPUESTA_ERROR = 1;

}