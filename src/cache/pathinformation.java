package cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class pathinformation implements Cloneable{
	public  int size = 0;
	private int pathid;
	private int sid;
	private int eid;
	private int time;
	private double weight;
	private Map<Integer,pathinformation> map = new HashMap<Integer,pathinformation>();
	public void Setpathid(int id){
		this.pathid = id;
	}
	
	public int Getpathid(){
		return this.pathid;
	}
	public void Setsid(int id){
		this.sid = id;
	}
	
	public int Getsid(){
		return this.sid;
	}
	public void Seteid(int id){
		this.eid = id;
	}
	
	public int Geteid(){
		return this.eid;
	}
	public void Settime(int date){
		this.time = date;
	}
	
	public int Gettime(){
		return this.time;
	}
	public void Setweight(double we){
		this.weight = we;
	}
	
	public double Getweight(){
		return this.weight;
	}
	public Map<Integer, pathinformation> getmap(){
		return this.map;
	}
	public void mapput(int pathid, pathinformation path){
		this.map.put(pathid, path);
		this.size ++;
	}
	public void mapout(int pathid){
		this.map.remove(pathid);
	}
	public int Getsize(){
		return this.size;
	}
	public void clear(){
		this.size = 0;
		this.map.clear();
	}
}
