package com.geoc.app.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class CryptoUtil {



	private static final char[] symbols;

	static {
		StringBuilder tmp = new StringBuilder();
		for (char ch = '0'; ch <= '9'; ++ch)
			tmp.append(ch);
		for (char ch = 'a'; ch <= 'z'; ++ch)
			tmp.append(ch);
		symbols = tmp.toString().toCharArray();
	}

	private final static Random random = new Random();

	private final static char[] buf=new char[5];

	public static String nextString() {
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}

	 public static String mask(String value)
	  {
	    int length = value.length();
	    StringBuilder result = new StringBuilder();

	    for (int i = 0; i < length; i++)
	    {
	      char c = value.charAt(i);

	      // Process letters, numbers, and symbols -- ignore spaces.
	      if (c != ' ')
	      {
	        // Add 47 (it is ROT-47, after all).
	        c += 47;

	        // If character is now above printable range, make it printable.
	        // Range of printable characters is ! (33) to ~ (126).  A value
	        // of 127 (just above ~) would therefore get rotated down to a
	        // 33 (the !).  The value 94 comes from 127 - 33 = 94, which is
	        // therefore the value that needs to be subtracted from the
	        // non-printable character to put it into the correct printable
	        // range.
	        if (c > '~')
	          c -= 94;
	      }

	      result.append(c);
	    }

	    return result.toString();
	  }

	public static String mash(String email) {
		{
			MessageDigest messageDigest = null;

			try {
				messageDigest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			messageDigest.reset();
			messageDigest.update(email.getBytes(Charset.forName("UTF8")));
			final byte[] resultByte = messageDigest.digest();
			// convert the byte to hex format 
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < resultByte.length; i++) {
				sb.append(Integer.toString((resultByte[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			return sb.toString();
		}

	}
}
