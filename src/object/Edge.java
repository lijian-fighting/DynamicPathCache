package object;

public class Edge {
	int edgeid;
	Point start;
	Point end;
	double dis;
	public Edge(int edgeid,Point start,Point end){
		this.edgeid = edgeid;
		this.start = start;
		this.end = end;
		this.dis = start.distance(end.Getplng(),end.Getplat());
	}
	public int Getedgeid(){
		return this.edgeid;
	}
	
	public Point Getstart(){
		return this.start;
	}
	public Point Getend(){
		return this.end;
	}
	public double Getdis(){
		return this.dis;
	}
	
	public Point except(Point v) {
		if (v.equals(end)) {
			return start;
		} else {
			return end;
		}
	}
}
