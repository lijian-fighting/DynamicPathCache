package function;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

import cache.LFUcache;
import cache.LRUcache;
import cache.Subgraph;
import cache.invertedlist;
import cache.pathinformation;
import object.Query;
import object.Result;

public class Test2 {
	public static Query qs = new Query();// 之后路径
	public static Query q = new Query();// 之后路径
	public static Map<Integer,String> qmap = new HashMap<Integer,String>();
	public static int SPCsize = 4496;
	public static List<Result> res = new ArrayList<Result>();

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
	static Subgraph NNI4 = new Subgraph();
	static invertedlist NPI4 =new invertedlist();

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
	public static boolean isSetEqual(Set set1, Set set2){

		if(set1.size() != set2.size()){
			return false;
		}

		Iterator ite1 = set1.iterator();
		while(ite1.hasNext()){
			if(!set2.contains(ite1.next())){
				return false;
			}
		}
		return true;
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
	public static void cache(int s, int e, int dt, Subgraph nNI, invertedlist nPI, int []rate, int judge, int key,double N,long []time,double A,
							 int count,int timeMAX,int cachesize,boolean isweightupdate, boolean ispathjoin) {
		ArrayList<Integer> shortpath = new ArrayList<Integer>();
		Cal_accur cal = new Cal_accur();
		/**
		 * 用于判断是否可以在缓存中命中
		 * 0表示缓存中没有这两点，1表示缓存中无法命中，2表示缓存可以命中
		 */
//		System.out.println(s+","+e);
		int flag;
		/**
		 * 用于判断是否可以预测
		 * 0表示无法预测，1表示可以预测
		 */
		int flag2;
		long start,mid;
//		System.out.println(s+","+e);
//		if(isSetEqual(nNI.getmap().keySet(),nPI.getmap().keySet())){
//			System.out.println("NNI == NPI");
//		}else{
//			System.out.println("NNI!=NPI");
//		}
		//number ++;
		if (nNI.getmap().size() != 0) {
			/**
			 * 进行缓存搜索，输入起点终点，以及缓存结构
			 */
			if(count >= 0) {
				start = System.currentTimeMillis();
				flag = search.path(s, e, nNI, PII, nPI, dt, LRU, LFU, judge, shortpath, true);
				time[judge] += System.currentTimeMillis() - start;
//			    mid = System.currentTimeMillis();
			}else{
				flag = search.path(s, e, nNI, PII, nPI, dt, LRU, LFU, judge, shortpath, true);
			}
			if(flag == 1){
				/**
				 * 进行pathjoin
				 */
				if(judge == 0) {
					if(ispathjoin) {
                    	mid =System.currentTimeMillis();
						flag2 = pathjoin.join(s, e, nNI, PII, nPI, dt, LRU, LFU, judge, shortpath, qmap, N);
						time[judge] += System.currentTimeMillis() - mid; //加pathjoin
					}else {
						flag2 = 0; // 不加pathjoin
					}
				}else{
					flag2 = 0;
				}
				if(flag2 == 0){
					//无法pathjoin，直接搜索API，同时更新缓存
					shortpath.addAll(shortpath(key));
					insertmap.insert(s, e, shortpath, nNI, PII, nPI, dt, LRU, LFU, judge,A,timeMAX,cachesize,isweightupdate);
				}else{
					//成功pathjoin
					if(count >= 0) {
						rate[0] = rate[0] + 1;
						ArrayList<Integer> temp = new ArrayList<Integer>();
						temp.addAll(shortpath(key));
						if (cal.isequal(shortpath, temp)) {
							rate[4] = rate[4] + 1;
						}
						if (cal.isX_equal(shortpath, temp, 0.05, qmap)) {
							rate[5] = rate[5] + 1;
						}
						if (cal.isX_equal(shortpath, temp, 0.1, qmap)) {
							rate[6] = rate[6] + 1;
						}
					}
				}
			} else if(flag == 0) { // 如果没有在缓存中找到这两个点，就直接进行计算，不需要预测
				shortpath.addAll(shortpath(key));
				insertmap.insert(s, e, shortpath, nNI, PII, nPI, dt, LRU, LFU, judge,A,timeMAX,cachesize,isweightupdate);
			} else {
//				for(int i = 0;i<shortpath.size();i++) {
//					System.out.print(shortpath.get(i));
//					if(i!=shortpath.size()-1) {
//						System.out.print("-->");
//					}
//				}
				if(count >= 0) {
					rate[0] = rate[0] + 1;
					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.addAll(shortpath(key));
					if (cal.isequal(shortpath, temp)) {
						rate[1] = rate[1] + 1;
					}
					if (cal.isX_equal(shortpath, temp, 0.05, qmap)) {
						rate[2] = rate[2] + 1;
					}
					if (cal.isX_equal(shortpath, temp, 0.1, qmap)) {
						rate[3] = rate[3] + 1;
					}
				}
			}
		} else {
			shortpath.addAll(shortpath(key));
			insertmap.insert(s, e, shortpath, nNI, PII, nPI, dt, LRU, LFU, judge,A,timeMAX,cachesize,isweightupdate);
		}
	}


	public static void inia(double N,int []reult,double A,int size,sigmod spc,int timeMAX,int cachesize,boolean iswupdate,boolean ispathjoin) {
		Cal_accur cal = new Cal_accur();
		/**
		 * rate：0表示成功命中的个数，1表示准确命中的个数，2表示5%,3表示10%,4表示准确pathjoin,5表示5%,6表示10%
		 */
		int[] rate1 = new int[]{0, 0, 0, 0, 0, 0, 0};
		int[] rate2 = new int[]{0, 0, 0, 0, 0, 0, 0};
		int[] rate3 = new int[]{0, 0, 0, 0, 0, 0, 0};
		int[] rate4 = new int[]{0, 0, 0, 0, 0, 0, 0};
		/**
		 * 用于记录每个测试运行的时间
		 */
		long []time = new long[]{0,0,0,0};
		for (int  key = 0; key < reult.length;key++) {
			int s = Integer.parseInt(q.getmap().get(reult[key]).get(0));
			int e = Integer.parseInt(q.getmap().get(reult[key]).get(1));
			int dt = Integer.parseInt(q.getmap().get(reult[key]).get(2));
//			loadpoint l = new loadpoint();
//			l.writepoint("start = "+q.getmap().get(reult[key]).get(0)," end =" +q.getmap().get(reult[key]).get(1));
//			System.out.println("time = " + dt);
			if (s != e) {
//				System.out.println(reult[key]);
				cache(s, e, dt, NNI1, NPI1, rate1, 0, reult[key], N, time, A,key,timeMAX,cachesize,iswupdate,ispathjoin);
				cache(s, e, dt, NNI2, NPI2, rate2, 1, reult[key], N, time, A,key,timeMAX,cachesize,iswupdate,ispathjoin);
				cache(s, e, dt, NNI3, NPI3, rate3, 2, reult[key], N, time, A,key,timeMAX,cachesize,iswupdate,ispathjoin);
				if(key == size){
					break;
				}
			}
		}
		spc.queryset(qs,q,reult,rate4,time,qmap,SPCsize,size,NNI4,NPI4);

		Result temp = new Result(iswupdate,ispathjoin,size,cachesize,(timeMAX/60),A,N,
				cal.div(rate1[0]*1.0,size*1.0),cal.div(rate2[0]*1.0,size*1.0),cal.div(rate3[0]*1.0,size*1.0),cal.div(rate4[0]*1.0,size*1.0),
				cal.div(time[0]*1.0,size*1.0),cal.div(time[1]*1.0,size*1.0),cal.div(time[2]*1.0,size*1.0),cal.div(time[3]*1.0,size*1.0),
				cal.div((rate1[1]+rate1[4])*1.0,rate1[0]*1.0),cal.div(rate2[1]*1.0,rate2[0]*1.0),cal.div(rate3[1]*1.0,rate3[0]*1.0),cal.div(rate4[1]*1.0,rate4[0]*1.0),
				cal.div((rate1[2]+rate1[5])*1.0,rate1[0]*1.0),cal.div(rate2[2]*1.0,rate2[0]*1.0),cal.div(rate3[2]*1.0,rate3[0]*1.0),cal.div(rate4[2]*1.0,rate4[0]*1.0),
				cal.div((rate1[3]+rate1[6])*1.0,rate1[0]*1.0),cal.div(rate2[3]*1.0,rate2[0]*1.0),cal.div(rate3[3]*1.0,rate3[0]*1.0),cal.div(rate4[3]*1.0,rate4[0]*1.0),insertmap.getmax_value());
		res.add(temp);
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
		NNI4.clear();
		NPI4.clear();
	}

	public static void main(String[] args) throws IOException {
		ResultToExcel re = new ResultToExcel();
		System.out.println("inia data...");
		loadpoint l = new loadpoint();
		l.load(qs, q, qmap);
		System.out.println(qmap.size());
		int []all = new int[qs.getmap().size()];
		l.loadseed(all);
		System.out.println(all.length);
		System.out.println("inia data successfully!");

		int []gener;
		gener = Arrays.copyOfRange(all,0,SPCsize);
		System.out.println(gener.length);
		Arrays.sort(gener);

		boolean isweightupdate = true;
		boolean ispathjoin = false;
		int []size = {7000};
//		int []size = {1000,2000,3000,4000,5000};
//		int []cachesize = {1000,2000,3000,4000,5000,6000,7000,8000,9000,10000};
//		int []cachesize = {1000,2000,3000,4000,5000};
		int []cachesize = {1000};

		int []time = {30*60};
//		int []time = {15*60,30*60,45*60,60*60};
		double A[] = {0.5};
//		double A[] = {0.0,0.2,0.4,0.6,0.8,1.0};
		double N[] = {-1.0}; //-1.0表示没有pathjoin
//		double N[] = {1.5};
//		double N[] = {1.2,1.4,1.6,1.8,2.0};
//		double N[] = {1,1.01,1.02,1.03,1.04,1.05,1.06,1.07,1.08,1.09,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,2.0};


		for(int i = 0;i<N.length;i++){
			for(int j = 0;j<A.length;j++) {
				for(int m = 0;m<size.length;m++) {
					for(int n = 0;n<time.length;n++) {
						for(int p = 0;p<cachesize.length;p++) {
							System.out.println(cachesize[p]);
							sigmod s = new sigmod();
//                          s.Revised_Greedy(qs,q,gener,SPCsize,cachesize[p],NNI4,NPI4);
							s.Revised_Greedy2(qs,q,gener,SPCsize,cachesize[p],NNI4,NPI4,null,null,null,0,isweightupdate);
							s.Revised_Greedy2(qs,q,gener,SPCsize,cachesize[p],NNI1,NPI1,PII,null,null,1,isweightupdate);
							s.Revised_Greedy2(qs,q,gener,SPCsize,cachesize[p],NNI2,NPI2,null,LRU,null,2,isweightupdate);
							s.Revised_Greedy2(qs,q,gener,SPCsize,cachesize[p],NNI3,NPI3,null,null,LFU,3,isweightupdate);
							if(NNI1.getmap().size() == NPI1.getmap().size()
									&&NNI2.getmap().size() == NPI2.getmap().size()
									&&NNI3.getmap().size() == NPI3.getmap().size()
									&&NNI4.getmap().size() == NPI4.getmap().size()){
								System.out.println("lijian:NNI == NPI");
								System.out.println(NNI1.getmap().size());
							}
							int[] test;
							test = Arrays.copyOfRange(all, SPCsize, SPCsize+size[m]);
//                            System.out.println(test.length);
                            Arrays.sort(test);
//                            int[] result = new int[gener.length + test.length];
//                            System.arraycopy(gener, 0, result, 0, gener.length);
//                            System.arraycopy(test, 0, result, gener.length, test.length);
//                            System.out.println(result.length);
							inia(N[i], test, A[j], size[m], s, time[n],cachesize[p],isweightupdate,ispathjoin); //初始化随机数列
							clear();
						}
					}
				}
			}
		}
//		re.print("/root/lijian/6.xls",res);
		re.print("F:/dataset/study/2.xls",res);
	}
}