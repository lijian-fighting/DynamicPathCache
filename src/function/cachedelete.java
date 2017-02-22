package function;

import java.util.ArrayList;
import java.util.Map;

import cache.LFUcache;
import cache.LRUcache;
import cache.Subgraph;
import cache.invertedlist;
import cache.pathinformation;

public class cachedelete {

	/**
	 * 用于判断两个点之间是否只有一条路径，并且该路径等于pathid
	 * @param pid NNI中的点
	 * @param id 包含该路径的点
	 * @param nPI 点边表
	 * @param pathid 路径编号
	 * @return true表示只有一条，false表示不是
	 */
	public boolean isonly(int pid,int id,invertedlist nPI,int pathid){
		 ArrayList<Integer> temp1 = new ArrayList<Integer>();	
		 ArrayList<Integer> temp2 = new ArrayList<Integer>();
		if(nPI.getmap().keySet().contains(pid)&&nPI.getmap().keySet().contains(id)) {
			temp1.addAll(nPI.getmap().get(pid));
			temp2.addAll(nPI.getmap().get(id));
			temp1.retainAll(temp2);
			if ((temp1.size() == 1) && (temp1.get(0) == pathid)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 删除点边表和点点表相关内容
	 * @param key 所有包含该路径的点集合
	 * @param nNI 点点表
	 * @param nPI 点边表
	 * @param pathid 路径编号
	 */
	public void deleteNNINPI(ArrayList<Integer> key,Subgraph nNI,invertedlist nPI,int pathid){
		/**
		 * 用于存放所有可直接删除的NNI中的map记录
		 */
		ArrayList<Integer> nid = new ArrayList<Integer>();
		/**
		 * 用于存放所有可直接删除的NPI中的map记录
		 */
		ArrayList<Integer> pid = new ArrayList<Integer>();

		for(Integer i : nNI.getmap().keySet()){
			for(int j = 0;j<key.size();j++){
				if((nNI.getmap().get(i).contains(key.get(j)))&&(isonly(i,key.get(j),nPI,pathid))){
					nNI.deleteEdgeId(i, key.get(j));
					if(nNI.getmap().get(i).size() == 0){
						nid.add(i);
					}
				}
			}
		}
		for(Integer i : nPI.getmap().keySet()){
			if(nPI.getmap().get(i).contains(pathid)){
				nPI.deleteEdgeId(i, pathid);
				if(nPI.getmap().get(i).size() == 0){
					pid.add(i);
				}
			}
		}
		//删除为空的记录
		for(int i = 0;i<nid.size();i++){
			nNI.mapout(nid.get(i));
		}
		for(int i = 0;i<pid.size();i++){
			nPI.mapout(pid.get(i));
		}
	}
	public void deletePII(int pathid,pathinformation pII){
		pII.mapout(pathid);
	}

	/**
	 * 删除LRU
	 * @param pathid 路径编号
	 * @param lru lru路径信息表
	 */
	public void deleteLRU(int pathid,LRUcache lru){
		lru.mapout(pathid);
	}

	/**
	 * 删除LFU
	 * @param pathid 路径编号
	 * @param lfu lfu路径信息表
	 */
	public void deleteLFU(int pathid,LFUcache lfu){
		lfu.mapout(pathid);
	}


	/**
	 * 根据路径编号，删除相关内容
	 * @param pathid 路径编号
	 * @param nNI 点点表
	 * @param pII mytest路径信息表
	 * @param nPI 点边表
	 * @param lru lru路径信息表
	 * @param lfu lfu路径信息表
	 * @param flag 用于判断进行哪种测试，0表示mytest，1表示lru，2表示lfu
	 */
	public void delete(int pathid,Subgraph nNI, pathinformation pII, invertedlist nPI,LRUcache lru,LFUcache lfu,int flag){
		/**
		 * 用于存放所有包含该路径的点集合
		 */
		ArrayList<Integer> key = new ArrayList<Integer>();
		for(Integer m:nPI.getmap().keySet()){
			if(nPI.getmap().get(m).contains(pathid)&&!key.contains(m)){
				key.add(m);
			}
		}
		if(flag == 0){
			deleteNNINPI(key,nNI,nPI,pathid);
			deletePII(pathid,pII);
		}else if(flag == 1){
			deleteNNINPI(key,nNI,nPI,pathid);
			deleteLRU(pathid,lru);
		}else if(flag == 2){
			deleteNNINPI(key,nNI,nPI,pathid);
			deleteLFU(pathid,lfu);
		}
	}
}
