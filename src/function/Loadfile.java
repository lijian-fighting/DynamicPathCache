package function;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import object.Point;
import object.Query;
import object.Edge;

public class Loadfile {
	private Map<Integer, Point> pointmap = new HashMap<Integer, Point>();
	private Map<Integer, Edge> edgemap = new HashMap<Integer, Edge>();
	private ArrayList<Query> querylist = new ArrayList<Query>();

	// /Users/lijian-mac/Desktop/study/shortestpath/Minneapolis_vertices.txt
	double ChangeX(double inputX) {
		return (inputX / 1000000.0) - 180.0;
	}

	// 纬度
	double ChangeY(double inputY) {
		return 90.0 - (inputY / 1000000.0);
	}
	public void loadpoint(File pointfile) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(pointfile));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String[] temp = tempString.split("\\s+");
				double lng = ChangeX(Long.parseLong(temp[1]));
				double lat = ChangeY(Long.parseLong(temp[2]));
				Point point = new Point(Integer.parseInt(temp[0]), lng, lat);
				for (int i = 0; i < Integer.parseInt(temp[3]); i++) {
					tempString = reader.readLine();
					String[] subItems = tempString.split("\\s+");
					point.addEdgeId(Integer.parseInt(subItems[0]));
				}
				pointmap.put(Integer.parseInt(temp[0]), point);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	public void loadedge(File edgefile) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(edgefile));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String[] temp = tempString.split("\\s+");
				if ((Double.valueOf(temp[5])) > -1) {
					int svid = Integer.parseInt(temp[6]);
					int evid = Integer.parseInt(temp[7]);
					Edge edge = new Edge(Integer.parseInt(temp[0]), pointmap.get(svid), pointmap.get(evid));
					edgemap.put(Integer.parseInt(temp[0]), edge);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}

		}
	}

	public void loadfile() {
		loadpoint(new File("/Users/lijian-mac/Desktop/study/shortestpath/Minneapolis_vertices.txt"));
		loadedge(new File("/Users/lijian-mac/Desktop/study/shortestpath/Minneapolis_edges.txt"));
	}

	public Map<Integer, Point> getpoint() {
		return pointmap;
	}

	public Map<Integer, Edge> getedge() {
		return edgemap;
	}
	public ArrayList<Query>  getquery() {
		return querylist;
	}
}
