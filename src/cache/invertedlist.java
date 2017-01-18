package cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class invertedlist implements Cloneable{
	public  int size = 0;
	private Map<Integer,ArrayList<Integer>> map;
	public invertedlist(){
		this.size = 0;
		this.map = new HashMap<Integer,ArrayList<Integer>>();
	}
	public void addEdgeId(int id,int pathid) {
		this.map.get(id).add(pathid);
		this.map.put(id,this.map.get(id));
	}
	public void deleteEdgeId(int id,int pathid) {
		for(int i = 0;i<this.map.get(id).size();i++){
			if(this.map.get(id).get(i)==pathid){
				this.map.get(id).remove(i);
				this.map.put(id,this.map.get(id));
			}
		}
	}
	public Map<Integer,ArrayList<Integer>> getmap(){
		return this.map;
	}
	
	public void mapput(int id, ArrayList<Integer> node){
		this.map.put(id, node);
		this.size ++;
	}
	
	public void mapout(int id){
		this.map.remove(id);
	}
	
	public int Getsize(){
		return this.size;
	}
	public void clear(){
		this.size = 0;
		this.map.clear();
	}
}
