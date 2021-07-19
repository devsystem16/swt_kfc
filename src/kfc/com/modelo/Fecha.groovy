package kfc.com.modelo

import java.text.SimpleDateFormat

class Fecha {


	static String  actual () {
		String SFecha=""
		Date fechalocal = new Date()
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")
		SFecha=formatter.format(fechalocal)
	}
	
	static String  actual_hhmmss () {
		String SFecha=""
		Date fechalocal = new Date()
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
		SFecha=formatter.format(fechalocal)
	}
}
