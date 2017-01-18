package object;

import java.util.ArrayList;

public class Point {
	private static double DEF_PI = 3.14159265359; // PI
    private static double DEF_2PI= 6.28318530712; // 2*PI
    private static double DEF_PI180= 0.01745329252; // PI/180.0
    private static double DEF_R =6370693.5; // radius of earth
	private int thisid = 0;
	private double plng;
	private double plat;
	private ArrayList<Integer> nearbyEdgeId;
	
	public Point(int thisid,double plng,double plat){
		this.thisid = thisid;
		this.plng = plng;
		this.plat = plat;
		nearbyEdgeId = new ArrayList<Integer>();
	}
	public Point(String s){
		String []temp = s.split(",");
		this.thisid++;
		this.plng = Double.parseDouble(temp[1]);
		this.plat = Double.parseDouble(temp[0]);
		nearbyEdgeId = new ArrayList<Integer>();
	}
	public int Getthisid(){
		return this.thisid;
	}
	
	public double Getplng(){
		return this.plng;
	}
	public double Getplat(){
		return this.plat;
	}
	public ArrayList<Integer> getnearbyEdgeID(){
		return this.nearbyEdgeId;
	}
	public void addEdgeId(int id) {
		this.nearbyEdgeId.add(id);
	}
		
	public double distance(double lng,double lat){
		double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = this.plng * DEF_PI180;
        ns1 = this.plat * DEF_PI180;
        ew2 = lng * DEF_PI180;
        ns2 = lat * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
	}
	
	public boolean equals(Point p) {
		return (this.plng - p.plng) * (this.plng - p.plng) < 1e-9
				&& (this.plat - p.plat) * (this.plat - p.plat) < 1e-9;
	}
}
