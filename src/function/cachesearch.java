package function;

import java.util.ArrayList;
import java.util.Date;

import cache.LFUcache;
import cache.LRUcache;
import cache.Subgraph;
import cache.invertedlist;
import cache.pathinformation;

public class cachesearch {
	/**
	 * 用于找到两个点的共同路径
	 * @param start 查询起点
	 * @param end 查询终点
	 * @param nPI 点边表
	 * @param coincide 用于存放两个点的相同路径
	 */
	public void iscoincide(int start, int end, invertedlist nPI, ArrayList<Integer> coincide) {
		ArrayList<Integer> temp1 = new ArrayList<Integer>();
		ArrayList<Integer> temp2 = new ArrayList<Integer>();
		temp1.addAll(nPI.getmap().get(start));
		temp2.addAll(nPI.getmap().get(end));
		temp1.retainAll(temp2);
		coincide.addAll(temp1);
		return;
	}

	/**
	 * 用于判断某个点是否属于某条路径
	 * @param pathid 路径编号
	 * @param pid 点的编号
	 * @param nPI 点边表
	 * @return true表示属于，false表示不属于
	 */
	public boolean ispath(int pathid, int pid, invertedlist nPI) {
		if (nPI.getmap().get(pid).contains(pathid)) {
			return true;
		}
		return false;
	}

	/**
	 * 用于在缓存中搜索路径
	 * @param start 查询起点
	 * @param end 查询终点
	 * @param nNI 点点表
	 * @param nPI 点边表
	 * @param pathid 路径编号
	 * @param shortpath 存放搜索结果
	 */
	public void search(int start, int end, Subgraph nNI, invertedlist nPI, int pathid, ArrayList<Integer> shortpath) {

		ArrayList<Integer> choice = new ArrayList<Integer>();
		if (start == end) {
			shortpath.add(end);
			return;
		} else {
			shortpath.add(start);
			/**
			 * 对于某个点的所有邻接点，如果这个点包含跟起始节点相同的路径编号，并且shortpath中没有这个点，就将这个点放入选择中
			 */
			for (int i = 0; i < nNI.getmap().get(start).size(); i++) {
				if (ispath(pathid, nNI.getmap().get(start).get(i), nPI) && !shortpath.contains(nNI.getmap().get(start).get(i))) {
					choice.add(nNI.getmap().get(start).get(i));
				}
			}
			/**
			 * 如果选择为空，就返回
			 */
			if (choice.size() == 0) {
				return;
			}
			/**
			 * 将候选节点作为起点依次进行预测
			 */
			for (int i = 0; i < choice.size(); i++) {
				search(choice.get(i), end, nNI, nPI, pathid, shortpath);
				if (shortpath.size() != 0 && shortpath.get(shortpath.size() - 1) == end) {
					break;
				}else{
					for (int j = 0; j < shortpath.size(); j++) {
						if (shortpath.get(j) == choice.get(i)) {
							shortpath.remove(j);
						}
					}
				}
			}
		}
	}

	/**
	 * 用于进行时间判断，并返回最终路径编号
	 * @param time 时间集合
	 * @param temp 路径集合
	 * @return 返回时间最新的路径编号
	 */
	public int backtime(ArrayList<Date> time, ArrayList<Integer> temp) {
		int k = 0; // 用于标记最小时间的下标
		Date latest = time.get(0);
		for (int i = 1; i < time.size(); i++) {
			if (time.get(i).compareTo(latest) > 0) {
				latest = time.get(i);
				k = i;
			}
		}
		return temp.get(k);
	}

	/**
	 * 用于计算多条相同路径中最新的一条路经
	 * @param temp 相同路径集合
	 * @param pII	mytest路径信息
	 * @param lru lru路径信息
	 * @param lfu lfu路径信息
	 * @param judge 用于判断进行哪种测试，0表示mytest，1表示lru，2表示lfu
	 * @return 返回最新路径的编号
	 */
	public int latestpath(ArrayList<Integer> temp, pathinformation pII, LRUcache lru, LFUcache lfu, int judge) {
		ArrayList<Date> time = new ArrayList<Date>();
		for (int i = 0; i < temp.size(); i++) {
			if (judge == 0) {
				time.add(pII.getmap().get(temp.get(i)).Gettime());
			} else if (judge == 1) {
				time.add(lru.getmap().get(temp.get(i)).Gettime());
			} else if (judge == 2) {
				time.add(lfu.getmap().get(temp.get(i)).Gettime());
			}
		}
		return backtime(time, temp);
	}

	/**
	 * 进行缓存搜索
	 * @param start 搜索的起点
	 * @param end 搜索的终点
	 * @param nNI 点点表
	 * @param pII 路径信息表
	 * @param nPI 点边表
	 * @param time 查询时间
	 * @param lru	lru路径信息表
	 * @param lfu lfu路径信息表
	 * @param judge 用于判断进行哪种测试，0表示mytest，1表示lru，2表示lfu
	 * @param shortestpath 用于存放查询结果
	 * @param flag 用于判断是否可以进行缓存更新，一般查询会更新缓存，而pathjoin不进行缓存更新
	 * @return 返回查询结果，0表示没有查到相关点，1表示没有找到缓存，2表示找到缓存
	 */
	public int path(int start, int end, Subgraph nNI, pathinformation pII, invertedlist nPI, Date time, LRUcache lru,
			LFUcache lfu, int judge, ArrayList<Integer> shortestpath,boolean flag) {
		/**
		 * id用于获得所有的缓存中的节点
		 */
		ArrayList<Integer> id = new ArrayList<Integer>();
		for (Integer key : nNI.getmap().keySet()) {
			id.add(key);
		}
		if ((id.contains(start)) && (id.contains(end))) {
			/**
			 * coincide用于存放起点和终点的相同路径
			 */
			ArrayList<Integer> coincide = new ArrayList<Integer>();
			iscoincide(start, end, nPI, coincide);
			if (coincide.size() == 0) {
				//可以找到两个点，但是没有共同路径就是指无法命中缓存，则返回1，用于进行pathjoin
				return 1;
			} else if (coincide.size() == 1) {
				//只有一条共同路径，就直接进行搜索
				search(start, end, nNI, nPI, coincide.get(0), shortestpath);
				/**
				 * 如果flag=true，就是因为进行了查询，然后可以改变了相应的值
				 * mytest，不需要改变
				 * lru 改变了时间参数
				 * lfu 改变了命中次数
				 * 否则不进行改变，因为pathjoin不改变缓存值
				 */
				if (judge == 1 && flag) {
					lru.getmap().get(coincide.get(0)).Settime(time);
				}
				if (judge == 2 && flag) {
					lfu.getmap().get(coincide.get(0)).Setcount();
				}
				//成功搜索到结果，并将返回2
				return 2;
			} else {
				/**
				 * key表示多条路径中最新的一条路经
				 */
				int key = latestpath(coincide, pII, lru, lfu, judge);
				search(start, end, nNI, nPI, key, shortestpath);
				if (judge == 1 && flag) {
					lru.getmap().get(key).Settime(time);
				}
				if (judge == 2 && flag) {
					lfu.getmap().get(key).Setcount();
				}
				//成功搜索到结果，并将返回2
				return 2;
			}
		} else {
			//没有找到相关节点，返回0
			return 0;
		}
	}
}