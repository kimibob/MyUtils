package com.zq.encrypt;

import java.security.MessageDigest;

public class MD5 {
	public static String evaluate(String data)
			throws Exception {
		if(data == null){
			return "";
		}
		String temp = "";
		try {
			MessageDigest md5Digest = MessageDigest.getInstance("MD5");
			byte[] encodeMD5Digest = md5Digest.digest(data.getBytes());
			temp = bytesToHex(encodeMD5Digest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp.substring(8, 24);
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		// 把数组每一字节换成16进制连成md5字符串
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			digital = bytes[i];

			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toUpperCase();
	}
}
