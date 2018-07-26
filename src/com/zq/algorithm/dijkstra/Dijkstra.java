package com.zq.algorithm.dijkstra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 算法步骤：
 * a.初始时，S只包含源点，即S＝{v}，v的距离为0。U包含除v外的其他顶点，即:U={其余顶点}，若v与U中顶点u有边，则<u,v>正常有权值，若u不是v的出边邻接点，则<u,v>权值为∞。
 * b.从U中选取一个距离v最小的顶点k，把k，加入S中（该选定的距离就是v到k的最短路径长度）。
 * c.以k为新考虑的中间点，修改U中各顶点的距离；若从源点v到顶点u的距离（经过顶点k）比原来距离（不经过顶点k）短，则修改顶点u的距离值，修改后的距离值的顶点k的距离加上边上的权。
 * d.重复步骤b和c直到所有顶点都包含在S中。
 *
 *1.狄克斯特拉算法用于在加权图中查找最短路径。 
 *2.仅当权重为正时狄克斯特拉算法才管用。 
 *3.如果图中包含负权边，请使用贝尔曼-福德算法。 
 */
public class Dijkstra {

	Set<Node> open = new HashSet<Node>();// 已求出最短路径的节点集合S
	Set<Node> close = new HashSet<Node>();// 未求出最短路径的节点集合U
	Map<String, Integer> path = new HashMap<String, Integer>();// 封装路径距离
	Map<String, String> pathInfo = new HashMap<String, String>();// 封装路径信息

	/*
	 * 初始化:步骤a
	 */
	public Node init() {
		// 初始路径,因没有A->E这条路径,所以path(E)设置为Integer.MAX_VALUE
//		 path.put("B", 6);
//		 pathInfo.put("B", "A->B");
//		 path.put("C", 3);
//		 pathInfo.put("C", "A->C");
//		 path.put("D", Integer.MAX_VALUE);
//		 pathInfo.put("D", "A");
//		 path.put("E", Integer.MAX_VALUE);
//		 pathInfo.put("E", "A");
//		 path.put("F", Integer.MAX_VALUE);
//		 pathInfo.put("F", "A");

		path.put("2", 10);
		pathInfo.put("2", "1->2");
		path.put("3", Integer.MAX_VALUE);
		pathInfo.put("3", "1");
		path.put("4", 30);
		pathInfo.put("4", "1->4");
		path.put("5", 100);
		pathInfo.put("5", "1->5");

		// 将初始节点放入close,其他节点放入open
		Node start = new MapBuilder().build(open, close);
		return start;
	}

	public void computePath(Node start) {
		System.out.println(open + "--" + close);
		Node nearest = getShortestPath(start);// 取距离start节点最近的子节点,放入close
		if (nearest == null) {
			return;
		}

		//步骤c
		Map<Node, Integer> childs = nearest.getChild();
		for (Node child : childs.keySet()) {
			if (open.contains(child)) {// 如果子节点在open中
				Integer newCompute = path.get(nearest.getName())
						+ childs.get(child);
				if (path.get(child.getName()) > newCompute) {// 之前设置的距离大于新计算出来的距离
//					System.out.print("目前到节点：" + child.getName() + " 路径："
//							+ pathInfo.get(child.getName()) + " 距离："
//							+ path.get(child.getName()) + " 发现更短距离："
//							+ newCompute);
					path.put(child.getName(), newCompute);
					pathInfo.put(
							child.getName(),
							pathInfo.get(nearest.getName()) + "->"
									+ child.getName());
					//System.out.println(" " + pathInfo.get(child.getName()));
				}
			}
		}
		computePath(nearest);// 向外一层层递归,直至所有顶点被遍历
	}

	/**
	 * 步骤b:找出目前与源点最近的节点
	 */
	private Node getShortestPath(Node node) {
		Node res = null;
		int minDis = Integer.MAX_VALUE;
		//这种先遍历与源节点相邻的最近节点的方法是错误的
//		Map<Node, Integer> childs = node.getChild();
//		for (Node child : childs.keySet()) {
//			if (open.contains(child)) {
//				int distance = childs.get(child);
//				if (distance < minDis) {
//					minDis = distance;
//					res = child;
//				}
//			}
//		}
		
		for (Node opend_node : open) {
			if(path.get(opend_node.getName()) < minDis){
				minDis = path.get(opend_node.getName());
				res = opend_node;
			}
		}
		close.add(res);
		open.remove(res);
		return res;
	}

	public void printPathInfo() {
		Set<Map.Entry<String, String>> pathInfos = pathInfo.entrySet();
		for (Map.Entry<String, String> pathInfo : pathInfos) {
			System.out.println(pathInfo.getKey() + ":" + pathInfo.getValue());
		}
		Set<Map.Entry<String, Integer>> paths = path.entrySet();

		for (Map.Entry<String, Integer> path : paths) {
			System.out.println(path.getKey() + ":" + path.getValue());
		}
	}

	public static void main(String[] args) {
		Dijkstra test = new Dijkstra();
		Node start = test.init();
		test.computePath(start);
		test.printPathInfo();
	}
}
