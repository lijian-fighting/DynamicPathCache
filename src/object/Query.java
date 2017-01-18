package object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Query {
	private int id = 0;
	private Map<Integer,ArrayList<String>> map;	
	public Query(){
		map = new HashMap<Integer,ArrayList<String>>();
	}
	public void mapput(ArrayList<String> qslist){
		this.id++;
		this.map.put(id, qslist);
	}
	public Map<Integer, ArrayList<String>> getmap(){
		return this.map;
	}
	public int getid(){
		return this.id;
	}
}
