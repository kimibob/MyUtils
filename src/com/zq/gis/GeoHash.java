package com.zq.gis;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Description: TODO<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver. Date Author Description<br>
 * ------------------------------------------------------<br>
 * 1.0 2018年9月12日 下午5:29:01 zhouqiang created.<br>
 */

public class GeoHash {
	private static LocationBean location;
	static DecimalFormat df = new DecimalFormat("0.000000");

	/**
	 * 1 2500km; 2 630km; 3 78km; 4 30km 5 2.4km; 6 610m; 7 76m; 8 19m 9 2m
	 */
	// geohash长度 Lat位数 Lng位数 Lat误差 Lng误差 km误差
	// 1 2 3 ±23 ±23 ±2500
	// 2 5 5 ± 2.8 ±5.6 ±630
	// 3 7 8 ± 0.70 ± 0.7 ±78
	// 4 10 10 ± 0.087 ± 0.18 ±20
	// 5 12 13 ± 0.022 ± 0.022 ±2.4
	// 6 15 15 ± 0.0027 ± 0.0055 ±0.61
	// 7 17 18 ±0.00068 ±0.00068 ±0.076
	// 8 20 20 ±0.000086 ±0.000172 ±0.01911
	// 9 22 23 ±0.000021 ±0.000021 ±0.00478
	// 10 25 25 ±0.00000268 ±0.00000536 ±0.0005971
	// 11 27 28 ±0.00000067 ±0.00000067 ±0.0001492
	// 12 30 30 ±0.00000008 ±0.00000017 ±0.0000186

	private static int hashLength = 7; // 经纬度转化为geohash长度
	private static int latLength = 17; // 纬度转化为二进制长度
	private static int lngLength = 18; // 经度转化为二进制长度

	private static double minLat;// 每格纬度的单位大小
	private static double minLng;// 每个经度的单位大小
	private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',
			'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private static HashMap<Character, Integer> CHARSMAP;

	static {
		CHARSMAP = new HashMap<Character, Integer>();
		for (int i = 0; i < CHARS.length; i++) {
			CHARSMAP.put(CHARS[i], i);
		}
	}

	public int gethashLength() {
		return hashLength;
	}

	/**
	 * @Description: 设置经纬度的最小单位
	 */
	private static void setMinLatLng() {
		minLat = LocationBean.MAXLAT - LocationBean.MINLAT;
		for (int i = 0; i < latLength; i++) {
			minLat /= 2.0;
		}
		minLng = LocationBean.MAXLNG - LocationBean.MINLNG;
		for (int i = 0; i < lngLength; i++) {
			minLng /= 2.0;
		}
	}

	/**
	 * @return
	 * @Description: 求所在坐标点及周围点组成的九个
	 */
	public static List<String> getGeoHashBase32For9() {
		double leftLat = location.getLat() - minLat;
		double rightLat = location.getLat() + minLat;
		double upLng = location.getLng() - minLng;
		double downLng = location.getLng() + minLng;
		List<String> base32For9 = new ArrayList<String>();
		// 左侧从上到下 3个
		String leftUp = getGeoHashBase32(leftLat, upLng);
		if (!(leftUp == null || "".equals(leftUp))) {
			base32For9.add(leftUp);
		}
		String leftMid = getGeoHashBase32(leftLat, location.getLng());
		if (!(leftMid == null || "".equals(leftMid))) {
			base32For9.add(leftMid);
		}
		String leftDown = getGeoHashBase32(leftLat, downLng);
		if (!(leftDown == null || "".equals(leftDown))) {
			base32For9.add(leftDown);
		}
		// 中间从上到下 3个
		String midUp = getGeoHashBase32(location.getLat(), upLng);
		if (!(midUp == null || "".equals(midUp))) {
			base32For9.add(midUp);
		}
		String midMid = getGeoHashBase32(location.getLat(), location.getLng());
		if (!(midMid == null || "".equals(midMid))) {
			base32For9.add(midMid);
		}
		String midDown = getGeoHashBase32(location.getLat(), downLng);
		if (!(midDown == null || "".equals(midDown))) {
			base32For9.add(midDown);
		}
		// 右侧从上到下 3个
		String rightUp = getGeoHashBase32(rightLat, upLng);
		if (!(rightUp == null || "".equals(rightUp))) {
			base32For9.add(rightUp);
		}
		String rightMid = getGeoHashBase32(rightLat, location.getLng());
		if (!(rightMid == null || "".equals(rightMid))) {
			base32For9.add(rightMid);
		}
		String rightDown = getGeoHashBase32(rightLat, downLng);
		if (!(rightDown == null || "".equals(rightDown))) {
			base32For9.add(rightDown);
		}

		return base32For9;
	}

	/**
	 * @param length
	 * @return
	 * @Description: 设置经纬度转化为geohash长度
	 */
	public static boolean sethashLength(int length) {
		if (length < 1) {
			return false;
		}
		hashLength = length;
		latLength = (length * 5) / 2;
		if (length % 2 == 0) {
			lngLength = latLength;
		} else {
			lngLength = latLength + 1;
		}
		setMinLatLng();
		return true;
	}

	/**
	 * @return
	 * 
	 * @Description: 获取经纬度的base32字符串
	 */

	public static String getGeoHash(String lat, String lng) {
		// setMinLatLng();
		// sethashLength(7);
		boolean[] bools = getGeoBinary(Double.valueOf(lat), Double.valueOf(lng));
		if (bools == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bools.length; i = i + 5) {
			boolean[] base32 = new boolean[5];
			for (int j = 0; j < 5; j++) {
				base32[j] = bools[i + j];
			}
			char cha = getBase32Char(base32);
			if (' ' == cha) {
				return null;
			}
			sb.append(cha);
		}
		return sb.toString();
		// return getGeoHashBase32(Long.parseLong(lat), Long.parseLong(lon));
	}

	/**
	 * 返回的GeoHash值得长度为8 精度为19米
	 * 
	 * @param lat
	 * @param lng
	 * @return
	 */
	public static String getGeoHash8(String lat, String lng) {
		// setMinLatLng();
		sethashLength(8);
		return getGeoHash(lat, lng);
	}

	public static String getGeoHash(String lat, String lng, Integer geoLength) {
		sethashLength(geoLength);
		return getGeoHash(lat, lng);
	}

	/**
	 * @param lat
	 * @param lng
	 * @return
	 * 
	 * @Description: 获取经纬度的base32字符串
	 */
	private static String getGeoHashBase32(double lat, double lng) {

		boolean[] bools = getGeoBinary(lat, lng);
		if (bools == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bools.length; i = i + 5) {
			boolean[] base32 = new boolean[5];
			for (int j = 0; j < 5; j++) {
				base32[j] = bools[i + j];
			}
			char cha = getBase32Char(base32);
			if (' ' == cha) {
				return null;
			}
			sb.append(cha);
		}
		return sb.toString();
	}

	/**
	 * @param base32
	 * @return
	 * 
	 * @Description: 将五位二进制转化为base32
	 */
	private static char getBase32Char(boolean[] base32) {
		if (base32 == null || base32.length != 5) {
			return ' ';
		}
		int num = 0;
		for (boolean bool : base32) {
			num <<= 1;
			if (bool) {
				num += 1;
			}
		}
		return CHARS[num % CHARS.length];
	}

	/**
	 * @param i
	 * @return
	 * 
	 * @Description: 将数字转化为二进制字符串
	 */
	private static String getBase32BinaryString(int i) {
		if (i < 0 || i > 31) {
			return null;
		}
		String str = Integer.toBinaryString(i + 32);
		return str.substring(1);
	}

	/**
	 * @param geoHash
	 * @return
	 * 
	 * @Description: 将geoHash转化为二进制字符串
	 */
	private static String getGeoHashBinaryString(String geoHash) {
		if (geoHash == null || "".equals(geoHash)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < geoHash.length(); i++) {
			char c = geoHash.charAt(i);
			if (CHARSMAP.containsKey(c)) {
				String cStr = getBase32BinaryString(CHARSMAP.get(c));
				if (cStr != null) {
					sb.append(cStr);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * @param geoHash
	 * @return
	 * 
	 * @Description: 返回geoHash 对应的坐标
	 */
	public static LocationBean getLocation(String geoHash) {
		String geoHashBinaryStr = getGeoHashBinaryString(geoHash);
		if (geoHashBinaryStr == null) {
			return null;
		}
		StringBuffer lat = new StringBuffer();
		StringBuffer lng = new StringBuffer();
		for (int i = 0; i < geoHashBinaryStr.length(); i++) {
			if (i % 2 != 0) {
				lat.append(geoHashBinaryStr.charAt(i));
			} else {
				lng.append(geoHashBinaryStr.charAt(i));
			}
		}
		// double latValue = getGeoHashMid(lat.toString(), LocationBean.MINLAT,
		// LocationBean.MAXLAT);
		// double lngValue = getGeoHashMid(lng.toString(), LocationBean.MINLNG,
		// LocationBean.MAXLNG);
		// String format = df.format(latValue);
		// df.format(lngValue);
		LocationBean location = new LocationBean(Double.valueOf(df
				.format(getGeoHashMidLat(lat.toString()))), Double.valueOf(df
				.format(getGeoHashMidLng(lng.toString()))));
		location.setGeoHash(geoHash);
		return location;
	}

	/**
	 * @param binaryStr
	 * @param min
	 * @param max
	 * @return
	 * 
	 * @Description: 返回二进制对应的中间值
	 */
	private static double getGeoHashMid(String binaryStr, double min, double max) {
		if (binaryStr == null || binaryStr.length() < 1) {
			return (min + max) / 2.0;
		}
		if (binaryStr.charAt(0) == '1') {
			return getGeoHashMid(binaryStr.substring(1), (min + max) / 2.0, max);
		} else {
			return getGeoHashMid(binaryStr.substring(1), min, (min + max) / 2.0);
		}
	}

	private static double getGeoHashMidLat(String binaryStr) {
		return getGeoHashMid(binaryStr, LocationBean.MINLAT,
				LocationBean.MAXLAT);
	}

	private static double getGeoHashMidLng(String binaryStr) {
		return getGeoHashMid(binaryStr, LocationBean.MINLNG,
				LocationBean.MAXLNG);
	}

	/**
	 * @param lat
	 * @param lng
	 * @return
	 * 
	 * @Description: 获取坐标的geo二进制字符串
	 */
	private static boolean[] getGeoBinary(double lat, double lng) {
		boolean[] latArray = getHashArray(lat, LocationBean.MINLAT,
				LocationBean.MAXLAT, latLength);
		boolean[] lngArray = getHashArray(lng, LocationBean.MINLNG,
				LocationBean.MAXLNG, lngLength);
		return merge(latArray, lngArray);
	}

	/**
	 * @param latArray
	 * @param lngArray
	 * @return
	 * 
	 * @Description: 合并经纬度二进制
	 */
	private static boolean[] merge(boolean[] latArray, boolean[] lngArray) {
		if (latArray == null || lngArray == null) {
			return null;
		}
		boolean[] result = new boolean[lngArray.length + latArray.length];
		Arrays.fill(result, false);
		for (int i = 0; i < lngArray.length; i++) {
			result[2 * i] = lngArray[i];
		}
		for (int i = 0; i < latArray.length; i++) {
			result[2 * i + 1] = latArray[i];
		}
		return result;
	}

	/**
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 * 
	 * @Description: 将数字转化为geohash二进制字符串
	 */
	private static boolean[] getHashArray(double value, double min, double max,
			int length) {
		if (value < min || value > max) {
			return null;
		}
		if (length < 1) {
			return null;
		}
		boolean[] result = new boolean[length];
		for (int i = 0; i < length; i++) {
			double mid = (min + max) / 2.0;
			if (value > mid) {
				result[i] = true;
				min = mid;
			} else {
				result[i] = false;
				max = mid;
			}
		}
		return result;
	}
	public static void main(String[] args) {
		System.out.println(getGeoHash("30.549608","114.376971",5));
		System.out.println(getLocation("wwx1cf4"));
	}
}
