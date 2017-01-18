package cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Subgraph implements Cloneable{
	public  int size = 0;
	public Map<Integer,ArrayList<Integer>> map ;
	
	public Subgraph(){
		this.size = 0;
		this.map = new HashMap<Integer,ArrayList<Integer>>();
	}
	
	public void addEdgeId(int id,int pid) {
		this.map.get(id).add(pid);
		this.map.put(id,this.map.get(id));
	}
	
	public void deleteEdgeId(int id,int pid) {
		for(int i = 0;i<this.map.get(id).size();i++){
			if(this.map.get(id).get(i)==pid){
				this.map.get(id).remove(i);
				this.map.put(id,this.map.get(id));
			}
		}
	}
	
	public void mapout(int id){
		this.map.remove(id);
	}
	
	public Map<Integer,ArrayList<Integer>> getmap(){	
		return this.map;
	}
	
	public void mapput(int id, ArrayList<Integer> near){
		this.map.put(id, near);
		this.size ++;
	}
	
	public int Getsize(){
		return this.size;
	}
	public void clear(){
		this.size = 0;
		this.map.clear();
	}

}
