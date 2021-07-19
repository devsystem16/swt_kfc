package kfc.com.modelo


import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec



class GeneradorClave {

	
	public  static String decifrar(String Data, String Key_izquierda, String Key_derecha) {
		try {
			//ENCRIPTACION
			byte[] keyValue = hex2Bytes(Key_izquierda + Key_derecha);
			byte[] tmpKey = new byte[24];
			System.arraycopy(keyValue, 0, tmpKey, 0, 16);
			System.arraycopy(keyValue, 0, tmpKey, 16, 8);
			keyValue = tmpKey;

			final SecretKey key = new SecretKeySpec(keyValue, "DESede");

			//DESENCRIPTAR
			final Cipher decipher = Cipher.getInstance("DESede/ECB/NoPadding");
			decipher.init(Cipher.DECRYPT_MODE, key);
			final byte[] decipherText = decipher.doFinal(hex2Bytes(Data));//cipherText es la variable tipop Byte que sta encriptada, linea 50

			return byte2Hex(decipherText);

		} catch (Exception e) {
			return e.getMessage();
		}

	}

	public static String cifrar(String Data, String Key_izquierda, String Key_derecha) {

		try {

			String HEXES = Data;

			byte[] keyValue = hex2Bytes(Key_izquierda + Key_derecha);

			byte[] tmpKey = new byte[24];
			System.arraycopy(keyValue, 0, tmpKey, 0, 16);
			System.arraycopy(keyValue, 0, tmpKey, 16, 8);
			keyValue = tmpKey;

			final SecretKey key = new SecretKeySpec(keyValue, "DESede");
			final Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			final byte[] cipherText = cipher.doFinal(hex2Bytes(HEXES));
			//System.out.println(byte2Hex(cipherText));
			return byte2Hex(cipherText);
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	public static byte[] hex2Bytes(String str) {
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer
					.parseInt(str.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	public static String byte2Hex(byte[] b) {

		// String Buffer can be used instead
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}

			if (n < b.length - 1) {
				hs = hs + "";
			}
		}

		return hs;
	}

	
	private static final int sizeOfIntInHalfBytes = 4;
	private static final int numberOfBitsInAHalfByte = 4;
	private static final int halfByte = 0x0F;
	private static final char[] hexDigits = [ '0', '1', '2', '3', '4', '5', '6', '7',
	  '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	];
	public static String getTalla(int dec) {
		StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
		hexBuilder.setLength(sizeOfIntInHalfBytes);
		for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i)
		{
		  int j = dec & halfByte;
		  hexBuilder.setCharAt(i, hexDigits[j]);
		  dec >>= numberOfBitsInAHalfByte;
		}
		return hexBuilder.toString();
	  }
	
	  public static String get_Key_ramdom() {
		 
		  String dato =	String.format("%040x", new BigInteger(1,  "${Fecha.actual_hhmmss()}".getBytes("UTF-8"))).toUpperCase()
		  dato = dato.reverse()
		  return  dato.substring(0, 16)
		}
	  
}
