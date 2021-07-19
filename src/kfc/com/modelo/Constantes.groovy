package kfc.com.modelo

class  Constantes {
	static String  backslashDbl = File.separator//+File.separator
	static String  backslas = File.separator

	static public int  contadorEnvios =0 ;
	static public int  contadorReversos =0 ;
	
	static String SEPARADOR_PROPERTIES = "->";
	static String RURA_JAR = "jar.ruta";
	static String NEW_LINE =  System.getProperty("line.separator")
	static String LINE_SALTO_RELLENO ="######################################################"
    static String RUTA_ARCHIVOS =   System.getProperties().getProperty("user.dir") + backslashDbl// "C:${backslashDbl}Servicio Tarjetas Multipais KFC${backslashDbl}"// new File("").getAbsolutePath() + backslashDbl //
	//static String RUTA_ARCHIVOS =    new File("").getAbsolutePath() + backslashDbl //
	static String TIMER_LOOP_ACTUALIZACION ="aplicacion.timer.actualizacion"
	static String TIMER_LOOP_APP ="aplicacion.timer.loop.app"
	
	
	static String ARCHIVO_CONFIGURACION_DINAMICO ="configuraciones.properties"
	static String LOCAL_IP =   "192.168.170.200" // InetAddress.getLocalHost().getHostAddress() // 
	static String ESTADO_EN_PROCESO_CANAL_MOVIMIENTTO = 61

	static String QUERY_ACTUALIZAR_APP ="query.verificarActualizacionApp";
	static String QUERY_INACTIVAR_ACTUALIZACION ="query.inactivarActualizar";

	static String ARCHIVO_APPLICATION_STATIC ="Application"
	static String ARCHIVO_CONFIGURACION_DINAMIC ="configuraciones"
	// Queryes and Store Procedure.
	static String QUERY_CONFIGURACION_INICIAL ="query.configuracionInicial"
	static String QUERY_PROCESO_COLA ="query.procesacola"

	static String ESTADO_SWT_Respuesta_Autorizacion_OK = "(CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', '41')))"
	static String ESTADO_SWT_Respuesta_Autorizacion_ERROR = "(CONVERT(UNIQUEIDENTIFIER, HASHBYTES('MD5', '41')))"

	static String TRANSACCION_INTENTOS = "trasaccion.intentos";
	static String VALIDADOR_CONEXION_DISPOSITIVO  = "valida.dispositivo.conectado.secuencia"

	static String tiempoInactividadParaLimpiar ="limpiar.memoria.por.inactividad"
	static String VERSION ="version"
	static String TCP_DIRECCION_DISPOSITIVO ="TCP.DIRECCION.${LOCAL_IP}"
	
	
	
	static String SEGURIDAD_LLAVE_IZQUIERDA ="SEGURIDAD.LLAVE.IZQUIERDA"
	static String SEGURIDAD_LLAVE_DERECHA ="SEGURIDAD.LLAVE.DERECHA"
	
	
	
}
