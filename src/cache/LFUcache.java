package cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LFUcache {
	public  int size = 0;
	private int pathid;
	private int sid;
	private int eid;
	private int count = 0;
	private int time;
	private Map<Integer,LFUcache> map = new HashMap<Integer,LFUcache>();
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
	public void Setcount(){
		this.count++;
	}
	
	public int Getcount(){
		return this.count;
	}
	
	public void Settime(int date){
		this.time = date;
	}
	
	public int Gettime(){
		return this.time;
	}
	public Map<Integer, LFUcache> getmap(){
		return this.map;
	}
	public void mapput(int pathid, LFUcache lfu){
		this.map.put(pathid, lfu);
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
