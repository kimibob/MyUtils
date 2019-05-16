package com.zq.xml;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zq.compress.StringZip;
import com.zq.string.StringUtil;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;

/**
 *
 * Description: TODO<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver. Date Author Description<br>
 * ------------------------------------------------------<br>
 * 1.0 2019年4月29日 上午10:20:59 zhouqiang created.<br>
 */

public class XmlParser {

	private static String[] SCENE_ARRAY = {"行政村","重点机构公司","党政军机关","大型企业工厂","商务楼宇"
		,"专业市场","城中村","大型商超","产业园区","公共场所"
		,"交通枢纽","宾馆","景区","医院","高校"
		,"居民小区"};
	private static Pattern pattern = Pattern.compile("(?<=<td>).*?(?=</td>)");
	private static ArrayList<CellScene> cellSceneList = new ArrayList<CellScene>();
	
	public static void main(String[] args) {
		String dir = "C:\\Users\\zhouqiang\\Desktop\\场景kml\\";
//		String dir = "C:\\Users\\zhouqiang\\Desktop\\kml_test\\";
		File parentDir = new File(dir);
		if(parentDir.isDirectory() && parentDir.exists()){
			File[] files = parentDir.listFiles();
			for (File file : files) {
				//if(file.getName().contains("居民小区")){
					System.out.println("======"+file.getName());
					String xmlFile = dir+file.getName();
					Document docResult = XmlUtil.readXML(xmlFile);
					String scene = getFileScene(file.getName());
					if("".equals(scene)){
						System.err.println("未识别该文件的场景!"+file.getName());
						continue;
					}
					parseKML(docResult, scene);
					insertDBBatch(cellSceneList);
//					for (CellScene cell : cellSceneList) {
//						if(cell.getSceneClass() == null||"".equals(cell.getSceneClass() )){
//							System.out.println(cell);
//						}
//					}
					System.out.println("======"+cellSceneList.size());
					cellSceneList.clear();
				//}
			}
		}else{
			System.out.println("输入路径错误："+dir);
			System.exit(0);
		}
		
//		for (CellScene cell : cellSceneList) {
//			if(cell.getCoordinates().contains("|")){
//				System.out.println(cell.getSceneName()+">>"+cell.getCoordinates().split("\\|").length);
//			}
//		}
		
//		parseKMLOld(docResult,  scene);

	}
	private static void parseKML(Document docResult, String scene){
		
		System.out.println("======开始解析场景："+scene);
		NodeList nodes = docResult.getElementsByTagName("Placemark");
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Element e = (Element)nodes.item(i);
			String description = e.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
			CellScene cell = new CellScene();
			format_description(cell, description, scene);
			
			StringBuffer coordinates = new StringBuffer("");
			int coor_num = e.getElementsByTagName("coordinates").getLength();
			for (int j = 0; j < coor_num; j++) {
				String coor = e.getElementsByTagName("coordinates").item(j).getFirstChild().getNodeValue();
				coordinates.append(coor);
				if(j != coor_num-1){
					coordinates.append("|");
				}
			}
			format_coordinates(cell, coordinates.toString());
			cellSceneList.add(cell);

		}
	}

	private static void parseKMLOld(Document docResult, String scene){
		
		NodeList nodes = XmlUtil.getNodeListByXPath("//kml/Folder/Placemark/description", docResult);
		for (int i = 0; i < nodes.getLength(); i++) {
			CellScene cell = new CellScene();
			Node node = (Element) nodes.item(i);
			format_description(cell, node.getTextContent(), scene);
			cellSceneList.add(cell);
		}
		NodeList nodes1 = XmlUtil.getNodeListByXPath("//kml/Folder/Placemark/Polygon/outerBoundaryIs/LinearRing/coordinates",
						docResult);
		if(cellSceneList.size() == nodes1.getLength()){
			for (int i = 0; i < nodes1.getLength(); i++) {
				 Node node = (Element) nodes1.item(i);
				 format_coordinates(cellSceneList.get(i), node.getTextContent());
			}
			
			for (CellScene cell : cellSceneList) {
				//System.out.println(cell.toString());
				//insertDB(cell);
			}
		}else{
			System.out.printf("Error! coordinates size:%s != description size:%s", nodes1.getLength(), nodes.getLength());
		}
	}
	
	private static void insertDBBatch(ArrayList<CellScene> cellSceneList){
		try {
			ArrayList<Entity> entityList = new ArrayList<Entity>();
			for (CellScene cell : cellSceneList) {
				Entity entity = Entity.create("CELL_SCENE_KML_BAK")
						.set("id", cell.getId())
						.set("city", cell.getCity())
						.set("county", cell.getCounty())
						.set("sceneclass", cell.getSceneClass())
						.set("scenename", cell.getSceneName())
						.set("cellproperty", cell.getCellProperty())
						.set("x", cell.getX())
						.set("y", cell.getY())
						.set("area", cell.getArea());
						List<String> coordinates_list = StringUtil.getStrList(cell.getCoordinates(),4000);
//						for (int i = 0; i < coordinates_list.size(); i++) {
//							entity.set("coordinates"+i, coordinates_list.get(i));
//						}
						//数据库表中coordinates字段预设有10个： coordinates0~9
						for (int i = 0; i < 10; i++) {
							if(i < coordinates_list.size()){
								entity.set("coordinates"+i, coordinates_list.get(i));
							}else{
								entity.set("coordinates"+i, null);
							}
						}
				entityList.add(entity);
				//每10条批量插入一次，大于10条会报错
				if(entityList.size()!=0 && entityList.size()%10 == 0){
					Db.use().insert(entityList);
					entityList.clear();
				}
			}
			//剩余10条插入
			Db.use().insert(entityList);
			entityList.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void insertDB(CellScene cell){
		try {
			
			Entity entity = Entity.create("CELL_SCENE_KML_BAK")
			.set("id", cell.getId())
			.set("city", cell.getCity())
			.set("county", cell.getCounty())
			.set("sceneclass", cell.getSceneClass())
			.set("scenename", cell.getSceneName())
			.set("cellproperty", cell.getCellProperty())
			.set("x", cell.getX())
			.set("y", cell.getY())
			.set("area", cell.getArea());
			//.set("coordinates", cell.getCoordinates());
			List<String> coordinates_list = StringUtil.getStrList(cell.getCoordinates(),4000);
			for (int i = 0; i < coordinates_list.size(); i++) {
				entity.set("coordinates"+i, coordinates_list.get(i));
			}
			Db.use().insert(entity);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(cell.toString());
		}
	}

	private static CellScene format_description(CellScene cell, String values, String sceneClass) {
		String description = values.toString().replaceAll(" |\r|\n", "");
		Matcher matcher = pattern.matcher(description);
		int pos = 0;
		if("交通枢纽".equals(sceneClass) || "公共场所".equals(sceneClass) 
				|| "产业园区".equals(sceneClass) || "大型商超".equals(sceneClass)
				|| "专业市场".equals(sceneClass) || "大型企业工厂".equals(sceneClass)
				|| "党政军机关".equals(sceneClass) || "重点机构公司".equals(sceneClass)
				|| "行政村".equals(sceneClass) ){
			while (matcher.find()) {
				if (pos == 1) {
					cell.setId(matcher.group());
				} else if(pos == 3){
					cell.setCity(matcher.group());
				} else if(pos == 5){
					cell.setCounty(matcher.group());
				} else if(pos == 7){
					cell.setSceneClass(matcher.group());
				} else if(pos == 9){
					cell.setSceneName(matcher.group());
				} else if(pos == 11){
					cell.setX(matcher.group());
				} else if(pos == 13){
					cell.setY(matcher.group());
				} else if(pos == 15){
					cell.setArea(matcher.group());
				}
				pos++;
			}
		}else if("高校".equals(sceneClass)){
			cell.setSceneClass("高校");
			while (matcher.find()) {
				if (pos == 1) {
					cell.setCity(matcher.group());
				} else if(pos == 3){
					cell.setCellProperty(matcher.group());
				}else if(pos == 7){
					cell.setId(matcher.group());
				}
				pos++;
			}
		}else if("城中村".equals(sceneClass)){
			while (matcher.find()) {
				if (pos == 1) {
					cell.setId(matcher.group());
				} else if(pos == 3){
					cell.setCity(matcher.group());
				} else if(pos == 5){
					cell.setCounty(matcher.group());
				} else if(pos == 7){
					cell.setSceneClass(matcher.group());
				} else if(pos == 9){
					cell.setSceneName(matcher.group());
				}
				pos++;
			}
		}else if("商务楼宇".equals(sceneClass)){
			while (matcher.find()) {
				if (pos == 1) {
					cell.setId(matcher.group());
				} else if(pos == 3){
					cell.setCity(matcher.group());
				} else if(pos == 5){
					cell.setCounty(matcher.group());
				} else if(pos == 7){
					cell.setCellProperty(matcher.group());
				} else if(pos == 9){
					cell.setSceneClass(matcher.group());
				} else if(pos == 11){
					cell.setSceneName(matcher.group());
				}
				pos++;
			}
		}else{//宾馆,居民小区,景区,医院
			while (matcher.find()) {
				if (pos == 1) {
					cell.setId(matcher.group());
				} else if(pos == 3){
					cell.setCity(matcher.group());
				} else if(pos == 5){
					cell.setCounty(matcher.group());
				} else if(pos == 7){
					cell.setSceneClass(matcher.group());
				} else if(pos == 9){
					cell.setSceneName(matcher.group());
				} else if(pos == 11){
					cell.setCellProperty(matcher.group());
				} else if(pos == 13){
					cell.setX(matcher.group());
				} else if(pos == 15){
					cell.setY(matcher.group());
				} else if(pos == 17){
					cell.setArea(matcher.group());
				}
				pos++;
			}
		}
		
		return cell;
	}

	private static CellScene format_coordinates(CellScene cell, String values) {
		String coordinates = values.toString().replaceAll("\r|\n", "")
				.replaceAll(" +", " ").trim().replaceAll(" \\| ", "|").replaceAll(" ", ";");
		cell.setCoordinates(coordinates);
		return cell;
	}
	
	private static String getFileScene(String fileName){
		
		for (int i = 0; i < SCENE_ARRAY.length; i++) {
			if(fileName.contains(SCENE_ARRAY[i])){
				return SCENE_ARRAY[i];
			}
		}
		return "";
	}
}
