/**  
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+	 ____
 |A|U|T|O|M|A|T|I|Z|A|C|I|O|N|	[0  0]
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+	 [##]
* @version 1, 23022021
* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
* @updated : 12042022
*/
package es.dgt.otp.csu.envioemailencuestascalidad;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;

import java.util.Random;

import java.io.*;
import java.util.HashMap;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.ObjectUtils;


/**
* Extraer las incidencias.
*
* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
*/
public final class ExcelIncidencias{

	private final String SEPARADOR ="\\;";
	private String INICIOLINEA ="";
	
	private HashMap<String, String> tiposIncidencias = new HashMap<String, String>();
	private HashMap<String, String> tiposPeticiones = new HashMap<String, String>();
			
	private LinkedList<RegistroIncidencia> contenedorIncidenciasFiltradas =new LinkedList<RegistroIncidencia>();
	private LinkedList<RegistroIncidencia> contenedorIncidenciasAleatorio =new LinkedList<RegistroIncidencia>();
	private HashSet<String> datosEmail =new HashSet<String>();

	/**
	* Contiene la informaci&oacuten asociada a cada incidencia.
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	*/
	public class RegistroIncidencia{
		//Campos extraidos + email (que rellenera desde BBDD)
		private String incidencia;
		private String estado;
		private String solicitante;
		private String departamento;
		private String email;
		private String tipoIncidencia;
		
		/**
		* Contiene la informaci&oacuten asociada a cada incidencia.
		*
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		*/
		public RegistroIncidencia(){};
		
		/**
		* Contiene la informaci&oacuten asociada a cada incidencia.
		*
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @param campos Valor de los campos aceptados
		*/
		public RegistroIncidencia(String[] campos){
			this.incidencia =campos[0];
			this.estado =campos[1];
			this.solicitante =campos[2];
			this.departamento =campos[3];
			this.email ="";
			this.tipoIncidencia =campos[4];
		}
		
		/**
		* Devuelve el c&oacutedigo de la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @return incidencia C&oacutedigo de la incidencia
		*/
		public String getIncidencia(){ return this.incidencia;}
		
		/**
		* Devuelve el Estado de la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @return estado Estado de la incidencia
		*/
		public String getEstado(){ return this.estado;}
		
		/**
		* Devuelve el Solicitante de la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @return solicitante Solicitante de la incidencia
		*/
		public String getSolicitante(){ return this.solicitante;}
		
		/**
		* Devuelve el departamento de la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @return departamento Departamento de la incidencia
		*/
		public String getDepartamento(){ return this.departamento;}
		
		/**
		* Devuelve el email del solicitante de la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @return email Email del solicitante de la incidencia
		*/
		public String getEmail(){ return this.email;}
		
		/**
		* Devuelve el tipo de la incidencia asignado por CSU
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @return tipoIncidencia Tipo asignado por CSU para la incidencia
		*/
		public String getTipoIncidencia(){ return this.tipoIncidencia;}
		
		/**
		* Asigna el c&ocutedigo de la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @param incidencia C&oacutedigo de la incidencia
		*/
		public void setIncidencia(String incidencia){ this.incidencia =incidencia;}
		
		/**
		* Asigna el estado de la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @param estado Estado de la incidencia
		*/
		public void setEstado(String estado){ this.estado =estado;}
		
		/**
		* Asigna el solicitante de la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @param solicitante Solicitante de la incidencia
		*/
		public void setSolicitante(String solicitante){ this.solicitante =solicitante;}
		
		/**
		* Asigna el departamento de la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @param departamento Departamento de la incidencia
		*/
		public void setDepartamento(String departamento){ this.departamento =departamento;}
		
		/**
		* Asigna el email del solicitante de la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @param email Email del solicitante de la incidencia
		*/
		public void setEmail(String email){ this.email =email;}
		
		/**
		* Asigna el tipo asignado por CSU a la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @param tipoIncidencia Tipo asignado por CSU para la incidencia
		*/
		public void setTipoIncidencia(String tipoIncidencia){ this.tipoIncidencia =tipoIncidencia;}
		
		/**
		* Asigna el tipo asignado por CSU a la incidencia
		* 
		* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
		* @return datos incidencia;estado;departamento;solicitante;email;TipoIncidencia
		*/
		public String toString(){
			return getIncidencia()+";"+getEstado()+";"+getDepartamento()+";"+getSolicitante()+";"+getEmail()+";"+getTipoIncidencia();
		}
	}
	
	
	/* Campos de informacion para el email 
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I 
	*/
	private ArrayList<Integer> camposTratados =new ArrayList<Integer>(){
		{
			add(0); 	//num_Incidencia
			add(2); 	//estado
			add(19); 	//solicitante
			add(23); 	//departamento
			add(26); 	//tipoIncidencia
		}
	};
			
	
	
	/**
	* Constructor 
	* Asigna el fichero origen de incidencias / peticiones de CSU
	* 
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	*/
	public ExcelIncidencias(){
		inicializarTiposIncidencias();
		incializarTiposPeticiones();
	}	
	

	/**
	* Lee las l&iacuteneas del fichero de incidencias / peticiones y carga los campos
	* necesarios para la informaci&oacuten del email.
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @param nombreArchivoIncidencias Nombre del archivo de origen incidencias para email
	* @return boolean error -> true
	*/
	public boolean leerFicheroCSV(String nombreArchivoIncidencias)
	{
		boolean error =false;
		int lineas =0;
		int lineasReales =0;
		int lineasVIP =0;
		int lineasNoCerradas =0;
		File archivoIncidencias =null;
		
		try{
			
			archivoIncidencias =new File(nombreArchivoIncidencias);
			if(archivoIncidencias.isFile() && archivoIncidencias.exists()) {
				GenerarLog.lineaLog("INFO","<ENCONTRADO FICHERO> \t"+archivoIncidencias);
				GenerarLog.lineaLog("INFO","<ARCHIVO> \t\t"+archivoIncidencias.getName());
				GenerarLog.lineaLog("INFO","<CONTENIDO> " +archivoIncidencias.length());
			} 
			else{
				GenerarLog.lineaLog("ERROR","[ERROR] <NO ENCONTRADO FICHERO> "+archivoIncidencias);
				error =true;
			}
		}catch(Exception ex){
			error =true;
			GenerarLog.lineaLog("ERROR","[ERROR] <TRATANDO FICHERO ENTRADA> "+archivoIncidencias);
			//ex.printStackTrace(); //Test
			return error;
		}
		
		if(!error)
		{
			try
			{
				/* UPDATE 08/04/2022 */
				/* Abrir el fichero */
				FileReader fr = new FileReader(nombreArchivoIncidencias);
				CSVReader reader = new CSVReader(fr);
				String[] nextLine;

				/* POSICIONES DE CAMPOS EN CADA LINEA
				* [0] -> CODIGO INCIDENCIA
				* [LONGITUD - 10] -> Solicitante Vip
				* [LONGITUD - 11] -> Nombre Solicitante
				* Se toma desde el final de la linea para evitar problemas con las
				* posiciones de los campos consecuencia del valor del
				* texto que aparece en el campo "Descripcion", que puede contener 
				* correos electronicos completos con "; or \n o lineas en blanco"
				*/
				final int POS_CODINCIDENCIA =0;
				final int POS_TIPO =4;
				final int POS_DEPARTAMENTO =7;
				final int POS_SOLICITANTEVIP =10;
				final int POS_SOLICITANTE =11;
				String[] lineaActual;

				GenerarLog.lineaLog("INFO","<TRATANDO LINEAS> ");
					
				while ((nextLine = reader.readNext()) != null) 
				{
					// La linea 0 es de cabeceras
					if(lineas>0){
						for (String e : nextLine) 
						{
							lineaActual =e.split(this.SEPARADOR); // Campos
							//Asignar el prefijo de incidencias 
							if(lineas==1) { asignarValorInicioLinea(lineaActual[0]);} 
							
							//Controlamos que sea un codigo de incidencia valido
							if(lineaActual[0].substring(0,3).equals(this.INICIOLINEA))
							{
								lineasReales++;
								
								//No contemplan incidencias VIP o VVIP
								if(lineaActual[lineaActual.length-POS_SOLICITANTEVIP].contains("VIP")){
									lineasVIP++;
								}else
								{ //Linea se usuario normal
									//Solo se aceptan las incidencias cerradas
									
									if(lineaActual[2].toUpperCase().replaceAll("\"","").trim().equals("CERRADO"))
									{
										RegistroIncidencia registroIncidencia =new RegistroIncidencia();
										registroIncidencia.setIncidencia(lineaActual[0]);
										registroIncidencia.setEstado(lineaActual[2]);
										registroIncidencia.setSolicitante(lineaActual[lineaActual.length-POS_SOLICITANTE]);
										registroIncidencia.setDepartamento(lineaActual[lineaActual.length-POS_DEPARTAMENTO]);
										
										registroIncidencia.setTipoIncidencia(extraerTipoReferenciaCampo(lineaActual[lineaActual.length-POS_TIPO]));
										this.contenedorIncidenciasFiltradas.add(registroIncidencia);
										lineasReales++;
									}else{
										lineasNoCerradas++;
									}
								}	
							}
						}
					} //fin-for campos
					
					lineas++;

				}//fin-while
					
				/* ESTADISTICA */
				GenerarLog.lineaLog("INFO","\t <LINEAS TRATADAS> \t["+lineas+"]");
				GenerarLog.lineaLog("INFO","\t <LINEAS VIP EXCLUIDAS> \t["+lineasVIP+"]");
				GenerarLog.lineaLog("INFO","\t <LINEAS INCIDENCIA NO CERRADAS> \t["+lineasNoCerradas+"]");
				GenerarLog.lineaLog("INFO","\t <LINEAS TRATADAS> \t["+lineasReales+"]");
				
			} catch (Exception e) 
			{
				error =true;
				GenerarLog.lineaLog("ERROR","[ERROR] <TRATANDO LINEAS ENTRADA>");
				e.printStackTrace(); //Test
			}
			
		}//fin-if
		return error;	
	}
		
	/**
	* Extraer el tipo de incidencia asignado por CSU
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @return tipoIncidencia Valor filtrado del tipo
	*/
	public String extraerTipoReferenciaCampo(String campo){
		String tipoIncidencia =campo;
		boolean tipoEncontrado =false;
		
		for (String tipo : this.tiposIncidencias.keySet()){
			if(campo.indexOf(tipo)>0){
				tipoIncidencia =this.tiposIncidencias.get(tipo);
				tipoEncontrado =true;
			}			
		}
		
		if(!tipoEncontrado){
			//Comprobar para peticiones
			for (String tipo : this.tiposPeticiones.keySet()){
				if(campo.indexOf(tipo)>0){
					tipoIncidencia =this.tiposPeticiones.get(tipo);
					tipoEncontrado =true;
				}			
			}
		}
		
		return tipoIncidencia;
	}
		
	/**
	* Asigna el valor inicial de los cuatro primero caracteres de la l&iacutenea 
	* de la incidencia de este modo se comprueba que es una incidencia o una 
	* petici&oacuten y se ignorar las l&iacuteneas no iniciales
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @param campo Campo de la l&iacutenea
	*/
	public void asignarValorInicioLinea(String campo){
		this.INICIOLINEA =campo.substring(0,3);	
		//System.out.println("[TEST] this.INICIOLINEA]: "+campo);	
		/*Comprobar que es el campo inicial
		Una codificación Incidencias de ejemplo es:
		I210101_000001  
		I210104_000002
		
		Una codificación Peticiones de ejemplo es:
		S210301_000002
		S210301_000003

		Se tomara como definidor de nueva linea I+dos dígitos (que indican el ano)
		*/
	}
	
	/**
	* Rellenar el contenedor con el n&uacutemero de incidencias incidado.
	* Las incidencias se seleccionan de manera aleatoria de entre todas las importadas.
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @param limite Limite de emails que queremos generar
	*/
	public void generarContenedorAletorios(int limite){
		int LIMITE =0;
		
		if(this.contenedorIncidenciasFiltradas.size()>0){
			//GenerarLog.lineaLog("INFO","<LINEAS ACEPTADAS> \t["+this.contenedorIncidenciasFiltradas.size()+"]");
			if(this.contenedorIncidenciasFiltradas.size()>limite){
				GenerarLog.lineaLog("INFO","<GENERANDO INCIDENCIAS> \t["+limite+"]");
				LIMITE =limite;
			}else{
				GenerarLog.lineaLog("INFO","<GENERANDO INCIDENCIAS> \t["+this.contenedorIncidenciasFiltradas.size()+"]");
				LIMITE =this.contenedorIncidenciasFiltradas.size();
			}
			
			Random number = new Random(123L);
			for (int i = 0; i < LIMITE; i++) {
			  //Generar entero Random entre [0, TOTALINCIDENCIAS]
			  int n = number.nextInt(this.contenedorIncidenciasFiltradas.size());
			  RegistroIncidencia registroIncidencia =this.contenedorIncidenciasFiltradas.get(n);
			  this.contenedorIncidenciasAleatorio.add(registroIncidencia);
			}
			GenerarLog.lineaLog("INFO","<INCIDENCIAS ALEATORIAS> ["+this.contenedorIncidenciasAleatorio.size()+"]");
		}
	}
	
	/**
	* Leer las direcciones de emails de los usuarios que han tenido incidencias / peticiones.
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @param nombreArchivoDirecciones Nombre del fichero CSV de las incidencias / Peticiones
	* @return boolean error -> true
	*/
	public boolean leerDireccionesEmail(String nombreArchivoDirecciones){
		boolean error =false;
		
		int lineas =0;
		int lineasReales =0;
		int numDireccionesEncontradas =0;
		File archivoDireccionesEmail =null;
		
		try{
			archivoDireccionesEmail =new File(nombreArchivoDirecciones);
			
			BufferedReader lector = new BufferedReader(new FileReader(archivoDireccionesEmail)); 
			if(archivoDireccionesEmail.exists()){
				GenerarLog.lineaLog("INFO","<ARCHIVO DIRECCIONES EMAIL> \t"+archivoDireccionesEmail.getName());
				GenerarLog.lineaLog("INFO","<CONTENIDO> " + archivoDireccionesEmail.length());
				GenerarLog.lineaLog("INFO","<TRATANDO DIRECCIONES> ");
				
				String lineaLeida =null; 
				while ((lineaLeida = lector.readLine()) != null) {
					//Ignorar la primera linea de cabecera
					if(lineas>=1){
						lineasReales++;
												
						for(int i=0; i<this.contenedorIncidenciasAleatorio.size(); i++){
							RegistroIncidencia incidencia =this.contenedorIncidenciasAleatorio.get(i);
							String[] lineaActual =lineaLeida.split(this.SEPARADOR);
							if(incidencia.getSolicitante().equals(lineaActual[0])){
								numDireccionesEncontradas++;
								incidencia.setEmail(lineaActual[2]);
								this.contenedorIncidenciasAleatorio.set(i, incidencia);
							}
						}
					}
					lineas++;
				} 
				
				GenerarLog.lineaLog("INFO","\t <LINEAS TRATADAS> \t["+lineasReales+"]");
			}
		} catch (Exception e) {
			error =true;
			GenerarLog.lineaLog("ERROR","[ERROR] <TRATANDO DIRECCIONES EMAIL>");
			//e.printStackTrace(); //Test
			return error;
		}
		
		GenerarLog.lineaLog("INFO","\t <DIRECCIONES BUSCADAS> \t["+this.contenedorIncidenciasAleatorio.size()+"]");
		GenerarLog.lineaLog("INFO","\t <DIRECCIONES ENCONTRADAS> \t["+numDireccionesEncontradas+"]");
		GenerarLog.lineaLog("INFO","\t <DIRECCIONES TRATADAS> \t["+lineas+"]");
		//verSolicitantesParaEnvio();//TEST
		return error;
			
	}
	
	/**
	* Muestra los solicitantes para el env&iacuteo de los emails
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	*/
	public void verSolicitantesParaEnvio(){
		for(RegistroIncidencia incidencia: this.contenedorIncidenciasAleatorio){
			System.out.println("Solicitante para envio: "+incidencia);
		}
	}
	
	/**
	* Devuelve los datos para el email
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @return HashSet<String> Contenedor de los datos para el email
	*/
	public HashSet<String> getDatosEmail(){
		this.datosEmail.clear();
		
		for(RegistroIncidencia incidencia: this.contenedorIncidenciasAleatorio){
			if(incidencia.getEmail()!=null){
				
				this.datosEmail.add(incidencia.getEmail()+";"+incidencia.getIncidencia()+";"+incidencia.getTipoIncidencia());
			}
		}
		
		GenerarLog.lineaLog("INFO","<EMAIL PARA ENVIO> ["+this.datosEmail.size()+"]");
		
		return this.datosEmail;
	}
	
	/**
	* Devuelve el n&uacutemero de emails generados
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @return int N&uacutemero de emails generados.
	*/
	public int getNumIncidenciasEnvio(){
		return datosEmail.size();
	}
	
	/**
	* Inicializa los tipos de incidencias.
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I 
	*/
	public void inicializarTiposIncidencias(){
		this.tiposIncidencias.put("Aplicaciones corporativas", "Aplicaciones corporativas");
		this.tiposIncidencias.put("Comunicaciones", "Comunicaciones");
		this.tiposIncidencias.put("Degradación", "Degradaci\u00f3n");
		this.tiposIncidencias.put("Error Acceso", "Error Acceso");
		this.tiposIncidencias.put("Error Datos", "Error Datos");
		this.tiposIncidencias.put("Error Funcional", "Error Funcional");
		this.tiposIncidencias.put("Error Move-Padrón-Matraba", "Error Move-Padron-Matraba");
		this.tiposIncidencias.put("Gestión de usuarios", "Gesti\u00f3n de usuarios");
		this.tiposIncidencias.put("Hardware", "Hardware");
		this.tiposIncidencias.put("Pérdida de Servicio", "P\u00e9rdida de Servicio");
		this.tiposIncidencias.put("Seguridad", "Seguridad");
		this.tiposIncidencias.put("Software", "Software");
	}
	
	/**
	* Inicializa los tipos de peticiones.
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I 
	*/
	public void incializarTiposPeticiones(){
		this.tiposPeticiones.put("Aplicaciones corporativas", "Aplicaciones corporativas");
		this.tiposPeticiones.put("Comunicaciones", "Comunicaciones");
		this.tiposPeticiones.put("Consulta", "Consulta");
		this.tiposPeticiones.put("Gestión de usuarios", "Gesti\u00f3n de usuarios");
		this.tiposPeticiones.put("Hardware", "Hardware");
		this.tiposPeticiones.put("Integración de Terceros", "Integraci\u00f3n de Terceros");
		this.tiposPeticiones.put("Software", "Software");
		this.tiposPeticiones.put("Sugerencia/Queja", "Sugerencia/Queja");
		this.tiposPeticiones.put("T. Petición de Proyecto Interno CSU", "T. Petici\u00f3n de Proyecto Interno CSU");
	}
	
	/**
	* Comprueba si un tipo de incidencia existe como tipo prefijado.
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @param tipoIncidencia Tipo de incidencia encontrado en el archivo CSV
	* @return boolean encontrado -> true
	*/
	public boolean existeTipoIncidencia(String tipoIncidencia){
		boolean encontrado =false;
		
		return encontrado;
	}
	
	/**
	* Comprueba si un tipo de petici&oacuten existe como tipo prefijado.
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @param tipoIncidencia Tipo de petici&oacuten encontrado en el archivo CSV
	* @return boolean encontrado -> true
	*/
	public boolean existeTipoPeticion(String tipoIncidencia){
		boolean encontrado =false;
		
		return encontrado;
	}	
	
}
