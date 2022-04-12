/**  
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+	 ____
 |A|U|T|O|M|A|T|I|Z|A|C|I|O|N|	[0  0]
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+	 [##]
*@version 1.2, 11032021
*@author Fco J. Becerra OTP-OPERACIONES NIVEL I
*@updated : 12042021
*/
package es.dgt.otp.csu.envioemailencuestascalidad;

/**
* Main. Lanza un bater&iacute de emails aleatorios a para encuestas sobre
* incidencias de CSU en EasyVista
*
* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
* 
*/
public class Encuestas{
	
	static private boolean error =false;
	
	static private int numeroEmailParaEnviar =0;
	static private String ficheroIncidencias ="NO INDICADO"; //"Todas_las_Incidencias.csv";
	static private String ficheroDirecciones ="NO INDICADO"; //"Directorio.csv";
	static private ExcelIncidencias excelIncidencias;
	static final private String RUTA_Y_FICHEROXML ="xml/ConfiguracionEmail.xml";
	static private EnviarEmail enviarEmail;
	static final private int NUMPARAMETROS=3;
	
	public static void main (String [ ] args) {
		GenerarLog.setverMensajePantalla(true);
		GenerarLog.lineaLog("INFO", "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\t ____");
		GenerarLog.lineaLog("INFO", "|A|U|T|O|M|A|T|I|Z|A|C|I|O|N|\t[0  0]");
		GenerarLog.lineaLog("INFO", "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\t [##]");
		GenerarLog.lineaLog("INFO","");
		GenerarLog.lineaLog("INFO", "GENERAR LISTA EMAIL INCIDENCIAS ALEATORIOS");
		GenerarLog.lineaLog("INFO","**** GENERADOR DGT ****");
		GenerarLog.lineaLog("INFO","* <Author: OTP - OPERACIONES N1>");
		GenerarLog.lineaLog("INFO","* <Version: 1.2>");
		GenerarLog.lineaLog("INFO","* <Creada: 11032021>");
		GenerarLog.lineaLog("INFO","* <Update: 12042022>");
		GenerarLog.lineaLog("INFO","***** =========================== ****");	
		
		error =comprobarArgumentos(args);
		
		if(!error){
			excelIncidencias =new ExcelIncidencias();
			error =excelIncidencias.leerFicheroCSV(ficheroIncidencias);
		}
		
		if(!error){
			excelIncidencias.generarContenedorAletorios(numeroEmailParaEnviar);
			error =excelIncidencias.leerDireccionesEmail(ficheroDirecciones);
			//excelIncidencias.verSolicitantesParaEnvio(); //TEST
		}
		
		if(!error){
			/*El parametro true o false en el xml de configuracion
			activa el envio del email*/
			enviarEmail =new EnviarEmail();
			error =enviarEmail.estadoError();
		}
		
		if(!error){
			error =enviarEmail.cargarConfiguracionEmail(RUTA_Y_FICHEROXML);
		}
		
		if(!error){
			int contadorEmail =0;
			
			/* Las direcciones se cargar de un excel junto con las incidencias*/
			for(String direccionEmail :excelIncidencias.getDatosEmail()){
				String[] datosEnvio =direccionEmail.replaceAll("\"","").split(";");
				contadorEmail++;
				int[] contadores ={contadorEmail, excelIncidencias.getNumIncidenciasEnvio()};
				//datosEnvio[0] ="fjbecerra.altran@dgt.es"; //TEST
				if(numeroEmailParaEnviar>0){
					error=enviarEmail.generarMensajeEmail(datosEnvio,contadores);
				}
			}
			enviarEmail.verMensajesOK_KO();
		}
		
		GenerarLog.lineaLog("INFO","***** ============================ ****");	
		GenerarLog.lineaLog("INFO","***** === FIN SCRIPT ENCUESTAS === ****");	
		GenerarLog.lineaLog("INFO","***** ============================ ****");	
	}
	
	/**
	* Comprueba los par&aacutemetros de la aplicaci&oacuten
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @param parametros Argumentos pasados a al programa.
	* @return error error -> True
	*/
	public static boolean comprobarArgumentos(String[] parametros){
		boolean error =false;
		
		if(parametros.length>=NUMPARAMETROS){
			switch (parametros.length){
				case 3:
					try{
						numeroEmailParaEnviar =Integer.parseInt(parametros[0]); 						
					}catch(Exception ex){
						error =true;
						GenerarLog.lineaLog("ERROR","<EL PRIMER PARAMETRO DEBE SER EL NUMERO DE EMAIL QUE DESEA ENVIA");	
						return error;
					}
					ficheroIncidencias =parametros[1];
					ficheroDirecciones =parametros[2];
					GenerarLog.lineaLog("INFO","<NUMERO DE EMAIL PARA GENERAR> ["+numeroEmailParaEnviar+"]");
					GenerarLog.lineaLog("INFO","<FICHERO ORIGEN INCIDENCIAS / PETICIONES> ["+ficheroIncidencias+"]");
					GenerarLog.lineaLog("INFO","<FICHERO ORIGEN DIRECCIONES USUARIOS> ["+ficheroDirecciones+"]");
				break;
				default:
					GenerarLog.lineaLog("ERROR","<DEBE INTRODUCIR MINIMO TRES PARAMETROS>");
					GenerarLog.lineaLog("ERROR","\t >> NUMERO DE EMAIL PARA ENVIAR");
					GenerarLog.lineaLog("ERROR","\t >> FICHERO DE INCIDENCIAS / PETICIONES");
					GenerarLog.lineaLog("ERROR","\t >> FICHERO DIRECCIONES");
				break;
			}
		}
		else{
			error =true;
			GenerarLog.lineaLog("ERROR","<DEBE INTRODUCIR TRES PARAMETROS>");
			GenerarLog.lineaLog("ERROR","\t >> NUMERO DE EMAIL PARA ENVIAR");
			GenerarLog.lineaLog("ERROR","\t >> FICHERO DE INCIDENCIAS / PETICIONES");
			GenerarLog.lineaLog("ERROR","\t >> FICHERO DIRECCIONES");
		}

		return error;
	}
}
