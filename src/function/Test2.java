package function;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import cache.LFUcache;
import cache.LRUcache;
import cache.Subgraph;
import cache.invertedlist;
import cache.pathinformation;
import com.sun.org.apache.xpath.internal.SourceTree;
import object.Query;

public class Test2 {
	public static Query qs = new Query();// 之后路径
	public static Query q = new Query();// 之后路径
	public static Map<Integer,String> qmap = new HashMap<Integer,String>();
	public static int size;
	public static int number  = 0;

	static Subgraph NNI1 = new Subgraph();
	static invertedlist NPI1 = new invertedlist();
	static pathinformation PII = new pathinformation();
	// 用于进行本次算法的更新

	static Subgraph NNI2 = new Subgraph();
	static invertedlist NPI2 = new invertedlist();
	static LRUcache LRU = new LRUcache();
	// 用于进行LRU算法的更新

	static Subgraph NNI3 = new Subgraph();
	static invertedlist NPI3 = new invertedlist();
	static LFUcache LFU = new LFUcache();
	// 用于进行LFU算法的更新

	static cachesearch search = new cachesearch();
	static cacheinsert insertmap = new cacheinsert();
	static Pathjoin pathjoin = new Pathjoin();

	/**
	 * 根据key值输出查询路径的大小
	 * @param key
	 * @return
	 */
	public static ArrayList<Integer> shortpath(int key) {
		ArrayList<Integer> shortpath = new ArrayList<Integer>();
		if (qs.getmap().get(key).size() == 1) {
			shortpath.add(Integer.parseInt(qs.getmap().get(key).get(0)));
			shortpath.add(Integer.parseInt(qs.getmap().get(key).get(qs.getmap().get(key).size() - 1)));
		} else {
			for (int i = 0; i < qs.getmap().get(key).size(); i++) {
				shortpath.add(Integer.parseInt(qs.getmap().get(key).get(i)));
			}
		}
		return shortpath;
	}

//	public static int actual(int s,int e,int key){
//		return qs.getmap().get(key).indexOf(e) -  qs.getmap().get(key).indexOf(s) + 1;
//	}

	/**
	 * 将输出结果写到文本中
	 * @param a 需要写入文本的字符串
	 */
	public static void write(String a){
	FileWriter writer = null;
	try { //
		// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件,false表示覆盖的方式写入
    	writer = new FileWriter("/home/zhujie/lijian/555.txt", true);
		String content = a + " " +"\r\n";
		writer.write(content);
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			if (writer != null) {
				writer.close();
			}
			} catch (IOException e) {
			e.printStackTrace();
			}
		}
	}

	public static boolean isequal(ArrayList<Integer> path,ArrayList<Integer> truepath){
		if(path.size() != truepath.size()){
			return false;
		}else{
			for(int i = 0;i<path.size();i++){
				if(!path.get(i).equals(truepath.get(i))){
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * 根据输入进行相应的缓存搜索和预测
	 * @param s 查询起点
	 * @param e 查询终点
	 * @param dt 查询时间
	 * @param nNI 点边表
	 * @param nPI 点点表
	 * @param rate 命中率
	 * @param judge 判断进行哪个测试，0表示我的测试，1表示LRU，2表示LFU
	 * @param key 判断查询问题编号
	 * @param N 参数，用于pathjoin
	 */
	public static void cache(int s, int e, Date dt, Subgraph nNI, invertedlist nPI, int []rate, int judge, int key,double N,long []time,double A) {
		ArrayList<Integer> shortpath = new ArrayList<Integer>();
		/**
		 * 用于判断是否可以在缓存中命中
		 * 0表示缓存中没有这两点，1表示缓存中无法命中，2表示缓存可以命中
		 */
		int flag;
		/**
		 * 用于判断是否可以预测
		 * 0表示无法预测，1表示可以预测
		 */
		int flag2;
		long start,mid;
		//System.out.println(s+","+e);
		//number ++;
		if (nNI.getmap().size() != 0) {
			/**
			 * 进行缓存搜索，输入起点终点，以及缓存结构
			 */
			start = System.currentTimeMillis();
			flag = search.path(s, e, nNI, PII, nPI, dt, LRU, LFU, judge, shortpath,true);
			time[judge] += System.currentTimeMillis() - start;
//			mid = System.currentTimeMillis();
			if(flag == 1){
				/**
				 * 进行pathjoin
				 */
				if(judge == 0) {
//					flag2 = pathjoin.join(s, e, nNI, PII, nPI, dt, LRU, LFU, judge, shortpath, qmap, N);
//					time[judge] += System.currentTimeMillis() - mid; //加pathjoin
					flag2 = 0; // 不加pathjoin
				}else{
					flag2 = 0;
				}
				if(flag2 == 0){
					//无法pathjoin，直接搜索API，同时更新缓存
					shortpath.addAll(shortpath(key));
					insertmap.insert(s, e, shortpath, nNI, PII, nPI, dt, LRU, LFU, judge,A);
				}else{
					//成功pathjoin
					rate[2] = rate[2] + 1;
					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.addAll(shortpath(key));
					if(isequal(shortpath,temp)){
						rate[3] = rate[3] + 1;
					}
				}
			} else if(flag == 0) { // 如果没有在缓存中找到这两个点，就直接进行计算，不需要预测
				shortpath.addAll(shortpath(key));
				insertmap.insert(s, e, shortpath, nNI, PII, nPI, dt, LRU, LFU, judge,A);
			} else {
				rate[0] = rate[0] + 1;
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.addAll(shortpath(key));
				if(isequal(shortpath,temp)){
					rate[1] = rate[1] + 1;
				}
			}
		} else {
			shortpath.addAll(shortpath(key));
			insertmap.insert(s, e, shortpath, nNI, PII, nPI, dt, LRU, LFU, judge,A);
		}
	}

	public static int[] randomArray(int min,int max,int n){
		int len = max-min+1;
		if(max < min || n > len){
			return null;
		}
		//初始化给定范围的待选数组
		int[] source = new int[len];
		for (int i = min; i < min+len; i++){
			source[i-min] = i;
		}
		int[] result = new int[n];
		Random rd = new Random();
		int index = 0;
		for (int i = 0; i < result.length; i++) {
			//待选数组0到(len-2)随机一个下标
			index = Math.abs(rd.nextInt() % len--);
			//将随机到的数放入结果集
			result[i] = source[index];
			//将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
			source[index] = source[len];
		}
		return result;
	}

	public static void inia(double N,boolean flag,int []reult,double A) {
		/**
		 * rate：0表示成功命中的个数，1表示准确命中的个数，2表示pathjoin个数，3表示pathjoin，并且准确的个数
		 */
		int[] rate1 = new int[]{0, 0, 0, 0};
		int[] rate2 = new int[]{0, 0, 0, 0};
		int[] rate3 = new int[]{0, 0, 0, 0};
		/**
		 * 用于记录每个测试运行的时间
		 */
		write("random and  weight and no pathjoin");
		int count = 0;
		long []time = new long[]{0,0,0};
		for (int  key : reult) {
			int s = Integer.parseInt(q.getmap().get(key).get(0));
			int e = Integer.parseInt(q.getmap().get(key).get(1));
			if (s != e) {
				Date dt = new Date();
				cache(s, e, dt, NNI1, NPI1, rate1, 0, key, N,time,A);
				count = count + 1;
			}
		}
		write("N = " + N + "A = " + A + " count =" + count +" cachesize = 5000");
		if(flag) {
			for (int key : reult) {
				int s = Integer.parseInt(q.getmap().get(key).get(0));
				int e = Integer.parseInt(q.getmap().get(key).get(1));
				if (s != e) {
					Date dt = new Date();
					cache(s, e, dt, NNI2, NPI2, rate2, 1, key, N,time, A);
				}
			}
			for (int key : reult) {
				int s = Integer.parseInt(q.getmap().get(key).get(0));
				int e = Integer.parseInt(q.getmap().get(key).get(1));
				if (s != e) {
					Date dt = new Date();
					cache(s, e, dt, NNI3, NPI3, rate3, 2, key, N,time,A);
				}
			}
		}
		write("my test:" + rate1[0]+" and hit:" + (rate1[0]*1.0/count) + " and searchtime:" + (time[0]*1.0/count) +"ms and hit_accur:"+(rate1[1]*1.0/rate1[0])+ " and predict:"+rate1[2]+" and rate:" + (rate1[2]*1.0/count)+" and pre_accur:"+(rate1[3]*1.0/rate1[2]));
		write("max = "+ insertmap.getmax_value());
		if(flag) {
			write("LRU test:" + rate2[0] + " and hit:" + (rate2[0] * 1.0 / count) + " and search time:" + (time[1]*1.0/count) + "ms and accurate:" + (rate2[1] * 1.0 / rate2[0]));
			write("LFU test:" + rate3[0] + " and hit:" + (rate3[0] * 1.0 / count) + " and search time:" + (time[2]*1.0/count) + "ms and accurate:" + (rate3[1] * 1.0 / rate3[0]));
		}
	}

	public static void inia(double N,boolean flag,double A) {
		/**
		 * rate：0表示成功命中的个数，1表示准确命中的个数，2表示pathjoin个数，3表示pathjoin，并且准确的个数
		 */
		write("no random and weight update and  no pathjoin and change a");
		int[] rate1 = new int[]{0, 0, 0, 0};
		int[] rate2 = new int[]{0, 0, 0, 0};
		int[] rate3 = new int[]{0, 0, 0, 0};
		/**
		 * 用于记录每个测试运行的时间
		 */
		int count = 0;
		long []time = new long[3];
		for (int  key : q.getmap().keySet()) {
			int s = Integer.parseInt(q.getmap().get(key).get(0));
			int e = Integer.parseInt(q.getmap().get(key).get(1));
			if (s != e) {
				Date dt = new Date();
				cache(s, e, dt, NNI1, NPI1, rate1, 0, key, N,time,A);
				count = count + 1;
			}
		}
		if(flag) {
			for (int key : q.getmap().keySet()) {
				int s = Integer.parseInt(q.getmap().get(key).get(0));
				int e = Integer.parseInt(q.getmap().get(key).get(1));
				if (s != e) {
					Date dt = new Date();
					cache(s, e, dt, NNI2, NPI2, rate2, 1, key, N,time,A);
				}
			}
			for (int key : q.getmap().keySet()) {
				int s = Integer.parseInt(q.getmap().get(key).get(0));
				int e = Integer.parseInt(q.getmap().get(key).get(1));
				if (s != e) {
					Date dt = new Date();
					cache(s, e, dt, NNI3, NPI3, rate3, 2, key, N,time,A);
				}
			}
		}
		write("N = " + N + "A = " + A + " count =" + count +" cachesize = 5000");
		write("my test:" + rate1[0]+" and hit:" + (rate1[0]*1.0/count) + " and searchtime:" + (time[0]*1.0/count) +"ms and hit_accur:"+(rate1[1]*1.0/rate1[0])+ " and predict:"+rate1[2]+" and rate:" + (rate1[2]*1.0/count)+" and pre_accur:"+(rate1[3]*1.0/rate1[2]));
		write("max = "+ insertmap.getmax_value());
		if(flag) {
			write("LRU test:" + rate2[0] + " and hit:" + (rate2[0] * 1.0 / count) + " and search time:" + (time[1]*1.0/count)  + "ms and accurate:" + (rate2[1] * 1.0 / rate2[0]));
			write("LFU test:" + rate3[0] + " and hit:" + (rate3[0] * 1.0 / count) + " and search time:" + (time[2]*1.0/count)  + "ms and accurate:" + (rate3[1] * 1.0 / rate3[0]));
		}
	}

	public static void clear(){
		NNI1.clear();
		NPI1.clear();
		PII.clear();
		NNI2.clear();
		NPI2.clear();
		LFU.clear();
		NNI3.clear();
		NPI3.clear();
		LRU.clear();
	}

	public static void main(String[] args) throws IOException {
		System.out.println("inia data...");
		loadpoint l = new loadpoint();
		l.load(qs, q, qmap);
		System.out.println("inia data successfully!");
		int[] reult = randomArray(1,qs.getmap().size(),qs.getmap().size()); // 生成一组随机序列
		boolean flag = true;
//		double N[] = {1.0,1.5,2.0,2.5,3.0}; // 带pathjoin
		double A[] = {0.0,0.2,0.4,0.6,0.8,1.0};
		double N[] = {1.0}; //不带pathjoin
		for(int i = 0;i<N.length;i++){
			for(int j = 0;j<A.length;j++) {
//				inia(N[i],flag,A[j]);
//				int[] reult = randomArray(1, qs.getmap().size(), qs.getmap().size()); // 生成一组随机序列
				inia(N[i], flag, reult,A[j]); //初始化随机数列
				clear();
				flag = false;
			}
		}
	}
}
