package com.zq.encrypt;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AES {
	private static final String ALGO = "AES";

	public static String evaluate(String Data, String keyValue)
			throws Exception {
		Key key = generateKey(keyValue);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}

	public static String decrypt(String encryptedData, String keyValue)
			throws Exception {
		Key key = generateKey(keyValue);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	private static Key generateKey(String keyValue) throws Exception {
		Key key = new SecretKeySpec(keyValue.getBytes(), "AES");
		return key;
	}

	public static void main(String[] args) throws Exception {
		String mb = "13599097712";
		String key = "FjWyCmcc20170228";
		String as = AES.evaluate(mb, key);
		String as1 = AES.decrypt("PTfRSb/BacCh2Sc8WYhAlQ==", key);
		System.out.println("as = " + as);
		System.out.println("as1 = " + as1);
	}
}
