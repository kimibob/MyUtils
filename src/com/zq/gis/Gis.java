package com.zq.gis;


/**
 *
 * Description: TODO<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver. Date Author Description<br>
 * ------------------------------------------------------<br>
 * 1.0 2019年4月4日 下午1:25:03 zhouqiang created.<br>
 */

public class Gis {
	private final static double DEF_PI = 3.14159265359; // PI
	private final static double DEF_PI180 = 0.01745329252; // PI/180.0
	private final static double DEF_R = 6370693.5; // radius of earth
	/**
	 * 地球每度的弧长(km)
	 * */
	public final static double EARTH_ARC = 111.199;
	
	/**
	 * 角度转化为弧度(rad)
	 * 
	 * */
	public static double rad(double d) {
		return d * Math.PI / 180.0;
	}
	
	/**
	 * 以A点为中心，计算 点B 在 点A 以正北方向顺时针转过的角度
	 */
	public static double getAngle(MyLatLng A, MyLatLng B) {
		double dx = (B.m_RadLo - A.m_RadLo) * A.Ed;
		double dy = (B.m_RadLa - A.m_RadLa) * A.Ec;
		double angle = 0.0;
		angle = Math.atan(Math.abs(dx / dy)) * 180 / Math.PI;
		double dLo = B.m_Longitude - A.m_Longitude;
		double dLa = B.m_Latitude - A.m_Latitude;
		if (dLo > 0 && dLa <= 0) {
			angle = (90 - angle) + 90;
		} else if (dLo <= 0 && dLa < 0) {
			angle = angle + 180;
		} else if (dLo < 0 && dLa >= 0) {
			angle = (90 - angle) + 270;
		}
		return angle;
	}
	
	/**
	 * 根据圆心、半径算出经纬度范围
	 * 
	 * @param lon
	 *            圆心经度
	 * @param lat
	 *            圆心纬度
	 * @param r
	 *            半径（米）
	 * @return double[4] 西侧经度，东侧经度，南侧纬度，北侧纬度
	 */
	public static double[] getRange(double lon, double lat, int r) {
		double[] range = new double[4];
		// 角度转换为弧度
		double ns = lat * DEF_PI180;
		double sinNs = Math.sin(ns);
		double cosNs = Math.cos(ns);
		double cosTmp = Math.cos(r / DEF_R);
		// 经度的差值
		double lonDif = Math.acos((cosTmp - sinNs * sinNs) / (cosNs * cosNs))
				/ DEF_PI180;
		// 保存经度
		range[0] = lon - lonDif;
		range[1] = lon + lonDif;
		double m = 0 - 2 * cosTmp * sinNs;
		double n = cosTmp * cosTmp - cosNs * cosNs;
		double o1 = (0 - m - Math.sqrt(m * m - 4 * (n))) / 2;
		double o2 = (0 - m + Math.sqrt(m * m - 4 * (n))) / 2;
		// 纬度
		double lat1 = 180 / DEF_PI * Math.asin(o1);
		double lat2 = 180 / DEF_PI * Math.asin(o2);
		// 保存
		range[2] = lat1;
		range[3] = lat2;
		return range;
	}

	/**
	 * 计算地球面上两上坐标点之间距离
	 * 
	 * @param lon1
	 *            位置1经度
	 * @param lat1
	 *            位置1纬度
	 * @param lon2
	 *            位置2经度
	 * @param lat2
	 *            位置2纬度
	 */
	public static double getLongDistance(double lon1, double lat1, double lon2,
			double lat2) {
		double ew1, ns1, ew2, ns2;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 求大圆劣弧与球心所夹的角(弧度)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1)
				* Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0)
			distance = 1.0;
		else if (distance < -1.0)
			distance = -1.0;
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		return distance;
	}

	/**
	 * 已知一点经纬度A，和与另一点B的距离和方位角，求B的经纬度
	 *
	 * @param lng1
	 *            A的经度
	 * @param lat1
	 *            A的纬度
	 * @param distance
	 *            AB距离（单位：千米）
	 * @param azimuth
	 *            AB方位角
	 * @return B的经纬度
	 * */
	public static String ConvertDistanceToLogLat(double lng1, double lat1,
			double distance, double azimuth) {
		azimuth = rad(azimuth);
		// 将距离转换成经度的计算公式
		double lon = lng1 + (distance * Math.sin(azimuth))
				/ (EARTH_ARC * Math.cos(rad(lat1)));
		// 将距离转换成纬度的计算公式
		double lat = lat1 + (distance * Math.cos(azimuth)) / EARTH_ARC;
		return lon + "," + lat;
	}
	
	public static void main(String args[]) {
		double x1 = 119.2749;
		double y1 = 26.0850;
		double r[] = getRange(x1, y1, 100);
		System.out.println(r[0]);
		System.out.println(r[1]);
		System.out.println(r[2]);
		System.out.println(r[3]);
		System.out.println();
		System.out.println(getLongDistance(x1, y1, r[0], y1));//西
		System.out.println(getLongDistance(x1, y1, r[1], y1));//东
		System.out.println(getLongDistance(x1, y1, x1, r[2]));//南
		System.out.println(getLongDistance(x1, y1, x1, r[3]));//北
		System.out.println();
		System.out.println(getAngle(new MyLatLng(x1, y1),
					new MyLatLng(r[0], y1)));
		System.out.println(getAngle(new MyLatLng(x1, y1),
				new MyLatLng(r[1], y1)));
		System.out.println(getAngle(new MyLatLng(x1, y1),
				new MyLatLng(x1, r[2])));
		System.out.println(getAngle(new MyLatLng(x1, y1),
				new MyLatLng(x1, r[3])));
		System.out.println();
		String target = ConvertDistanceToLogLat(x1, y1, 0.1, 90);
		System.out.println(target);
		System.out.println(getLongDistance(x1, y1, Double.parseDouble(target.split(",")[0])
				, Double.parseDouble(target.split(",")[1])));
		
		
		
	}
}
