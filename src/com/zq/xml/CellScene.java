package com.zq.xml;

/**
 *
 * Description: TODO<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver.		Date		Author			Description<br>
 * ------------------------------------------------------<br>
 * 1.0		2019年4月29日 上午11:22:56		zhouqiang		created.<br>
 */

public class CellScene {

	private String id;
	private String city;
	private String county;
	private String sceneClass;
	private String sceneName;
	private String cellProperty;
	private String X;
	private String Y;
	private String Area;
	private String coordinates;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[\"id\":\"");
		builder.append(id);
		builder.append("\", \"city\":\"");
		builder.append(city);
		builder.append("\", \"county\":\"");
		builder.append(county);
		builder.append("\", \"sceneClass\":\"");
		builder.append(sceneClass);
		builder.append("\", \"sceneName\":\"");
		builder.append(sceneName);
		builder.append("\", \"cellProperty\":\"");
		builder.append(cellProperty);
		builder.append("\", \"X\":\"");
		builder.append(X);
		builder.append("\", \"Y\":\"");
		builder.append(Y);
		builder.append("\", \"Area\":\"");
		builder.append(Area);
		builder.append("\", \"coordinates\":\"");
		builder.append(coordinates);
		builder.append("\"]");
		return builder.toString();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getSceneClass() {
		return sceneClass;
	}
	public void setSceneClass(String sceneClass) {
		this.sceneClass = sceneClass;
	}
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public String getCellProperty() {
		return cellProperty;
	}
	public void setCellProperty(String cellProperty) {
		this.cellProperty = cellProperty;
	}
	public String getX() {
		return X;
	}
	public void setX(String x) {
		X = x;
	}
	public String getY() {
		return Y;
	}
	public void setY(String y) {
		Y = y;
	}
	public String getArea() {
		return Area;
	}
	public void setArea(String area) {
		Area = area;
	}
	public String getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	
}
