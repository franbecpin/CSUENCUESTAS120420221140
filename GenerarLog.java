/**  
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+	 ____
 |A|U|T|O|M|A|T|I|Z|A|C|I|O|N|	[0  0]
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+	 [##]
*@version 1, 23022021
*@author Fco J. Becerra OTP-OPERACIONES NIVEL I
*@updated : 11032021
*/
package es.dgt.otp.csu.envioemailencuestascalidad;

/*
OFF: este es el nivel de mínimo detalle, deshabilita todos los logs.
FATAL: se utiliza para mensajes críticos del sistema, generalmente después de guardar el mensaje el programa abortará.
ERROR: se utiliza en mensajes de error de la aplicación que se desea guardar, estos eventos afectan al programa pero lo dejan seguir funcionando, como por ejemplo que algún parámetro de configuración no es correcto y se carga el parámetro por defecto.
WARN: se utiliza para mensajes de alerta sobre eventos que se desea mantener constancia, pero que no afectan al correcto funcionamiento del programa.
INFO: se utiliza para mensajes similares al modo "verbose" en otras aplicaciones.
DEBUG: se utiliza para escribir mensajes de depuración. Este nivel no debe estar activado cuando la aplicación se encuentre en producción.
TRACE: se utiliza para mostrar mensajes con un mayor nivel de detalle que debug.
ALL: este es el nivel de máximo detalle, habilita todos los logs (en general equivale a TRACE).
*/

/**
* Generar el log por pantalla de la operaci&oacuten
* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
*/
public final class GenerarLog{
	
	static private boolean verMensajePantalla =false;
	
	/**
	* Imprime la l&iacutenea de log por pantalla
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @param nivelMensaje Nivel del mensaje
	* @param mensaje Mensaje que muestra el log
	*/
	public static void lineaLog(String nivelMensaje, String mensaje){
		if (verMensajePantalla){
			System.out.println("["+nivelMensaje+"] "+mensaje);
		}
	}
	
	/**
	* Activa / desactiva la visualizaci&oacuten del log en pantalla
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @param verPantalla Activar/desactiva
	*/
	public static void setverMensajePantalla(boolean verPantalla){
		verMensajePantalla =verPantalla;
	}
	
}