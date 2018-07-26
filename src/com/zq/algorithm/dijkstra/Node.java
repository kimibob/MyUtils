package com.zq.algorithm.dijkstra;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Node {
	private String name; 
    private Map<Node,Integer> child=new HashMap<Node,Integer>(); 
    public Node(String name){ 
        this.name=name; 
    } 
    public String getName() { 
        return name; 
    } 
    public void setName(String name) { 
        this.name = name; 
    } 
    public Map<Node, Integer> getChild() { 
        return child; 
    } 
    public void setChild(Map<Node, Integer> child) { 
        this.child = child; 
    }
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append("[");
		Set<Map.Entry<Node, Integer>> childs = child.entrySet();
		for (Map.Entry<Node, Integer> c : childs) {
			builder.append(c.getKey().getName() + ":" + c.getValue()+" ");
		}
		builder.append("]");
		return builder.toString();
	}
    
}
