package com.zq.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

/**
 * 字符串使用ZIP压缩和解压缩的代码。
 */
public class StringZip {
	public static String zipString(String str) {
		try {
			ByteArrayOutputStream bos = null;
			GZIPOutputStream os = null;
			byte[] bs = null;
			try {
				bos = new ByteArrayOutputStream();
				os = new GZIPOutputStream(bos);
				os.write(str.getBytes());
				os.close();
				bos.close();
				bs = bos.toByteArray();
				return new String(bs, "iso-8859-1");
			} finally {
				bs = null;
				bos = null;
				os = null;
			}
		} catch (Exception ex) {
			return str;
		}
	}

	public static String unzipString(String str) {
		ByteArrayInputStream bis = null;
		ByteArrayOutputStream bos = null;
		GZIPInputStream is = null;
		byte[] buf = null;
		try {
			bis = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
			bos = new ByteArrayOutputStream();
			is = new GZIPInputStream(bis);
			buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) != -1) {
				bos.write(buf, 0, len);
			}
			is.close();
			bis.close();
			bos.close();
			return new String(bos.toByteArray());
		} catch (Exception ex) {
			return str;
		} finally {
			bis = null;
			bos = null;
			is = null;
			buf = null;
		}
	}
	public static void main(String[] args) {
		System.out.println(unzipString(zipString("asdasda")));
		
		try {
//			List<Entity> list = Db.use().query("select id,coordinates from CELL_SCENE_KML where rownum <?", 3);
//			for (Entity entity : list) {
//				System.out.println(entity.get("ID")+"--"+StringZip.unzipString(entity.getStr("COORDINATES")));
//			}
			System.out.println("1234567890".substring(0, "1234567890".length()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}