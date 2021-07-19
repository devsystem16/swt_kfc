package kfc.com.modelo

import java.lang.reflect.InvocationTargetException

import com.kfc.conexion.ConexionSqlServer
import com.kfc.modelo.reflexion.JarLector

import groovyjarjarantlr4.v4.misc.EscapeSequenceParsing

import java.time.Duration;
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



class Tarjetas {

	int exito
	ConexionSqlServer ocnn
	ColaProcesos cola

	Tarjetas () {}
	private static Tarjetas instance = null;

	public static  Tarjetas getInstance( ColaProcesos cola, ConexionSqlServer ocnn) {
		if (instance == null) {
			instance = new Tarjetas( cola ,  ocnn)
		}
		instance
	}


	public Tarjetas(  ColaProcesos cola, ConexionSqlServer ocnn) {
		super();
		//		this.idCanalMovimiento = idCanalMovimiento
		//		this.ipEstacion = ipEstacion
		this.exito = exito = 0
		this.ocnn = ocnn
		this.cola = cola
	}

	void limpiarTransaccionesPendientes (boolean limpiarReverso) {

		RequerimientoAutorizacion requerimiento = null
		cola.oCnn = ocnn

		requerimiento = new RequerimientoAutorizacion(ocnn)
	
		requerimiento.requerimientoPendiente(cola)
	 
		if (requerimiento.getTipoTransaccion().equals("REVERSO")) {
		
			if (limpiarReverso) {
		
				cola.actualizarEstadoEjecutado()
				requerimiento.actualizarEstadoProcesado()
				//requerimiento.destruirTrama()
			}
		}else {
		
			
			cola.actualizarEstadoEjecutado()
			requerimiento.actualizarEstadoProcesado()
			//requerimiento.destruirTrama()
		}
	}

	void procesar() {

		boolean TIME_OUT = false

		Respuesta_Autorizacion respuestaSwitch =  Respuesta_Autorizacion.getInstancia()
		RequerimientoAutorizacion requerimiento = null
		try {


			LogsApp.getInstance().Escribir("################################################################")
			LogsApp.getInstance().Escribir("Inicia proceso cobro con tarjeta")
			// Cambiar a estado 61 canal movimiento (en estado pendiente.)
			LogsApp.getInstance().Escribir("Procesando ID Canal Movimineto :"+cola.iDCanalMovimiento)
			cola.oCnn = ocnn
			cola.actalizarEsatoEnProceso()
			LogsApp.getInstance().Escribir("Actualiza estado de Canal Moviminetos 61 :"+cola.iDCanalMovimiento)

			//  Obtiene los requerimientos pendientes.
			requerimiento = new RequerimientoAutorizacion(ocnn)
			requerimiento.requerimientoPendiente(cola)


			// Verifico si existe registro.
			if (requerimiento.rqaut_id != null ) {

				LogsApp.getInstance().Escribir("Movimieto: ${requerimiento.rqaut_movimiento}");
				LogsApp.getInstance().Escribir("Tipo de transaccion:${requerimiento.getTipoTransaccion()} CON ${requerimiento.getMedioAutorizador()}" )


				// Actualizar SWT_Requerimiento_Autorizacion  a pendiente (61)
				requerimiento.actualizarEstado()
				LogsApp.getInstance().Escribir("Actualiza estado de Requerimineto 61 :"+requerimiento.getRqaut_id());


				// requerimiento.descomponerTrama()

				// Obtiene configuracion del switch
				Configuracion_Canal_Movimiento switchConfig= new  Configuracion_Canal_Movimiento(ocnn)
				switchConfig.cargarConfiguracion()
				requerimiento.switchConfig = switchConfig


				// Preparo la respuesta.
				respuestaSwitch.ocnn = ocnn
				respuestaSwitch.requerimiento = requerimiento
				String secuenciaValidacion = null

				  /*
				 try {
					secuenciaValidacion =requerimiento.asignarValoresSecuencia( 
						
						Propiedades.get(Constantes.ARCHIVO_CONFIGURACION_DINAMIC, 
						"${requerimiento.getMedioAutorizador()}."+ Constantes.VALIDADOR_CONEXION_DISPOSITIVO) 
						)
			 
				} catch (Exception e) {

					secuenciaValidacion =""
				}

				LogsApp.getInstance().Escribir("Secuencia para validar dispositivo conectado: ${secuenciaValidacion.toLowerCase()}")
				boolean estadoValidacion = false
				if (secuenciaValidacion !=""&&secuenciaValidacion.toLowerCase() != "ninguna"&& secuenciaValidacion.toLowerCase() !="no") {

					JarLector jl = new JarLector()
					estadoValidacion = jl.executeMetodoSecuencia(secuenciaValidacion, respuestaSwitch)
					jl = null

				}else {
					estadoValidacion = true
				}

				LogsApp.getInstance().Escribir("Estado de conexion dispositivo: ${estadoValidacion}")
				*/
				boolean estadoValidacion = true
				if ( estadoValidacion) {

					println "Esperando respuesta metodo."
					String lineasDeEjecucion =  requerimiento.getLineasDeSecuencia()


					// REFLEXION PARA ENVIAR TRAMA Y RETORNA UNA TRAMA.
					JarLector j = JarLector.getInstancia(Propiedades.get(Constantes.ARCHIVO_CONFIGURACION_DINAMIC, "jar.ruta")
							.split(Constantes.SEPARADOR_PROPERTIES))
					j.ocnn = ocnn

					// Verificar si la secuencia es de modalidad objeto
					if (!lineasDeEjecucion.contains("creaTrama") || !lineasDeEjecucion.contains("asignaAtributo")  ) {

						LogsApp.getInstance().Escribir("Entra a funcion para validar trama")
						// Validar trama de envio.
						if (!requerimiento.tramaEnvioValida()) {

							LogsApp.getInstance().Escribir("Trama de requerimineto longitud INVALIDA");
							respuestaSwitch.insetarBug("Trama de requerimineto longitud Invalida")
							//requerimiento.destruirTrama()
							return
						}
						LogsApp.getInstance().Escribir("Trama de requerimineto longitud CORRECTA")
					}

					LogsApp.getInstance().Escribir("Secuencia de Transaccion y trama: " +Encriptador.Encriptar( lineasDeEjecucion))


					String tramaRespuesta =""
					final Duration timeout = Duration.ofSeconds( requerimiento.getTimeOut() )
					ExecutorService executor = Executors.newSingleThreadExecutor()


			 	if (requerimiento.getTipoTransaccion().equals("REVERSO")  && requerimiento.existeReverso() ) { // Validar si ya se hizo un reverso.
						cola.actualizarEstadoEjecutado()
						LogsApp.getInstance().Escribir("Actualizar a estado 42 ID: ${cola.iDCanalMovimiento}")

						requerimiento.actualizarEstadoProcesado()
						LogsApp.getInstance().Escribir("Se encontro un reverso ya realizado de esta transacción.")
						//requerimiento.destruirTrama()
						return
					}  
 
					try {


						LogsApp.getInstance().Escribir("ENTRO a ejecucion del metodo envio con transaccion tipo: ${requerimiento.getTipoTransaccion()} ")
						final Future<String> handler = executor.submit(new Callable() {
									@Override
									public String call() throws Exception {
				 
										println "Ejecutando......"
										
										return "00,222585,000000027300,000000000000,000050    ,014164,005029,000C8815,210718,2004,00,VISA      ,DB,00,9872,491511,0000,013111562              ,CCO CTRO CHIA LOC 46   ,00,000000000000,000000000000,6d"
									     //return "0202PP000200AUTORIZACION OK.    00023100000118104520201117496462KFC12601000000855095                                                                                                                                VISA ELECT/DEB           03GEOMARA OLMOS                                       VISA DEBITO                                                                                  0200008000F800438108XXXXXX4361         210187A074D50F05C0ABDEF6660B59F147B429DA46C75A8601B843DECF878E173ABE                           936373E24343A31343955D1B6AB550EB"
										// return  j.executeMetodoSecuencia(lineasDeEjecucion, respuestaSwitch) //
										  // contact less aprobada "0202PP000100APROBADA  TRANS.    00007100002412105520200807446398PPKFC0011791310101                 016                                                                             002DINERS CLUB                   VISA                     03 HUAMALA/JULIO                                      VISA CREDITO        A0000000031010      A363D267505199D9                     C230C9BA69FC3AB40080008000F800411077XXXXXXX149         2512B538106F07F4F2F6802A3F78F65D01D235FBB4E0344139AE9C1DAF0F0D36AFE7                           932363E25353A303D3AB1595050FE0DD" 
										   
									   //j.executeMetodoSecuencia
									   //(lineasDeEjecucion, respuestaSwitch) //
										 
										//j.executeMetodoSecuencia(lineasDeEjecucion, respuestaSwitch)
										          
										//return j.executeMetodoSecuencia("obtieneClaseNI(com.credibanco.entidades.EnvioProcesoPago[])->asignaAtributo([0]>TipoTransaccion|SwitchIp,RedAdquirente,CodigoDiferido)->ejecutaMetodo(obtieneClase(com.credibanco.entidades.LAN)>[]>ProcesoPago>[[1]])->creaTrama([2])" ,respuestaSwitch )
										//return j.executeMetodoSecuencia("obtieneClase(com.credibanco.tef.controller.param.TefParameters)->ejecutaMetodo([0]>[]>getInstance>[])->ejecutaMetodo([0]>[]>instanceDataManager>[])->ejecutaMetodo([0]>[]>instanceAuthorizationManager>[])->obtieneClase(com.credibanco.tef.controller.TEFTransactionManager)->ejecutaMetodo([4]>[*00*&[2]&[3]]>checkLastTransaction>[])" ,respuestaSwitch )
									}
								})

						try {
							tramaRespuesta=handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS)
						} catch (TimeoutException e) {
							println e.getMessage()
							TIME_OUT = true
							handler.cancel(true)
						}
						executor.shutdown()


					} catch (Exception e) {
						e.printStackTrace()
					}


					LogsApp.getInstance().Escribir("metodo envio con transaccion tipo: ${requerimiento.getTipoTransaccion()}  EJECUTADO")
					// Si no se obtubo un time out procesar respuesta.
					if (!TIME_OUT) {
						                     
					    // tramaRespuesta=	"0100APROBADO            02800115182320190110276594ESP01401                                                                                                        AMERICAN EXPRESS                                            RED DATAFAST                                                7200016121     CA153340376653XXXXX003                  CASTRO E./MYCHAEL                       02                                         ";

						println "Response: ${tramaRespuesta}"
						LogsApp.getInstance().Escribir("Respuesta obtenida (Trama): ${Encriptador.Encriptar(tramaRespuesta)}")
						respuestaSwitch.procesarRespuestaSwitch(tramaRespuesta)

					}else {
						respuestaSwitch.insetarBugTIME_OUT("TIME OUT");
						LogsApp.getInstance().Escribir("Se obtubo un TIME_OUT esperando respuesta")
					}

				}else {

					respuestaSwitch.insetarBug("Problemas de conexion con el dispositivo [${requerimiento.medioAutorizador}]");
					println "Dispositivo no conectado"
					//					cola.actualizarEstadoEjecutado()
					//					LogsApp.getInstance().Escribir("Actualizar a estado 42 ID: ${cola.iDCanalMovimiento}")

			

				}


				cola.actualizarEstadoEjecutado()
				LogsApp.getInstance().Escribir("Actualizar a estado 42 ID: ${cola.iDCanalMovimiento}")
			    requerimiento.actualizarEstadoProcesado() // observacion mccs.

			//	requerimiento.destruirTrama()
			} // fin verificacion si existe requerimientoPendiente.
			else {
				LogsApp.getInstance().Escribir( "No hay registros en SWT_Requerimiento_Autorizacion pendientes (No insertado por MXP)")
				cola.actualizarEstadoEjecutado()
				LogsApp.getInstance().Escribir("Actualizar a estado 42 ID: ${cola.iDCanalMovimiento}")
			}

			LogsApp.getInstance().Escribir("Fin proceso cobro con tarjeta")

		} catch (InvocationTargetException e) {
			respuestaSwitch.insetarBug( e.getTargetException().toString())
			LogsApp.getInstance().Escribir("------------Exception:  ${e.getTargetException()}")
			println e.getTargetException()
			LogsApp.getInstance().Escribir("Fin proceso cobro con tarjeta")
		}
		catch (Exception e) {
			respuestaSwitch.insetarBug( e.getMessage().toString())
			LogsApp.getInstance().Escribir("------------Exception:  ${e.getMessage().toString()}")
			println e.getMessage().toString()
			LogsApp.getInstance().Escribir("Fin proceso cobro con tarjeta")
		}



	}
}
