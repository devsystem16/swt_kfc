package kfc.com.modelo
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import sun.security.util.Length

import java.io.IOException;
import java.net.InetAddress;

public class ValidadorDispositivos {

 
	RequerimientoAutorizacion requerimiento ;

 
	public static  boolean DispositivoConectadoPuertoSerial(String puertoSerial) {

		try {

			Enumeration portList = CommPortIdentifier.getPortIdentifiers();

			while (portList.hasMoreElements()) {

				CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
				if (portId.getPortType() == CommPortIdentifier.PORT_PARALLEL) {
					if (portId.getName().equals(puertoSerial))
						return true
				} else {
					if (portId.getName().equals(puertoSerial))
						return true
				}
			}

			return false
		} catch (Exception e) {
			return false
		}
	}

	public static boolean DispositivoConectadoIP (String ip) {

		try {
			InetAddress ping = InetAddress.getByName(ip);
			if (ping.isReachable(2000)) {
				return true
			} else {
				return false
			}
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}



	public static String tcpSend (  String trm , int logitud) {
 
		String Keycomponente =  GeneradorClave.get_Key_ramdom()  // Se generó aleatoriamente..
		String keyIzquierda = Propiedades.get(Constantes.ARCHIVO_CONFIGURACION_DINAMIC, Constantes.SEGURIDAD_LLAVE_IZQUIERDA)
		String keyDerecha = Propiedades.get(Constantes.ARCHIVO_CONFIGURACION_DINAMIC, Constantes.SEGURIDAD_LLAVE_DERECHA)

		String CriptogramaC = GeneradorClave.cifrar(keyIzquierda, Keycomponente, keyDerecha).toUpperCase()
		int talla = (trm + Keycomponente + CriptogramaC).length()

		String trama =   GeneradorClave.getTalla(talla) + trm + Keycomponente + CriptogramaC
 
		String tcp_Server_port  =  Propiedades.get(Constantes.ARCHIVO_CONFIGURACION_DINAMIC, Constantes.TCP_DIRECCION_DISPOSITIVO)
		String respuesta =""

		println "REQUEST :" +trama
		 
		 String ipServer = "192.168.100.31"// tcp_Server_port.split(":")[0]
		 int puerto =  9999 //Integer.parseInt( tcp_Server_port.split(":")[1])

		// String ipServer =   "192.168.100.8"//  tcp_Server_port.split(":")[0]
	   	//int puerto = 6500 // Integer.parseInt( tcp_Server_port.split(":")[1])

//println   tcp_Server_port 
		Socket sc;
		DataOutputStream mensaje=null;
		DataInputStream entrada=null;
		
		println "la longitud es :::>>> " + logitud
		int leido=logitud;
		byte [] datosBytes   =new byte[logitud];
		sc = new Socket( ipServer , puerto );
		mensaje = new DataOutputStream(sc.getOutputStream());
 
		mensaje.write((trama).getBytes());

		System.out.println("Esperando una respuesta:");
		entrada = new DataInputStream(sc.getInputStream());
		try
		{
			int i=1;
			String cadenaRespuesta="";
			int longitudCadena=0;
			leido=entrada.read(datosBytes,0,leido)
			while ((leido)!= -1)
			{

				if (leido>0)
				{
					cadenaRespuesta=cadenaRespuesta+new String(datosBytes);
					longitudCadena=cadenaRespuesta.length();
 
					
					if(longitudCadena >= trama.length())
					{
						break;
					}

				}
				else
				{
					entrada=null;
					sc.close();
					break;
				}
			}
			
			
			respuesta = cadenaRespuesta
			System.out.println("CADENA DE RESPUESTA: "+cadenaRespuesta);
		}
		catch (EOFException e)
		{

		}
		sc.close();

		return respuesta  
	}


	public static String tcpSend3(  String mensaje) {
		
		 
				String trama = mensaje
		
				String tcp_Server_port  = Propiedades.get(Constantes.ARCHIVO_CONFIGURACION_DINAMIC, Constantes.TCP_DIRECCION_DISPOSITIVO)
				String respuesta =""
				Socket socket;
				try
				{
					String ipServer = tcp_Server_port.split(":")[0]
					int puerto = Integer.parseInt( tcp_Server_port.split(":")[1])
		
					socket = new Socket( ipServer  ,puerto );
		
					//Send the message to the server
					OutputStream os = socket.getOutputStream();
					OutputStreamWriter osw = new OutputStreamWriter(os );
					BufferedWriter bw = new BufferedWriter(osw);
		

					bw.write(trama);
					bw.flush();
		
		
					System.out.println("Trama enviada al servidor. : "+trama);
		
		
		
					//Get the return message from the server
					InputStream is = socket.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
		
					//			println br.read()
					//		    println  br.ready()
		
		
					String message =  br.readLine()
		
		
					respuesta = message
					//System.out.println("Message received from the server : " +message);
		
				}
				catch (Exception exception)
				{
					exception.printStackTrace();
		
				}
				finally
				{
					//Closing the socket
					try
					{
						socket.close();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
		
				return respuesta
			}
		

	public static String tcpSend2(  String mensaje) {

		String Keycomponente =  GeneradorClave.get_Key_ramdom()  // Se generó aleatoriamente..
		String keyIzquierda = Propiedades.get(Constantes.ARCHIVO_CONFIGURACION_DINAMIC, Constantes.SEGURIDAD_LLAVE_IZQUIERDA)
		String keyDerecha = Propiedades.get(Constantes.ARCHIVO_CONFIGURACION_DINAMIC, Constantes.SEGURIDAD_LLAVE_DERECHA)

		String CriptogramaC = GeneradorClave.cifrar(keyIzquierda, Keycomponente, keyDerecha).toUpperCase()
		int talla = (mensaje + Keycomponente + CriptogramaC).length()

		String trama =  GeneradorClave.getTalla(talla) + mensaje + Keycomponente + CriptogramaC

		String tcp_Server_port  = Propiedades.get(Constantes.ARCHIVO_CONFIGURACION_DINAMIC, Constantes.TCP_DIRECCION_DISPOSITIVO)
		String respuesta =""
		Socket socket;
		try
		{
			String ipServer = tcp_Server_port.split(":")[0]
			int puerto = Integer.parseInt( tcp_Server_port.split(":")[1])

			socket = new Socket( ipServer  ,puerto );

			//Send the message to the server
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os );
			BufferedWriter bw = new BufferedWriter(osw);


			bw.write(trama);
			bw.flush();


			System.out.println("Trama enviada al servidor. : "+trama);



			//Get the return message from the server
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			//			println br.read()
			//		    println  br.ready()


			String message =  br.readLine()


			respuesta = message
			//System.out.println("Message received from the server : " +message);

		}
		catch (Exception exception)
		{
			exception.printStackTrace();

		}
		finally
		{
			//Closing the socket
			try
			{
				socket.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		return respuesta
	}

	public static String enviarConSocket (String Trama, String ipServer , String puerto) {

		String mensaje =""

		//Host del servidor
		final String HOST = ipServer;
		//Puerto del servidor
		final int PUERTO = Integer.parseInt( puerto);
		DataInputStream ins;
		DataOutputStream out;

		try {
			//Creo el socket para conectarme con el cliente
			Socket sc = new Socket(HOST, PUERTO);

			ins = new DataInputStream(sc.getInputStream());
			out = new DataOutputStream(sc.getOutputStream());

			//Envio un mensaje al cliente
			out.writeUTF(Trama)
			out.flush()


			//Recibo el mensaje del servidor
			mensaje = ins.readUTF();

			//System.out.println(mensaje);

			sc.close();

		} catch (IOException ex) {

		}


		return mensaje
	}


	
}
