package function;

import cache.LFUcache;
import cache.Subgraph;
import cache.invertedlist;
import cache.pathinformation;
import object.Query;

import javax.jnlp.IntegrationService;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: LiJian
 * @Description:
 * @Date: Created in 15:48 2017/1/16
 * @Modified By:
 */
public class sigmod {
    private static Query qs = new Query();// 之后路径
    private static Query q = new Query();// 之后路径
    private static Map<Integer,String> qmap = new HashMap<Integer,String>();
    private static int cachesize = 5000;
    private static double E = 1.0;
    private static Subgraph NNI = new Subgraph();
    private static invertedlist NPI = new invertedlist();
    private static List<Integer> cache = new ArrayList<Integer>();

    /**
     * 根据cache和query求某个pathid的值
     * @param key
     * @return
     */
    public double getcost(int key){
        int count = 0;
        if(cache.size() == 0){
            for(Integer pathid:q.getmap().keySet()){
                if(qs.getmap().get(key).contains(q.getmap().get(pathid).get(0))&&qs.getmap().get(key).contains(q.getmap().get(pathid).get(1))){
                    count = count + 1;
                }
            }
            return count*1.0/(qs.getmap().get(key).size());
        }else{
            for(Integer temp:cache){
                if(qs.getmap().get(temp).contains(q.getmap().get(key).get(0))&&qs.getmap().get(temp).contains(q.getmap().get(key).get(1))){
                    return 0.0;
                }
            }
            for(Integer pathid:q.getmap().keySet()){
                if(qs.getmap().get(key).contains(q.getmap().get(pathid).get(0))&&qs.getmap().get(key).contains(q.getmap().get(pathid).get(1))){
                    count = count + 1;
                }
            }
            return count*1.0/(qs.getmap().get(key).size());
        }
    }

    /**
     * 返回缓存中已有的节点数
     * @return
     */
    public int allnode(){
        return NNI.getmap().size();
    }

    /**
     * 判断两个double类型数据的大小
     * @param a
     * @param b
     * @return
     */
    public boolean judge(double a,double b){
        BigDecimal data1 = new BigDecimal(a);
        BigDecimal data2 = new BigDecimal(b);
        if(data1.compareTo(data2)<=0){
            return true;
        }
        return false;
    }

    public void in(int pid, int node) {
        /**
         * 用于存放当前点点表中的所有点
         */
        if (NNI.getmap().keySet().contains(pid)) {
            //如果NNI中已经包含了key值，则直接在该记录后面增加，如果已经有了，就不添加
            if (!NNI.getmap().get(pid).contains(node)) {
                NNI.addEdgeId(pid, node);
            }
        } else {
            //如果NNI没有包含key值，则新增这条记录
            ArrayList<Integer> temp = new ArrayList<Integer>();
            temp.add(node);
            NNI.mapput(pid, temp);
        }
    }

    public void insertNNI(ArrayList<String> node) {
        if(node.size() >1){
            for (int i = 0; i < node.size(); i++) {
                if (i == 0) {
                    in(Integer.parseInt(node.get(i)), Integer.parseInt(node.get(i + 1)));
                } else if (i == node.size() - 1) {
                    in(Integer.parseInt(node.get(i)), Integer.parseInt(node.get(i - 1)));
                } else {
                    in(Integer.parseInt(node.get(i)), Integer.parseInt(node.get(i + 1)));
                    in(Integer.parseInt(node.get(i)), Integer.parseInt(node.get(i - 1)));
                }
            }
        }
    }

    public void insertNPI(ArrayList<String> node, int pathid) {
        /**
         * 存放所有NPI的节点集合
         */
        for (int i = 0; i < node.size(); i++) {
            if (NPI.getmap().keySet().contains(Integer.parseInt(node.get(i)))) {
                NPI.addEdgeId(Integer.parseInt(node.get(i)), pathid);
            } else {
                ArrayList<Integer> temp = new ArrayList<Integer>();
                temp.add(pathid);
                NPI.mapput(Integer.parseInt(node.get(i)), temp);
            }
        }
    }

    public void Revised_Greedy(){
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
        for(Integer key:qs.getmap().keySet()){
            int s = Integer.parseInt(qs.getmap().get(key).get(0));
            int e = Integer.parseInt(qs.getmap().get(key).get(qs.getmap().get(key).size()-1));
            if(s!=e) {
                double cost = getcost(key);
                Key_value temp = new Key_value(key,cost);
                H.add(temp);
            }
        }
        while((allnode()<= cachesize) && (!H.isEmpty())){
            Key_value temp = H.poll();
            int pathid = temp.getkey();
            double pathcost = getcost(pathid);
            if(judge(temp.getvalue(),pathcost)){
                if((cachesize - allnode() >= qs.getmap().get(pathid).size())){
                    cache.add(pathid);
                    insertNNI(qs.getmap().get(pathid));
                    insertNPI(qs.getmap().get(pathid),pathid);
                }
            }else{
                Key_value temp1 = new Key_value(pathid,pathcost);
                H.add(temp1);
            }
        }
    }

    public void iscoincide(int start, int end, ArrayList<Integer> coincide) {
        ArrayList<Integer> temp1 = new ArrayList<Integer>();
        ArrayList<Integer> temp2 = new ArrayList<Integer>();
        temp1.addAll(NPI.getmap().get(start));
        temp2.addAll(NPI.getmap().get(end));
        temp1.retainAll(temp2);
        coincide.addAll(temp1);
    }
    /**
     * 0表示缓存中没有发现或者没有结果，否则返回路径编号
     * @param s
     * @param e
     * @param node
     * @return
     */
    private int lookup(int s,int e,ArrayList<Integer> node){
        ArrayList<Integer> coincide = new ArrayList<Integer>();
        if(NNI.getmap().keySet().contains(s)&&NNI.getmap().keySet().contains(e)){
            iscoincide(s,e,coincide);
            if(coincide.size() == 0){
                return 0;
            }else{
                cachesearch searchpath = new cachesearch();
                searchpath.search(s,e,NNI,NPI,coincide.get(coincide.size()-1),node);
                return 1;
            }

        }else{
            return 0;
        }
    }
    private boolean isequal(ArrayList<Integer> path,ArrayList<Integer> truepath){
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
    public static ArrayList<Integer> getshortpath(int key) {
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
    public void queryset(){
        long time = 0;
        int []rate = {0,0};
        int count = 0;
        int flag = 0;
        for (int key : q.getmap().keySet()) {
            ArrayList<Integer> shortpath = new ArrayList<Integer>();
            int s = Integer.parseInt(q.getmap().get(key).get(0));
            int e = Integer.parseInt(q.getmap().get(key).get(1));
            if (s != e) {
                long start;
                start = System.currentTimeMillis();
                flag = lookup(s,e,shortpath);
                time += System.currentTimeMillis() -start;
                count = count + 1;
            }
            if(flag == 1){
                rate[0] = rate[0] + 1;
                ArrayList<Integer> temp = new ArrayList<Integer>();
                temp.addAll(getshortpath(key));
//                System.out.println("search:");
//                print(shortpath);
//                System.out.println("actual:");
//                print(temp);
                if(isequal(shortpath,temp)){
                    rate[1] = rate[1] + 1;
                }
            }
        }
//        System.out.println(rate[1]);
        System.out.println("static test:" + rate[0]+" and hit:"+ (rate[0] * 1.0 / count)+" and time:"+(time*1.0/count)+"ms and accur:"+(rate[1]*1.0/rate[0]));
    }
    public void print(ArrayList<Integer> node){
        for(int i = 0;i<node.size();i++){
            System.out.print(node.get(i));
            if(i!=node.size()-1){
                System.out.print("->");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("inia data...");
        loadpoint l = new loadpoint();
        l.load(qs, q, qmap);
        System.out.println("inia data successfully!");
        sigmod s = new sigmod();
        s.Revised_Greedy();
        s.queryset();
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
