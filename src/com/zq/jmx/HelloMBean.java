package com.zq.jmx;

public interface HelloMBean {
	public String getName();
    public void setName(String name);
    public void printHello();
    public void printHello(String whoName);
    public int getNum(String name);
}
