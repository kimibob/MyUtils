package com.zq.bytesutil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;


public class BytesUtils {

	/**
	 * 字节数组 -->> 十六进制的字符串(高字节序) new byte[]{3,5,2,10} -->>0x03 0x05 0x02 0x0a
	 * 
	 * @param bytes
	 *            字节数组
	 * @return 十六进制的字符串
	 */
	public static String bytesToHexStringH(byte[] bytes, String separator) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder(separator);
		int byteLength = bytes.length;
		for (int i = 0; i < byteLength; i++) {
			int val = bytes[i] & 0xFF;
			String hexVal = Integer.toHexString(val);
			if (hexVal.length() == 1) {
				sb.append("0");
				sb.append(hexVal);
			} else {
				sb.append(hexVal);
			}

			if (separator != "" && i < byteLength - 1) {
				sb.append(separator);
			}
		}
		sb.trimToSize();
		return sb.toString();
	}

	/**
	 * 字节数组 -->> 十六进制的字符串(低字节序) new byte[]{3,5,2,10} -->>0x0a 0x02 0x05 0x03
	 * 
	 * @param bytes
	 *            字节数组
	 * @return 十六进制的字符串
	 */
	public static String bytesToHexStringL(byte[] bytes, String separator) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder(separator);
		int byteLength = bytes.length;
		for (int i = byteLength - 1; i >= 0; i--) {
			int val = bytes[i] & 0xFF;
			String hexVal = Integer.toHexString(val);
			if (hexVal.length() == 1) {
				sb.append("0");
				sb.append(hexVal);
			} else {
				sb.append(hexVal);
			}
			if (separator != "" && i > 0) {
				sb.append(separator);
			}
		}
		sb.trimToSize();
		return sb.toString();
	}

	private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
	
	/**
	 * 十六进制的字符串 -->> 字节数组   
	 * @param strHex 十六进制的字符串 0a020503
	 * @param isHigh 是否高字节序
	 * @return 字节数组
	 */
	public static byte[] hexStringToBytes(String strHex, boolean isHigh) {
		if (strHex == null || strHex.length() == 0) {
			return null;
		}
		strHex = strHex.toUpperCase();
		int length = strHex.length() / 2;
		char[] hexChars = strHex.toCharArray();
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			if (isHigh) {
				bytes[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
			} else {
				bytes[length - 1 - i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
			}
		}
		return bytes;
	}

	/**
	 * 十六进制的字符串 -->> 字节数组   
	 * @param strHex 十六进制的字符串 0a020503
	 * @param isHigh 是否高字节序
	 * @return 字节数组
	 */
	public static byte[] shortToBytesL(short val) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (val & 0xFF);     // 低字节存于内存低地址
        bytes[1] = (byte) (((short) (val >>> 8)) & 0xFF);

        return bytes;
    }

    public static byte[] shortToBytesH(short val) {
        byte[] bytes = new byte[2];
        bytes[1] = (byte) (val & 0xFF);
        bytes[0] = (byte) (val >>> 8 & 0xFF);

        return bytes;
    }

    public static byte[] unsignedShortToBytesH(int val) {
        byte[] bytes = new byte[2];
        byte[] bytesAll = BytesUtils.intToBytesH(val);
        bytes[0] = bytesAll[2];
        bytes[1] = bytesAll[3];

        return bytes;
    }

    public static byte[] intToBytesL(int val) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (val & 0xFF);
        bytes[1] = (byte) (val >>> 8 & 0xFF);
        bytes[2] = (byte) (val >>> 16 & 0xFF);
        bytes[3] = (byte) (val >>> 24 & 0xFF);

        return bytes;
    }

    public static byte[] intToBytesH(int val) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (val & 0xFF);
        bytes[2] = (byte) (val >>> 8 & 0xFF);
        bytes[1] = (byte) (val >>> 16 & 0xFF);
        bytes[0] = (byte) (val >>> 24 & 0xFF);

        return bytes;
    }

    public static byte[] longToBytesL(long val) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) ((val >>> (i * 8)) & 0xFF);
        }

        return bytes;
    }

    public static byte[] longToBytesH(long val) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) ((val >>> (i * 8)) & 0xFF);
        }

        return bytes;
    }

    public static byte[] floatToBytesL(float val) {
        return intToBytesL(Float.floatToIntBits(val));
    }

    public static byte[] doubleToBytesL(double val) {
        return longToBytesL(Double.doubleToLongBits(val));
    }

    public static short bytesToShortL(byte[] bytes) {
        short s = (short) (((short) bytes[1] << 8) & 0xFF00);
        s |= (short) bytes[0] & 0xFF;

        return s;
    }

    public static short bytesToShortH(byte[] bytes) {
        short s = (short) (((short) bytes[0] << 8) & 0xFF00);
        s |= bytes[1] & 0xFF;

        return s;
    }

    public static int bytesToIntL(byte[] bytes) {
        int i = (bytes[3] << 24) & 0xFF000000;
        i |= (bytes[2] << 16) & 0xFF0000;
        i |= (bytes[1] << 8) & 0xFF00;
        i |= bytes[0] & 0xFF;

        return i;
    }

    public static int bytesToIntH(byte[] bytes) {
        int i = (bytes[0] << 24) & 0xFF000000;
        i |= (bytes[1] << 16) & 0xFF0000;
        i |= (bytes[2] << 8) & 0xFF00;
        i |= bytes[3] & 0xFF;

        return i;
    }

    public static long bytesToLongL(byte[] b) {
        // 如果不强制转换为long，那么默认会当作int，导致最高32位丢失
        long l = (long) b[0] & 0xFFL;
        l |= ((long) b[1] << 8) & 0xFF00L;
        l |= ((long) b[2] << 16) & 0xFF0000L;
        l |= ((long) b[3] << 24) & 0xFF000000L;
        l |= ((long) b[4] << 32) & 0xFF00000000L;
        l |= ((long) b[5] << 40) & 0xFF0000000000L;
        l |= ((long) b[6] << 48) & 0xFF000000000000L;
        l |= ((long) b[7] << 56) & 0xFF00000000000000L;

        return l;
    }

    public static long bytesToLongH(byte[] b) {
        long l = ((long) b[0] << 56) & 0xFF00000000000000L;
        l |= ((long) b[1] << 48) & 0xFF000000000000L;
        l |= ((long) b[2] << 40) & 0xFF0000000000L;
        l |= ((long) b[3] << 32) & 0xFF00000000L;
        l |= ((long) b[4] << 24) & 0xFF000000L;
        l |= ((long) b[5] << 16) & 0xFF0000L;
        l |= ((long) b[6] << 8) & 0xFF00L;
        l |= (long) b[7] & 0xFFL;

        return l;
    }

    public static int bytesToUnsignedShortL(byte[] bytes) {
        int s = (bytes[1] << 8) & 0xFF00;
        s |= bytes[0] & 0xFF;
        return s;
    }

    public static int bytesToUnsignedShortH(byte[] bytes) {
        int s = (bytes[0] << 8) & 0xFF00;
        s |= bytes[1] & 0xFF;
        return s;
    }

    public static long bytesToUnsignedIntL(byte[] bytes) {
        long i = ((long) bytes[3] << 24) & 0xFF000000L;
        i |= ((long) bytes[2] << 16) & 0xFF0000L;
        i |= ((long) bytes[1] << 8) & 0xFF00L;
        i |= (long) bytes[0] & 0xFFL;
        return i;
    }

    public static long bytesToUnsignedIntH(byte[] bytes) {
        long i = (((long) bytes[0]) << 24) & 0xFF000000L;
        i |= (((long) bytes[1]) << 16) & 0xFF0000L;
        i |= (((long) bytes[2]) << 8) & 0xFF00L;
        i |= ((long) bytes[3]) & 0xFFL;
        return i;
    }
    
    /**
     * 将字符转为字节(编码)
     * @param chars 字符数组
     * @return 字节数组
     */
    public static byte[] charsToBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    /**
     * 将字节转为字符(解码)
     * @param bytes 字节数组
     * @return 字符数组
     */
    public static char[] bytesToChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }
    
    
	public static void main(String[] args) {
		byte[] b = new byte[] { 3, 5, 2, 10 };
		System.out.println(BytesUtils.bytesToHexStringL(b, ""));
		System.out.println(Arrays.toString(hexStringToBytes("0a020503",false)));
	}

}
