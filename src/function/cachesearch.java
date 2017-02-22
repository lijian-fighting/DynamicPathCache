package function;

import java.util.*;

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

	public boolean ispath(int pathid, int pid, invertedlist nPI) {
//		System.out.println(nPI.getmap().size());
//		if(nPI.getmap().keySet().contains(pid)){
//			System.out.println("yes");
//		}else{
//			System.out.println("no");
//		}
		if (nPI.getmap().keySet().contains(pid)&&nPI.getmap().get(pid).contains(pathid)) {
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
//		Node flag = new Node(0);
//		Node s = new Node(start,flag);
//		Node p = flag;
//		Queue<Node> choice = new LinkedList<Node>();
//		choice.offer(s);
//		while(!choice.isEmpty()){
//			Node temp = choice.peek();
//			if(temp.key == end){
//				p = temp;
//				break;
//			}
//			for (int i = 0; i < nNI.getmap().get(temp.key).size(); i++) {
//				if (nPI.getmap().get(nNI.getmap().get(temp.key).get(i)).contains(pathid)&&nNI.getmap().get(temp.key).get(i)!= temp.pre.key){
//					Node in = new Node(nNI.getmap().get(temp.key).get(i),temp);
//					choice.offer(in);
//				}
//			}
//			choice.poll();
//		}
//		while(p.key != 0){
//			shortpath.add(p.key);
//			p = p.pre;
//		}
//		Collections.reverse(shortpath);
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
				if (ispath(pathid, nNI.getmap().get(start).get(i), nPI)
						&& !shortpath.contains(nNI.getmap().get(start).get(i))) {
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
						if (shortpath.get(j) .equals( choice.get(i))) {
							shortpath.remove(j);
						}
					}
				}
			}
		}
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
		int max = 0;
		int pathid = 0;
		if(judge == 0) {
			for (Integer key : temp) {
				if(pII.getmap().get(key).Gettime()>max){
					max = pII.getmap().get(key).Gettime();
					pathid = pII.getmap().get(key).Getpathid();
				}
			}
		}else if(judge == 1){
			for(Integer key:temp){
				if(lru.getmap().get(key).Gettime()>max){
					max = lru.getmap().get(key).Gettime();
					pathid = lru.getmap().get(key).Getpathid();
				}
			}
		}else if(judge == 2){
			for(Integer key:temp){
				if(lfu.getmap().get(key).Gettime()>max){
					max = lfu.getmap().get(key).Gettime();
					pathid = lfu.getmap().get(key).Getpathid();
				}
			}
		}
		return pathid;
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
	public int path(int start, int end, Subgraph nNI, pathinformation pII, invertedlist nPI, int time, LRUcache lru,
					LFUcache lfu, int judge, ArrayList<Integer> shortestpath,boolean flag) {
		/**
		 * id用于获得所有的缓存中的节点
		 */
		if ((nNI.getmap().keySet().contains(start))
				&& (nNI.getmap().keySet().contains(end))
				&&(nPI.getmap().keySet().contains(start))
				&& (nPI.getmap().keySet().contains(end))) {
			/**
			 * coincide用于存放起点和终点的相同路径
			 */
			ArrayList<Integer> coincide = new ArrayList<Integer>();
			iscoincide(start, end, nPI, coincide);
			if (coincide.size() == 0) {
				//可以找到两个点，但是没有共同路径就是指无法命中缓存，则返回1，用于进行pathjoin
				return 1;
			} else if (coincide.size() == 1) {
//				System.out.println("1");
//				System.out.println(coincide.get(0));
//				if(nPI.getmap().get(start).contains(coincide.get(0))&&nPI.getmap().get(end).contains(coincide.get(0))){
//					System.out.println("yes");
//				}
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