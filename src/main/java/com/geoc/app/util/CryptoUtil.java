package com.geoc.app.util;

public class CryptoUtil {

	private static final String SECRET = "intelinside";

	public static String mask(String email){
		return xor(email,SECRET);
	}
	
	private static String xor(final String str, final String key) {
		StringBuffer sb = new StringBuffer (str);

	      int lenStr = str.length();
	      int lenKey = key.length();
		   
	      //
	      // For each character in our string, encrypt it...
	      for ( int i = 0, j = 0; i < lenStr; i++, j++ ) 
	      {
	         if ( j >= lenKey ) j = 0;  // Wrap 'round to beginning of key string.

	         //
	         // XOR the chars together. Must cast back to char to avoid compile error. 
	         //
	         sb.setCharAt(i, (char)(str.charAt(i) ^ key.charAt(j))); 
	      }

	      return sb.toString();	}
}
