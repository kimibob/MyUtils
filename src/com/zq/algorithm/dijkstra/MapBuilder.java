package com.zq.algorithm.dijkstra;

import java.util.Set;

public class MapBuilder {
	public Node build(Set<Node> open, Set<Node> close) {
//		Node nodeA = new Node("A");
//		Node nodeB = new Node("B");
//		Node nodeC = new Node("C");
//		Node nodeD = new Node("D");
//		Node nodeE = new Node("E");
//		Node nodeF = new Node("F");
//		nodeA.getChild().put(nodeB, 6);
//		nodeA.getChild().put(nodeC, 3);
//		
//		nodeB.getChild().put(nodeA, 6);
//		nodeB.getChild().put(nodeC, 2);
//		nodeB.getChild().put(nodeD, 5);
//
//		nodeC.getChild().put(nodeA, 3);
//		nodeC.getChild().put(nodeB, 2);
//		nodeC.getChild().put(nodeD, 3);
//		nodeC.getChild().put(nodeE, 4);
//		
//		nodeD.getChild().put(nodeB, 5);
//		nodeD.getChild().put(nodeC, 3);
//		nodeD.getChild().put(nodeE, 2);
//		nodeD.getChild().put(nodeF, 3);
//		
//		nodeE.getChild().put(nodeC, 4);
//		nodeE.getChild().put(nodeD, 2);
//		nodeE.getChild().put(nodeF, 5);
//		
//		nodeF.getChild().put(nodeD, 3);
//		nodeF.getChild().put(nodeE, 5);
//		
//		open.add(nodeB);
//		open.add(nodeC);
//		open.add(nodeD);
//		open.add(nodeE);
//		open.add(nodeF);
//		close.add(nodeA);
//		return nodeA;
		Node node1 = new Node("1");
		Node node2 = new Node("2");
		Node node3 = new Node("3");
		Node node4 = new Node("4");
		Node node5 = new Node("5");
		node1.getChild().put(node2, 10);
		node1.getChild().put(node4, 30);
		node1.getChild().put(node5, 100);

		node2.getChild().put(node1, 10);
		node2.getChild().put(node3, 50);
		
		node3.getChild().put(node2, 50);
		node3.getChild().put(node4, 20);
		node3.getChild().put(node5, 10);
		
		node4.getChild().put(node1, 30);
		node4.getChild().put(node3, 20);
		node4.getChild().put(node5, 60);
		
		node5.getChild().put(node1, 100);
		node5.getChild().put(node3, 10);
		node5.getChild().put(node4, 60);
		
		open.add(node2);
		open.add(node3);
		open.add(node4);
		open.add(node5);
		close.add(node1);
		return node1;
	}
}
