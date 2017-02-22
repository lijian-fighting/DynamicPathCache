package function;

import cache.*;
import object.Query;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: LiJian
 * @Description:
 * @Date: Created in 15:48 2017/1/16
 * @Modified By:
 */
public class sigmod {
    private static double E = 1.0;

    /**
     * 根据cache和query求某个pathid的值
     *
     * @param key
     * @return
     */
    public double getcost(int key, Query qs, Query q,int []result,int size,List<Integer> cache) {
        int count = 0;
        if (cache.size() == 0) {
            for (int k =0;k<size;k++) {
                if (qs.getmap().get(key).contains(q.getmap().get(result[k]).get(0)) && qs.getmap().get(key).contains(q.getmap().get(result[k]).get(1))) {
                    count = count + 1;
                }
            }
            return count * E / (qs.getmap().get(key).size());
        } else {
            for (Integer temp : cache) {
                if (qs.getmap().get(temp).contains(q.getmap().get(key).get(0)) && qs.getmap().get(temp).contains(q.getmap().get(key).get(1))) {
                    return 0.0;
                }
            }
            for (int k =0;k<size;k++) {
                if (qs.getmap().get(key).contains(q.getmap().get(result[k]).get(0)) && qs.getmap().get(key).contains(q.getmap().get(result[k]).get(1))) {
                    count = count + 1;
                }
            }
            return count * E / (qs.getmap().get(key).size());
        }
    }


    /**
     * 判断两个double类型数据的大小
     *
     * @param a
     * @param b
     * @return
     */
    public boolean judge(double a, double b) {
        BigDecimal data1 = new BigDecimal(a);
        BigDecimal data2 = new BigDecimal(b);
        if (data1.compareTo(data2) <= 0) {
            return true;
        }
        return false;
    }

    public void in(int pid, int node,Subgraph NNI) {
        /**
         * 用于存放当前点点表中的所有点
         */
        if (NNI.getmap().keySet().contains(pid)) {
            //如果NNI中已经包含了key值，则直接在该记录后面增加，如果已经有了，就不添加
            if (!NNI.getmap().get(pid).contains(node)) {
                NNI.getmap().get(pid).add(node);
            }
        } else {
            //如果NNI没有包含key值，则新增这条记录
            ArrayList<Integer> temp = new ArrayList<Integer>();
            temp.add(node);
            NNI.mapput(pid, temp);
        }
    }

    public void insertNNI(ArrayList<String> node,Subgraph NNI) {
        if (node.size() > 1) {
            for (int i = 0; i < node.size(); i++) {
                if (i == 0) {
                    in(Integer.parseInt(node.get(i)), Integer.parseInt(node.get(i + 1)),NNI);
                } else if (i == node.size() - 1) {
                    in(Integer.parseInt(node.get(i)), Integer.parseInt(node.get(i - 1)),NNI);
                } else {
                    in(Integer.parseInt(node.get(i)), Integer.parseInt(node.get(i - 1)),NNI);
                    in(Integer.parseInt(node.get(i)), Integer.parseInt(node.get(i + 1)),NNI);
                }
            }
        }
    }

    public void insertNPI(ArrayList<String> node, int pathid,invertedlist NPI) {
        /**
         * 存放所有NPI的节点集合
         */
        for (int i = 0; i < node.size(); i++) {
            if (NPI.getmap().keySet().contains(Integer.parseInt(node.get(i)))) {
                NPI.getmap().get((Integer.parseInt(node.get(i)))).add(pathid);
            } else {
                ArrayList<Integer> temp = new ArrayList<Integer>();
                temp.add(pathid);
                NPI.mapput(Integer.parseInt(node.get(i)), temp);
            }
        }
    }

    public ArrayList<Integer> shortpath1(int key,Query qs) {
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

    public void Revised_Greedy2(Query qs, Query q, int []result, int size, int cachesize, Subgraph NNI,invertedlist NPI, pathinformation PII,
                                LRUcache lru, LFUcache lfu,int judge,boolean isweightupdate) {
        Queue<Key_value> H = new PriorityQueue<Key_value>(new Comparator<Key_value>() {
            @Override
            public int compare(Key_value o1, Key_value o2) {
                BigDecimal data1 = new BigDecimal(o1.getvalue());
                BigDecimal data2 = new BigDecimal(o2.getvalue());
                int flag = data1.compareTo(data2);
                if (flag < 0)
                    return 1;
                else if (flag == 0)
                    return 0;
                else
                    return -1;
            }
        });
        List<Integer> cache = new ArrayList<Integer>();
        cacheinsert insert = new cacheinsert();
        for (int key = 0;key<size;key++) {
            int s = Integer.parseInt(qs.getmap().get(result[key]).get(0));
            int e = Integer.parseInt(qs.getmap().get(result[key]).get(qs.getmap().get(result[key]).size() - 1));
            if (s != e) {
                double cost = getcost(result[key], qs, q,result,size,cache);
                Key_value temp = new Key_value(result[key], cost);
                H.add(temp);
            }
        }

        while ((NNI.getmap().size()<= cachesize) && (!H.isEmpty())) {
            Key_value temp = H.poll();
            int pathid = temp.getkey();
            double pathcost = getcost(pathid, qs, q,result,size,cache);
            if (judge(temp.getvalue(), pathcost)) {
                if ((cachesize - NNI.getmap().size() >= qs.getmap().get(pathid).size())) {
                    cache.add(pathid);
                    insertNNI(qs.getmap().get(pathid),NNI);
                    insertNPI(qs.getmap().get(pathid), pathid,NPI);
                    if(judge == 1){
                        if(isweightupdate) {
                            insert.insertPII(Integer.parseInt(q.getmap().get(pathid).get(0)),Integer.parseInt(q.getmap().get(pathid).get(1)),PII,Integer.parseInt(q.getmap().get(pathid).get(2)),pathid);
                        }else {
                            insert.insertPII1(Integer.parseInt(q.getmap().get(pathid).get(0)), Integer.parseInt(q.getmap().get(pathid).get(1)), PII, Integer.parseInt(q.getmap().get(pathid).get(2)), pathid, shortpath1(pathid, qs), NPI);
                        }
                    }else if(judge == 2){
                        insert.insertLRU(Integer.parseInt(q.getmap().get(pathid).get(0)),Integer.parseInt(q.getmap().get(pathid).get(1)),lru,pathid,Integer.parseInt(q.getmap().get(pathid).get(2)));
                    }else if(judge == 3){
                        insert.insertLFU(Integer.parseInt(q.getmap().get(pathid).get(0)),Integer.parseInt(q.getmap().get(pathid).get(1)),lfu,pathid,Integer.parseInt(q.getmap().get(pathid).get(2)));
                    }
                }
            } else {
                Key_value temp1 = new Key_value(pathid, pathcost);
                H.add(temp1);
            }
        }
    }

    public void iscoincide(int start, int end, ArrayList<Integer> coincide,invertedlist NPI ) {
        ArrayList<Integer> temp1 = new ArrayList<Integer>();
        ArrayList<Integer> temp2 = new ArrayList<Integer>();
        temp1.addAll(NPI.getmap().get(start));
        temp2.addAll(NPI.getmap().get(end));
        temp1.retainAll(temp2);
        coincide.addAll(temp1);
    }

    /**
     * 0表示缓存中没有发现或者没有结果，否则返回路径编号
     *
     * @param s
     * @param e
     * @param node
     * @return
     */
    private int lookup(int s, int e, ArrayList<Integer> node,Subgraph NNI,invertedlist NPI) {
        ArrayList<Integer> coincide = new ArrayList<Integer>();
        if (NNI.getmap().keySet().contains(s) && NNI.getmap().keySet().contains(e)) {
            iscoincide(s, e, coincide,NPI);
            if (coincide.size() == 0) {
                return 0;
            } else {
                cachesearch searchpath = new cachesearch();
                searchpath.search(s, e, NNI, NPI, coincide.get(coincide.size() - 1), node);
                return 1;
            }
        } else {

            return 0;
        }
    }


    public static ArrayList<Integer> getshortpath(int key, Query qs) {
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

    public void queryset(Query qs, Query q, int[] result, int[] rate, long[] time, Map<Integer, String> map, int SPCsize, int size, Subgraph NNI,invertedlist NPI) {
        Cal_accur cal = new Cal_accur();
        int count = 0;
        int flag = 0;
        for (int key = 0; key < result.length; key++) {
            ArrayList<Integer> shortpath = new ArrayList<Integer>();
            int s = Integer.parseInt(q.getmap().get(result[key]).get(0));
            int e = Integer.parseInt(q.getmap().get(result[key]).get(1));
            if (s != e) {
                long start;
                start = System.currentTimeMillis();
                flag = lookup(s, e, shortpath,NNI,NPI);
                time[3] += System.currentTimeMillis() - start;
                count = count + 1;
            }
            if (flag == 1) {
                rate[0] = rate[0] + 1;
                ArrayList<Integer> temp = new ArrayList<Integer>();
                temp.addAll(getshortpath(result[key], qs));
//                System.out.println("search:");
//                print(shortpath);
//                System.out.println("actual:");
//                print(temp);
                if (cal.isequal(shortpath, temp)) {
                    rate[1] = rate[1] + 1;
                }
                if (cal.isX_equal(shortpath, temp, 0.05, map)) {
                    rate[2] = rate[2] + 1;
                }
                if (cal.isX_equal(shortpath, temp, 0.1, map)) {
                    rate[3] = rate[3] + 1;
                }
            }
        }
    }
}
class Key_value{
    private int key;
    private double value;
    public int getkey(){
        return this.key;
    }
    public double getvalue(){
        return this.value;
    }
    Key_value(int key,double value){
        this.key = key;
        this.value = value;
    }
}
