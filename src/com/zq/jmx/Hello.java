package com.zq.jmx;

import java.util.HashMap;

public class Hello implements HelloMBean {
	private String name;
	private HashMap<String, Integer> abc = new HashMap<String, Integer>();
	
	public Hello() {
		super();
		this.abc.put("zq", 180);
		this.abc.put("sl", 139);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void printHello() {
		System.out.println("Hello world, " + name);
	}

	@Override
	public void printHello(String whoName) {
		System.out.println("Hello, " + whoName);
	}
	
	public int getNum(String name) {
//		System.out.println("name:"+ name+"this.abc.get(name)"+this.abc.get(name));
		return this.abc.get(name);
	}
}