package function;

import cache.Subgraph;
import cache.invertedlist;
import cache.pathinformation;
import object.Query;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LiJian
 * @Description:
 * @Date: Created in 17:57 2017/2/17
 * @Modified By:
 */
public class com {
    private static Subgraph NNI1 = new Subgraph();
    private static invertedlist NPI1 = new invertedlist();
    private static pathinformation PII = new pathinformation();
    public static Query qs = new Query();// 之后路径
    public static Query q = new Query();// 之后路径
    public static Map<Integer,String> qmap = new HashMap<Integer,String>();
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
    public static void main(String[] args) {
//        loadpoint l = new loadpoint();
//        l.load(qs, q, qmap);
//        seed t =new seed();
//        for(Integer key:qs.getmap().keySet()){
//            String temp = "";
//            for(int i = 0;i<qs.getmap().get(key).size();i++) {
//                temp += qs.getmap().get(key).get(i)+ " ";
//            }
//            t.write(temp);
//        }
//        cachesearch search = new cachesearch();
//        cacheinsert insertmap = new cacheinsert();
//        int flag = 0;
//        for (Integer key : q.getmap().keySet()) {
//            int s = Integer.parseInt(q.getmap().get(key).get(0));
//            int e = Integer.parseInt(q.getmap().get(key).get(1));
//            t.write("NNI:----");
//            for(Integer temp:NNI1.getmap().keySet()){
//                String str = temp + " : ";
//                for(int i = 0;i<NNI1.getmap().get(temp).size();i++){
//                    str += NNI1.getmap().get(temp).get(i)+" ";
//                }
//                t.write(str);
//            }
//            t.write("NPI:----");
//            for(Integer temp:NPI1.getmap().keySet()){
//                String str = temp+" : ";
//                for(int i = 0;i<NPI1.getmap().get(temp).size();i++){
//                    str += NPI1.getmap().get(temp).get(i)+" ";
//                }
//                t.write(str);
//            }
//            Date dt = new Date();
//            ArrayList<Integer> shortpath = new ArrayList<Integer>();
//            if (NNI1.getmap().size() != 0) {
//                flag = search.path(s, e, NNI1, PII, NPI1, dt, null, null, 0, shortpath, true);
//                if (flag == 0 || flag == 1) {
//                    shortpath.addAll(shortpath(key));
//                    insertmap.insert(s, e, shortpath, NNI1, PII, NPI1, dt, null, null, 0, 0.5, 30 * 60 * 1000, 200, false);
//                }
//            } else {
//                shortpath.addAll(shortpath(key));
//                insertmap.insert(s, e, shortpath, NNI1, PII, NPI1, dt, null, null, 0, 0.5, 30 * 60 * 1000, 200, false);
//            }
//        }
    }
}
