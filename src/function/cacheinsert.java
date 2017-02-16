package function;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.HashMap;

import cache.LFUcache;
import cache.LRUcache;
import cache.Subgraph;
import cache.invertedlist;
import cache.pathinformation;

public class cacheinsert {
	public long timemax;
	public double A;                                                ;
	public int size;
	public double MAX = 50.0;
	public double max_value = 0.0;

	/**
	 * 用于计算路径的影响
	 * @param key 点
	 * @param nPI 点边表
	 * @return 点的影响
	 */
	public double cal(int key, invertedlist nPI){
		int count = 0;
		int num = 0;
		for(Integer temp:nPI.getmap().keySet()){
			if(nPI.getmap().get(temp).contains(key)){
				count = count + nPI.getmap().get(temp).size();
				num = num + 1;
			}
		}
		return (count*1.0/num);
	}

	/**
	 * 比较两个浮点数的大小
	 * @param a
	 * @param b
	 * @return 第一个小于第二个，返回true，否则返回false
	 */
	public boolean judge(double a,double b){
		BigDecimal data1 = new BigDecimal(a);
		BigDecimal data2 = new BigDecimal(b);
		if(data1.compareTo(data2)<0){
			return true;
		}
		return false;
	}

	/**
	 * 返回mytest路径信息表中的最大值
	 * @param map
	 * @param pII
	 * @param we
	 * @param node
	 * @return double类型的最大值
	 */
	public void maxweight(Map<Integer, Double> map, pathinformation pII, invertedlist nPI, double[] we, ArrayList<Integer> node,Date time) {
		Map<Integer, Double> po = new HashMap<Integer,Double>();
		double max = 0;
		for (Integer key : pII.getmap().keySet()) {
			double temp = cal(key,nPI);
			po.put(key,temp);
			if (judge(max,temp)) {
				max = temp;
			}
		}
		if(judge(max_value,max)){
			max_value = max;
		}
		int sum = 0;
		for(Integer key : nPI.getmap().keySet()){
			if(node.contains(key)){
				sum = sum + nPI.getmap().get(key).size();
			}
		}
		we[0] = A*(sum*1.0/max) + (1 - A);
		for(Integer key:po.keySet()){
			double temp = pathweight(po.get(key),max,time,pII.getmap().get(key).Gettime());
			map.put(key,temp);
		}
	}

	/**
	 * 向mytest路径信息表中添加信息
	 * @param start 查询起点
	 * @param end 查询终点
	 * @param pII mytest路径信息表
	 * @param time 查询时间
	 * @param pathid 路径编号
	 */
	public void insertPII(int start, int end, pathinformation pII, Date time, int pathid) {
		pathinformation p = new pathinformation();
		p.Setpathid(pathid);
		p.Setsid(start);
		p.Seteid(end);
		p.Settime(time);
		pII.mapput(pathid, p);
	}

	/**
	 * 向NPI中添加记录
	 * @param node API查询结果的点集合
	 * @param nPI 点边表
	 * @param pathid 路径编号
	 */
	public void insertNPI(ArrayList<Integer> node, invertedlist nPI, int pathid) {
		/**
		 * 存放所有NPI的节点集合
		 */
		for (int i = 0; i < node.size(); i++) {
			if (nPI.getmap().keySet().contains(node.get(i))) {
				nPI.addEdgeId(node.get(i), pathid);
			} else {
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(pathid);
				nPI.mapput(node.get(i), temp);
			}
		}
	}

	/**
	 * NNI的插入操作
	 * @param nNI 点点表
	 * @param pid key值
	 * @param node value值
	 */
	public void in(Subgraph nNI, int pid, int node) {
		/**
		 * 用于存放当前点点表中的所有点
		 */
		if (nNI.getmap().keySet().contains(pid)) {
			//如果NNI中已经包含了key值，则直接在该记录后面增加，如果已经有了，就不添加
			if (!nNI.getmap().get(pid).contains(node)) {
				nNI.addEdgeId(pid, node);
			}
		} else {
			//如果NNI没有包含key值，则新增这条记录
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(node);
			nNI.mapput(pid, temp);
		}
	}

	/**
	 * 向点点表中添加记录
	 * @param node API返回的查询结果
	 * @param nNI 点点表
	 */
	public void insertNNI(ArrayList<Integer> node, Subgraph nNI) {
		if(node.size() >1){
			for (int i = 0; i < node.size(); i++) {
				if (i == 0) {
					in(nNI, node.get(i), node.get(i + 1));
				} else if (i == node.size() - 1) {
					in(nNI, node.get(i), node.get(i - 1));
				} else {
					in(nNI, node.get(i), node.get(i + 1));
					in(nNI, node.get(i), node.get(i - 1));
				}
			}
		}
	}

	/**
	 * 用于返回那些已经在缓存中，并且起点和终点都在API返回节点中的路径编号
	 * @param node API返回的所有节点
	 * @param pII mytest路径信息表
	 * @param lru lru路径信息表
	 * @param lfu lfu路径信息表
	 * @param flag 用于判断进行哪种测试，0表示mytest，1表示lru，2表示lfu
	 * @return 用于返回那些已经在缓存中，并且起点和终点都在API返回节点中的路径编号
	 */
	public ArrayList<Integer> isexist(ArrayList<Integer> node, pathinformation pII, LRUcache lru, LFUcache lfu,
			int flag) {
		/**
		 * 用于存放那些已经在缓存中，并且起点和终点都在API返回节点中的路径
		 */
		ArrayList<Integer> pathid = new ArrayList<Integer>();
		if (flag == 0) {
			for (Integer key : pII.getmap().keySet()) {
				if ((node.contains(pII.getmap().get(key).Getsid()))
						&& (node.contains(pII.getmap().get(key).Geteid()))) {
					pathid.add(pII.getmap().get(key).Getpathid());
				}
			}
		} else if (flag == 1) {
			for (Integer key : lru.getmap().keySet()) {
				if ((node.contains(lru.getmap().get(key).Getsid()))
						&& (node.contains(lru.getmap().get(key).Geteid()))) {
					pathid.add(lru.getmap().get(key).Getpathid());
				}
			}
		} else if (flag == 2) {
			for (Integer key : lfu.getmap().keySet()) {
				if ((node.contains(lfu.getmap().get(key).Getsid()))
						&& (node.contains(lfu.getmap().get(key).Geteid()))) {
					pathid.add(lfu.getmap().get(key).Getpathid());
				}
			}
		}
		return pathid;
	}

	/**
	 * 用于计算所有的节点数量
	 * @param node API返回的查询节点
	 * @param nNI 点点表
	 * @return 所有的节点数量
	 */
	public int allnode(ArrayList<Integer> node, Subgraph nNI) {
		int sum = 0;
		sum = sum + node.size();
		for (Integer key : nNI.getmap().keySet()) {
			if (!node.contains(key)) {
				sum = sum + 1;
			}
		}
		return sum;
	}

	/**
	 * 计算某条路的最终权重信息
	 * @param temp
	 * @param max
	 * @param time
	 * @param dt
	 * @return
	 */
	public double pathweight(double temp, double max, Date dt,Date time) {
		long interval = (dt.getTime() - time.getTime()) / 1000;
		return A * (temp / max) + (1 - A) * (((timemax - interval) * 1.0) / timemax);
	}

	/**
	 * 计算需要删除的节点
	 * @param map
	 * @param nPI
	 * @return
	 */
	public int nodedelete(ArrayList<Integer> map, invertedlist nPI) {
		int count = 0;
		for(Integer path : map){
			for(Integer key:nPI.getmap().keySet()){
				if(nPI.getmap().get(key).size() == 1 && nPI.getmap().get(key).get(0).intValue() == path.intValue()){
					count = count + 1;
				}
			}
		}
		return count;
	}
	// pII添加,同时返回路径id
	public void insertPII1(int start, int end, pathinformation pII, Date time, int pathid, ArrayList<Integer> node,
						  invertedlist nPI) {
		pathinformation p = new pathinformation();
		p.Setpathid(pathid);
		p.Setsid(start);
		p.Seteid(end);
		p.Settime(time);
		// 权重没有添加
		int sum = 0;
		for(Integer key : nPI.getmap().keySet()){
			if(node.contains(key)){
				sum = sum + nPI.getmap().get(key).size();
			}
		}
		double weight = (sum * 1.0) / node.size();
		p.Setweight(weight);
		pII.mapput(pathid, p);
	}
	
	public boolean update_MAX(pathinformation pII, Subgraph nNI, invertedlist nPI, ArrayList<Integer> node,
						  cachedelete delete, ArrayList<Integer> path, Date time) {
		 Queue<Key_value> H = new PriorityQueue<Key_value>(new Comparator<Key_value>() {
	            @Override
	            public int compare(Key_value o1, Key_value o2) {
	                BigDecimal data1 = new BigDecimal(o1.getvalue());
	                BigDecimal data2 = new BigDecimal(o2.getvalue());
	                int flag = data1.compareTo(data2);
	                if (flag > 0)
	                    return 1;
	                else if (flag == 0)
	                    return 0;
	                else
	                    return -1;
	            }
	        });
		 Map<Integer, Double> listweight = new HashMap<Integer, Double>();
		for (Integer key : pII.getmap().keySet()) {
			listweight.put(key,
						pathweight(pII.getmap().get(key).Getweight(),MAX, time,pII.getmap().get(key).Gettime() ));
		}
		int sum = 0;
		for(Integer key : nPI.getmap().keySet()){
			if(node.contains(key)){
				sum = sum + nPI.getmap().get(key).size();
			}
		}
		double weight = (sum * 1.0) / node.size();
		for(Integer key:listweight.keySet()){
			Key_value temp = new Key_value(key,listweight.get(key));
			H.add(temp);
		}
		
		int allnode = allnode(node, nNI);
		ArrayList<Integer> map = new ArrayList<Integer>();
		// 存放每次权重最低的路径
		int count = 0;
		while (allnode > size) {
			Key_value temp = H.poll();
			if (judge((A * (weight / MAX) + (1-A)), temp.getvalue())){
				if (!path.contains(temp.getkey())) {
					map.add(temp.getkey());
					allnode = allnode - nodedelete(map, nPI);
				}
				count++;
				if (count == listweight.size()) {
					return false;
				}
			} else {
				return false;
			}
		}
		for (int i = 0; i < path.size(); i++) {
			delete.delete(path.get(i), nNI, pII, nPI, null, null, 0);
		}
		for (int i = 0; i < map.size(); i++) {
			delete.delete(map.get(i), nNI, pII, nPI, null, null, 0);
		}
		return true;
	}
	/**
	 * mytest缓存更新
	 * @param pII mytest路径信息表
	 * @param nNI 点点表
	 * @param nPI 点边表
	 * @param node API返回结果集
	 * @param delete delete类
	 * @param path 那些已经在缓存中，并且起点和终点都在API返回节点中的路径编号
	 * @param time 查询时间
	 * @return 能否更新，true表示可以更新，false表示不能更新
	 */
	public boolean update(pathinformation pII, Subgraph nNI, invertedlist nPI, ArrayList<Integer> node,
			cachedelete delete, ArrayList<Integer> path, Date time) {
		 Queue<Key_value> H = new PriorityQueue<Key_value>(new Comparator<Key_value>() {
	            @Override
	            public int compare(Key_value o1, Key_value o2) {
	                BigDecimal data1 = new BigDecimal(o1.getvalue());
	                BigDecimal data2 = new BigDecimal(o2.getvalue());
	                int flag = data1.compareTo(data2);
	                if (flag > 0)
	                    return 1;
	                else if (flag == 0)
	                    return 0;
	                else
	                    return -1;
	            }
	        });
		/**
		 * weight[0] 表示当前权重，weight1表示最大权重
		 */
		double []weight = {0.0,0.0};
		/**
		 * 存放根据权重进行排序后的mytest路径信息表
		 */
		Map<Integer, Double> listweight = new HashMap<Integer,Double>();
		maxweight(listweight,pII,nPI,weight,node,time);
		for(Integer key:listweight.keySet()){
			Key_value temp = new Key_value(key,listweight.get(key));
			H.add(temp);
		}
		
		int allnode = allnode(node, nNI);
		ArrayList<Integer> map = new ArrayList<Integer>();
		// 存放每次权重最低的路径
		int count = 0;
		while (allnode > size) {
			Key_value temp = H.poll();
			if (judge(weight[0],temp.getvalue())) {
				if (!path.contains(temp.getkey())) {
					map.add(temp.getkey());
					allnode = allnode - nodedelete(map, nPI);
				}
				count++;
				if (count == H.size()) {
					return false;
				}
			} else {
				return false;
			}
		}
		for (int i = 0; i < path.size(); i++) {
			delete.delete(path.get(i), nNI, pII, nPI, null, null, 0);
		}
		for (int i = 0; i < map.size(); i++) {
			delete.delete(map.get(i), nNI, pII, nPI, null, null, 0);
		}
		return true;
	}

	/**
	 * 进行mytest缓存的更新
	 * @param start 查询的起点
	 * @param end 查询的终点
	 * @param shortpath API返回的查询结果
	 * @param nNI 点点表
	 * @param pII mytest路径缓存信息
	 * @param nPI 点边表
	 * @param time 查询时间
	 * @param lru lru路径信息表
	 * @param lfu lfu路径信息表
	 * @param flag 用于判断进行哪种测试，0表示mytest，1表示lru，2表示lfu
	 */
	public void insert(int start, int end, ArrayList<Integer> shortpath, Subgraph nNI, pathinformation pII, invertedlist nPI, Date time,
			LRUcache lru, LFUcache lfu, int flag,double a, long timeMax, int cachesize,boolean isweightupdate) {
		cachedelete delete = new cachedelete();
		/**
		 * pathid 用于存放下一个pathid(获取最大的已有size)
		 */
		this.A = a;
		this.timemax = timeMax;
		this.size = cachesize;
		int pathid = 0;
		if(flag == 0){
			pathid = pII.Getsize() + 1;
		}else if(flag == 1){
			pathid = lru.Getsize() + 1;
		}else if(flag == 2){
			pathid = lfu.Getsize() + 1;
		}

		/**
		 * 用于存放那些已经在缓存中，并且起点和终点都在API返回节点中的路径编号
		 */
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.addAll(isexist(shortpath, pII, lru, lfu, flag)); // 存放相同路径的开始
		
		//System.out.println(path);
		//System.out.println(allnode(shortpath,nNI));
		
		if (allnode(shortpath, nNI) <= size) { // 直接加入路径
			//表示当前缓存并没有满,删除当前所有已经被包含的路径
			for (int i = 0; i < path.size(); i++) {
				delete.delete(path.get(i), nNI, pII, nPI, lru, lfu, flag);
			}
			insertNNI(shortpath, nNI);
			insertNPI(shortpath, nPI, pathid);
			//根据flag进行对应的路径信息表的插入
			if (flag == 0) {
				if(isweightupdate) {
					insertPII(start, end, pII, time, pathid);
				}else {
					insertPII1(start, end, pII, time, pathid, shortpath, nPI); // 不实时更新
				}
			} else if (flag == 1) {
				insertLRU(start, end, lru, pathid, time);
			} else if (flag == 2) {
				insertLFU(start, end, lfu, pathid, time);
			}
		} else {
			// 路径已经满了 删除之后再插入
			if (flag == 0) {
				if(isweightupdate) {
					if (update(pII, nNI, nPI, shortpath, delete, path, time)) {
						insertNNI(shortpath, nNI);
						insertNPI(shortpath, nPI, pathid);
						insertPII(start, end, pII, time, pathid);
					}//实时更新
				} else {
					if (update_MAX(pII, nNI, nPI, shortpath, delete, path, time)) {
						insertNNI(shortpath, nNI);
						insertNPI(shortpath, nPI, pathid);
						insertPII1(start, end, pII, time, pathid, shortpath, nPI);
					}//不实时更新
				}
			} else if (flag == 1) {
				if (update1(lru, nNI, nPI, shortpath, delete, path)) {
					insertNNI(shortpath, nNI);
					insertNPI(shortpath, nPI, pathid);
					insertLRU(start, end, lru, pathid, time);
				}
			} else if (flag == 2) {
				if (update2(lfu, nNI, nPI, shortpath, delete, path)) {
					insertNNI(shortpath, nNI);
					insertNPI(shortpath, nPI, pathid);
					insertLFU(start, end, lfu, pathid, time);
				} 
			}
		}
		/*write("*****NNI****"+"\n");
		for(Integer key : nNI.getmap().keySet()){
			write("key = "+ key + "value =" + nNI.getmap().get(key)+"\n");
		}
		write("*****NPI****");
		for(Integer key : nPI.getmap().keySet()){
			write("key = "+ key + "value =" + nPI.getmap().get(key)+"\n");
		}
		write("****PII*****");
		for(Integer key : pII.getmap().keySet()){
			write("key = "+ key + "sid =" + pII.getmap().get(key).Getsid()+"eid =" + pII.getmap().get(key).Geteid()+"time =" + pII.getmap().get(key).Gettime()+"\n");
		}*/
	}


	/**
	 * 进行lfu缓存更新
	 * @param lfu lfu路径信息表
	 * @param nNI 点点表
	 * @param nPI 点边表
	 * @param node API返回节点
	 * @param delete cachedelete类
	 * @param path 存放那些已经在缓存中，并且起点和终点都在API返回节点中的路径编号
	 * @return 能否更新lfu
	 */
	public boolean update2(LFUcache lfu, Subgraph nNI, invertedlist nPI, ArrayList<Integer> node, cachedelete delete,
			ArrayList<Integer> path) {
		 Queue<Key_count> H = new PriorityQueue<Key_count>(new Comparator<Key_count>() {
	            @Override
	            public int compare(Key_count o1, Key_count o2) {
	            	int flag = o1.getcount() - o2.getcount();
	                if (flag > 0)
	                    return 1;
	                else if (flag == 0)
	                    return 0;
	                else
	                    return -1;
	            }
	        });
		int allnode = allnode(node, nNI);
		for(Integer key:lfu.getmap().keySet()){
			Key_count temp = new Key_count(key,lfu.getmap().get(key).Getcount());
			H.add(temp);
		}
		ArrayList<Integer> map = new ArrayList<Integer>();
		int count = 0;
		while (allnode > size) {
			Key_count temp = H.poll();
			if (!path.contains(temp.getkey())) {
				map.add(temp.getkey());
				allnode = allnode - nodedelete(map, nPI);
			}
			count++;
			if (count == H.size()) {
				return false;
			}
		}
		for (int i = 0; i < path.size(); i++) {
			delete.delete(path.get(i), nNI, null, nPI, null, lfu, 2);
		}
		for (int i = 0; i < map.size(); i++) {
			delete.delete(map.get(i), nNI, null, nPI, null, lfu, 2);
		}
		return true;
	}

	/**
	 * 进行lru更新
	 * @param lru lru路径信息表
	 * @param nNI 点点表
	 * @param nPI 点边表
	 * @param node API返回的节点
	 * @param delete cachedelete类
	 * @param path 存放那些已经在缓存中，并且起点和终点都在API返回节点中的路径编号
	 * @return 返回能否更新
	 */
	public boolean update1(LRUcache lru, Subgraph nNI, invertedlist nPI, ArrayList<Integer> node, cachedelete delete, ArrayList<Integer> path) {
		/**
		 * 存放当前所有节点
		 */
		 Queue<Key_Date> H = new PriorityQueue<Key_Date>(new Comparator<Key_Date>() {
	            @Override
	            public int compare(Key_Date o1, Key_Date o2) {
	            	int flag = o1.gettime().compareTo(o2.gettime());
	                if (flag > 0)
	                    return 1;
	                else if (flag == 0)
	                    return 0;
	                else
	                    return -1;
	            }
	        });
		int allnode = allnode(node, nNI);
		for(Integer key:lru.getmap().keySet()){
			Key_Date temp = new Key_Date(key,lru.getmap().get(key).Gettime());
			H.add(temp);
		}
		ArrayList<Integer> map = new ArrayList<Integer>();
		int count = 0;
		while (allnode > size) {
			Key_Date temp = H.poll();
			if (!path.contains(temp.getkey())) {
				map.add(temp.getkey());
				allnode = allnode - nodedelete(map, nPI);
			}
			count++;
			if (count == H.size()) {
				return false;
			}
		}
		for (int i = 0; i < path.size(); i++) {
			delete.delete(path.get(i), nNI, null, nPI, lru, null, 1);
		}
		for (int i = 0; i < map.size(); i++) {
			delete.delete(map.get(i), nNI, null, nPI, lru, null, 1);
		}
		return true;
	}

	public void insertLFU(int start, int end, LFUcache lfu, int pathid,  Date time) {
		LFUcache p = new LFUcache();
		p.Setpathid(pathid);
		p.Setsid(start);
		p.Seteid(end);
		p.Settime(time);
		lfu.mapput(pathid, p);
	}

	public void insertLRU(int start, int end, LRUcache lru, int pathid,  Date time) {
		LRUcache p = new LRUcache();
		p.Setpathid(pathid);
		p.Setsid(start);
		p.Seteid(end);
		p.Settime(time);
		lru.mapput(pathid, p);
	}
	public double getmax_value(){
		return this.max_value;
	}
}

class Key_Date{
    private int key;
    private Date time;
    public int getkey(){
        return this.key;
    }
    public Date gettime(){
        return this.time;
    }
    Key_Date(int key,Date time){
        this.key = key;
        this.time = time;
    }
}
class Key_count{
    private int key;
    private int count;
    public int getkey(){
        return this.key;
    }
    public int getcount(){
        return this.count;
    }
    Key_count(int key,int count){
        this.key = key;
        this.count = count;
    }
}
