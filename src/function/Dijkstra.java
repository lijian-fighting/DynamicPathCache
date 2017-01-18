package function;

import java.util.Map;

import object.Edge;
import object.Point;

public class Dijkstra{
	private static double M = Integer.MAX_VALUE; // 表示此路不通

	public static double[] dijkstra(int start, int end, int[] prev, Map<Integer, Edge> edgemap,
			Map<Integer, Point> pointmap) {
		int n = pointmap.size();
		double[] shortPath = new double[n];
		boolean[] visited = new boolean[n];
		// 初始化，第一个顶点求出
		for (int i = 0; i < n; i++) {
			shortPath[i] = M;
			visited[i] = false;
			prev[i] = -1;
		}
		shortPath[start] = 0;
		for (int count = 0; count <= n - 1; count++) // 要加入n-1个顶点
		{
			double dmin = M;
			int u = -1;
			for (int i = 0; i < n; i++) {
				if (visited[i] == false && shortPath[i] < dmin) {
					dmin = shortPath[i];
					u = i;
				}
			}
			if (dmin == M) {
				break;
			}
			visited[u] = true;

			// 以k为中间点想，修正从start到未访问各点的距离
			for (int eid : pointmap.get(u).getnearbyEdgeID()) {
				int vid = edgemap.get(eid).except(pointmap.get(u)).Getthisid();
				if (visited[vid])
					continue;
				if (dmin + edgemap.get(eid).Getdis() < shortPath[vid]) {
					shortPath[vid] = dmin + edgemap.get(eid).Getdis();
					prev[vid] = u;
				}
			}
			if (visited[end])
				break;
		}
		return shortPath;
	}

	public static String shortestpath(int start, int end, Map<Integer, Point> pointmap, Map<Integer, Edge> edgemap,
			int[] prev) {
		int pointnum = pointmap.size(); // 记录点的数量
		if (start < pointnum && end < pointnum) {
			double[] shortest = dijkstra(start, end, prev, edgemap, pointmap);
			for (int i = 0; i < shortest.length; i++) {
				if (i == end) {
					if (shortest[i] == M) {
						System.out.println("从" + start + "出发到" + i + "无方法到达");
						return "无法到达";
					} else {
						System.out.println("从" + start + "出发到" + i + "的最短距离为：" + shortest[i]);
						String path = "";
						int k = i;
						while (k != start) {
							path = "-->" + Integer.toString(k) + path;
							k = prev[k];
						}
						path = start + path;
						System.out.println("从" + start + "出发到" + i + "的最短路径为：" + path);
						return path;
					}
				}
			}
			System.out.println("从" + start + "出发到" + end + "无方法到达");
			return "无法到达";
		} else {
			System.out.println("没有找到出发点或终点");
			return "没有找到出发点或终点";
		}
	}
}
