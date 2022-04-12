/**  
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+	 ____
 |A|U|T|O|M|A|T|I|Z|A|C|I|O|N|	[0  0]
 +-+-+-+-+-+-+-+-+-+-+-+-+-+-+	 [##]
*@version 1, 23022021
*@author Fco J. Becerra OTP-OPERACIONES NIVEL I
*@updated : 12042022
*/
package es.dgt.otp.csu.envioemailencuestascalidad;

import java.util.ArrayList; 
import java.util.Scanner;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.Message;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import com.sun.mail.smtp.SMTPTransport;


import com.sun.mail.util.MailSSLSocketFactory;
/** Clase que genera y envia el email con la url para la encuesta.
*
* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
*/
public class EnviarEmail{

	private int mensajesOK =0;
	private int mensajesKO =0;
	
	private boolean enviarEmail =false;
	private ArrayList<String> cuerpoMensajeHTML = new ArrayList<String>();
	private boolean error =false;
	
	private Properties propertiesConfiguracionEmail = new Properties();
	
	private final String[] NOMBRESFICHEROHTMLMENSAJE ={"resources/MensajeIncidencia.html", "resources/MensajePeticion.html"};
	
	/**
	* Clase que genera y envia el email con la url para la encuesta.
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	*/
	public EnviarEmail(){
		this.error =asignaPlantillasHtml();
	}
	
	/**
	* Asignar el cuerpo de los mensajes con las plantillas
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @return error error -> True
	*
	*/
	public boolean asignaPlantillasHtml(){
		boolean error =false;
		 
		for(int i=0; i<NOMBRESFICHEROHTMLMENSAJE.length; i++){
			GenerarLog.lineaLog("INFO","<HTML COMPROBANDO> "+NOMBRESFICHEROHTMLMENSAJE[i]);
			try{			
				File ficheroHTML = new File(NOMBRESFICHEROHTMLMENSAJE[i]); 
				if (!ficheroHTML.exists()){
					GenerarLog.lineaLog("SEVERE","[ERROR] <PLANTILLA HTML NO EXISTE> "+NOMBRESFICHEROHTMLMENSAJE[i]);
				}else{
					Scanner myReader = new Scanner(ficheroHTML);
					String tmp ="";
					while (myReader.hasNextLine()) {
						tmp +=myReader.nextLine();
					}
					this.cuerpoMensajeHTML.add(tmp);
					myReader.close();
				}
			}
			catch(Exception ex){
				GenerarLog.lineaLog("SEVERE","[ERROR] <PLANTILLA HTML> "+NOMBRESFICHEROHTMLMENSAJE[i]);
				//ex.printStackTrace(); 
				this.error =true;
				return error;
			}
			
		}
		 
		return error;
		
	}
	
	/**
	* Devuelve el estado de posible error de la operaci&oacuten.
	*
	* @author Fco J. Becerra OTP-OPERACIONES NIVEL I
	* @return boolean error -> True
	*/
	public boolean estadoError(){
		return this.error;
	}
	
	/**
	* Carga el fichero de configuraci&oacuten del email.
	*
	* @author OTP-OPERACIONES NIVEL I
	* @version 1.0
	* @update 12042022
	*
	* @param configuracionEmail Fichero de configuraci&oacuten de comunicaciones.
	* @return boolean Error -> True
	*/ 
	public boolean cargarConfiguracionEmail(String configuracionEmail){
		boolean error =false;
		
		File ficheroConfiguracionEmail =new File(configuracionEmail);
		
		if(!ficheroConfiguracionEmail.exists()){
			error =true;
			GenerarLog.lineaLog("SEVERE","[ERROR] <FICHERO CONFIGURACION EMAIL> "+ficheroConfiguracionEmail);
		}
		else{
			if(ficheroConfiguracionEmail.length()==0){
				error =true;
				GenerarLog.lineaLog("SEVERE","[ERROR] <FICHERO CONFIGURACION EMAIL VACIO>");
			}
		}
		
		if(!error){
			try{
				DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder =dbf.newDocumentBuilder();
				Document document =documentBuilder.parse(configuracionEmail);
				
				//Obtener el elemento raíz del documento
				//Element raiz = document.getDocumentElement();
				//Elimina nodos vacíos y combina adyacentes en caso de que los hubiera
				document.getDocumentElement().normalize();
				
				NodeList listaElementos;
				//Almacenar los nodos
				listaElementos =document.getElementsByTagName("datosConexion");
				
				//Recorrer los nodos
				for(int i = 0; i <listaElementos.getLength(); i++) {
					
					Node nodeTemp = listaElementos.item(i);
					Element eElement = (Element) nodeTemp;
					
					//¿Nodo?
					if(nodeTemp.getNodeType()==Node.ELEMENT_NODE && eElement.getAttribute("Elemento").equals("Datos conexion"))
					{
						this.propertiesConfiguracionEmail.put("mail.smtp.from", eElement.getElementsByTagName("mail.smtp.from").item(0).getTextContent());
						this.propertiesConfiguracionEmail.put("mail.smtp.proxy.password", eElement.getElementsByTagName("mail.smtp.proxy.password").item(0).getTextContent());
						this.propertiesConfiguracionEmail.put("mail.smtp.host", eElement.getElementsByTagName("mail.smtp.host").item(0).getTextContent());
						this.propertiesConfiguracionEmail.put("mail.smtp.port", eElement.getElementsByTagName("mail.smtp.port").item(0).getTextContent());
						this.propertiesConfiguracionEmail.put("mail.smtp.connectiontimeout", eElement.getElementsByTagName("mail.smtp.connectiontimeout").item(0).getTextContent());
						this.propertiesConfiguracionEmail.put("mail.smtp.timeout", eElement.getElementsByTagName("mail.smtp.timeout").item(0).getTextContent());
						this.propertiesConfiguracionEmail.put("mail.smtp.auth", eElement.getElementsByTagName("mail.smtp.auth").item(0).getTextContent());
						this.propertiesConfiguracionEmail.put("mail.smtp.ssl.enable", eElement.getElementsByTagName("mail.smtp.ssl.enable").item(0).getTextContent());
						this.propertiesConfiguracionEmail.put("mail.smtp.ssl.protocols", eElement.getElementsByTagName("mail.smtp.ssl.protocols").item(0).getTextContent());
						//Update 11042022
						this.propertiesConfiguracionEmail.put("enviarEmails", eElement.getElementsByTagName("enviarEmails").item(0).getTextContent());
					}
				}//fin-for
			}catch(Exception ex){
				error =true;
				GenerarLog.lineaLog("SEVERE","[ERROR] <GENERANDO CONFIGURACION EMAIL>");
				//ex.printStackTrace(); //Test
				return error;
			}
		}
		return error;
	}
	
	/**
	* Genera y envia el email para cada usuario
	* @author OTP-OPERACIONES NIVEL I
	* @version 1.0
	* @param datosEnvio Datos del usuario para el envio
	* @param contadores Contador de envios realizados
	*/
	public boolean generarMensajeEmail(String[] datosEnvio, int[] contadores){
		boolean error =false;
		Session session =null;
		MimeMessage message =null;
		
		try{
			GenerarLog.lineaLog("INFO","<ENVIANDO EMAIL AUTOMATICO> ["+contadores[0]+"/"+contadores[1]+"]");
			
			try{
				MailSSLSocketFactory sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);
				this.propertiesConfiguracionEmail.put("mail.smtp.ssl.socketFactory", sf);
			}catch(Exception ex){
				error =true;
				GenerarLog.lineaLog("SEVERE","\t[ERROR] MailSSLSocketFactory>");
				// ex.printStackTrace(); //Test
			}
			
			session = Session.getInstance(this.propertiesConfiguracionEmail, null);
			
		}catch(Exception ex){
			error =true;
			GenerarLog.lineaLog("SEVERE","[ERROR] <CONFIGURACION EMAIL>");
			// ex.printStackTrace(); //Test
		}
		
		if(!error){ //Validar direccion from
			try {
				InternetAddress emailAddr = new InternetAddress(this.propertiesConfiguracionEmail.getProperty("mail.smtp.from"));
				emailAddr.validate();
			} catch (AddressException ex) {
				error = true;
				GenerarLog.lineaLog("SEVERE","\t[ERROR] <DIRECCION FROM EMAIL> "+this.propertiesConfiguracionEmail.getProperty("mail.smtp.from"));
			}
		}
		
		if(!error){ //Validad direcciones de to
			try {
				InternetAddress emailAddr = new InternetAddress(datosEnvio[0]);
				emailAddr.validate();
				GenerarLog.lineaLog("INFO","\t\t[TO] "+datosEnvio[0]);				
				GenerarLog.lineaLog("INFO","\t\t[INCIDENCIA] "+datosEnvio[1]+" - "+datosEnvio[2]);	
			} catch (AddressException ex) {
				error =true;
				GenerarLog.lineaLog("SEVERE","\t[ERROR] <DIRECCION TO EMAIL> "+datosEnvio[0]);
			}
		}
		
		if(!error){	//Crear el mensaje
			try{
				message = new MimeMessage(session);
				message.setFrom(new InternetAddress(this.propertiesConfiguracionEmail.getProperty("mail.smtp.from")));
				message.addRecipients(Message.RecipientType.TO,datosEnvio[0]);								
			}catch(Exception ex){
				GenerarLog.lineaLog("SEVERE","\t[ERROR] <ASIGNANDO DIRECCIONES EMAIL> ");
				error =true;
			}
		}
		
		if(!error)
		{	
			try{
				String asunto ="Encuesta de calidad CSU - ";
				if(datosEnvio[1].indexOf("I")!=-1){
					asunto +="Incidencia";
				}else{
					asunto +="Petici\u00f3n";
				}
				asunto +=" ["+datosEnvio[1]+"] - "+datosEnvio[2];
				message.setSubject(asunto,"utf-8");
				
				//Cuerpo del mensaje
				MimeMultipart multipart = new MimeMultipart();
				//Convertir cuerpo en html
				final MimeBodyPart  messageBodyPart = new MimeBodyPart();
				
				//Comprobar si es incidencia 'I' o peticion 'S'
				if(datosEnvio[1].indexOf("I")!=-1){
					messageBodyPart.setContent(this.cuerpoMensajeHTML.get(0), "text/html;charset=UTF-8");
				}else{
					messageBodyPart.setContent(this.cuerpoMensajeHTML.get(1), "text/html;charset=UTF-8");
				}
					
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart); //Asignar el contenido
				
				this.enviarEmail =Boolean.parseBoolean(this.propertiesConfiguracionEmail.getProperty("enviarEmails"));				

				if(this.enviarEmail)
				{
					GenerarLog.lineaLog("INFO","\t <ENVIANDO EMAIL> ");
					SMTPTransport t = (SMTPTransport) session.getTransport("smtp"); 
					t.connect(this.propertiesConfiguracionEmail.getProperty("mail.smtp.host"), this.propertiesConfiguracionEmail.getProperty("mail.smtp.from"), this.propertiesConfiguracionEmail.getProperty("mail.smtp.proxy.password"));
					t.sendMessage(message, message.getAllRecipients());
					GenerarLog.lineaLog("INFO","\t <--EMAIL ENVIADO--> ");								
					t.close();
					mensajesOK++;
				}
				else{
					GenerarLog.lineaLog("INFO","<ENVIAR EMAIL DESACTIVADO> ");
					mensajesKO++;
				}
						
			}catch(Exception ex){
				mensajesKO++;
				GenerarLog.lineaLog("SEVERE","\t[ERROR] <ENVIANDO EMAIL> ");
				// TEST ex.printStackTrace();
			}
		}//fin-if
		
		return error;
	}
	
	/**
	* Mostrar informaci&oacuten sobre los env&iacuteos de emails
	* @author OTP-OPERACIONES NIVEL I
	* @version 1.0
	*/
	public void verMensajesOK_KO(){
		GenerarLog.lineaLog("INFO","<EMAILS ENVIADOS> ["+this.mensajesOK+"]");
		GenerarLog.lineaLog("INFO","<EMAILS NO ENVIADOS> ["+this.mensajesKO+"]");
	}
	
}
