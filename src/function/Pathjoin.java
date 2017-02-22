package function;

import cache.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * @Author: LiJian
 * @Description:
 * @Date: Created in 12:49 2016/12/19
 * @Modified By:
 */
public class Pathjoin {
    private static double EARTH_RADIUS = 6378137; // radius of earth
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 返回两条路径相交顶点的中间点
     * @param path1 包含起点的路径
     * @param path2 包含终点的路径
     * @param nPI 点边表
     * @return 返回一个中间顶点的编号
     */
    public int back(int path1,int path2,invertedlist nPI){
        /**
         * 用于存放那些被path1和path2共有的点
         */
        ArrayList<Integer> point = new ArrayList<Integer>();
        for(Integer key:nPI.getmap().keySet()){
            if(nPI.getmap().get(key).contains(path1)&&nPI.getmap().get(key).contains(path2)){
                point.add(key);
            }
        }
        if(point.size() == 0){
            return -1;
        }else if(point.size() == 1){
            return point.get(0);
        }else{
            int temp = point.size()/2;
            return point.get(temp);
        }
    }

    /**
     * 将double类型转化为弧度
     * @param d
     * @return double类型的弧度
     */
    public double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两个坐标之间的距离
     * @param point1
     * @param point2
     * @return double的类型的距离
     */
    public  double GeShortDistance(String point1, String point2)
    {
        String []temp1 = point1.split(",");
        double lon1 = Double.parseDouble(temp1[0]);
        double lat1 = Double.parseDouble(temp1[1]);
        String []temp2 = point2.split(",");
        double lon2 = Double.parseDouble(temp2[0]);
        double lat2 = Double.parseDouble(temp2[1]);
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double radLon1 = rad(lon1);
        double radLon2 = rad(lon2);
        if (radLat1 < 0)
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0)
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0)
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0)
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0)
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0)
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = EARTH_RADIUS * Math.cos(radLat1);

        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = EARTH_RADIUS * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1 - z2) * (z1 - z2));
        //余弦定理求夹角
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));
        double dist = theta * EARTH_RADIUS;
        return dist;
    }

    /**
     * 两个double类型相加
     * @param a
     * @param b
     * @return 相加的结果
     */
    public double add(double a,double b){
        BigDecimal data1 = new BigDecimal(a);
        BigDecimal data2 = new BigDecimal(b);
        return data1.add(data2).doubleValue();
    }

    /**
     * double类型数据相除
     * @param v1 第一个数
     * @param v2 第二个数
     * @return 相除的结果
     */
    public  double div(double v1, double v2)
    {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * double类型相除
     * @param v1
     * @param v2
     * @param scale
     * @return 相除的结果
     */
    public  double div(double v1, double v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 用于判断两个double类型数据的大小
     * @param a 表示计算得到的参数
     * @param b 实际参数
     * @return true表示满足条件，可以前者小于后者，false表示不满足条件，前者大于后者
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
     * 用于进行pathjoin
     * @param s 查询的起点
     * @param e 查询的终点
     * @param nNI 点点表
     * @param PII mytest路径信息表
     * @param nPI 点边表
     * @param dt 查询的时间
     * @param LRU lru路径信息表
     * @param LFU lfu路径信息表
     * @param judge 用于判断进行哪种测试，0表示mytest，1表示lru，2表示lfu
     * @param shortpath 用于存放pathjoin路径
     * @param qmap 用于存放点与坐标的对应关系
     * @param N pathjoin的参数
     * @return 返回结果，0表示没有成功的pathjoin，1表示成功pathjoin
     */
    public int join(int s, int e, Subgraph nNI, pathinformation PII, invertedlist nPI, int dt, LRUcache LRU, LFUcache LFU, int judge, ArrayList<Integer> shortpath,Map<Integer,String> qmap,double N){
        /**
         * vm 用于存放对应的点的编号
         */
        int vm = -1;
        /**
         * 用于存放预测的最好结果
         */
        double max = Double.MAX_VALUE;
        /**
         * 用于存放候选节点
         */
        ArrayList<Integer> candidate = new ArrayList<Integer>();
        for(Integer path1:nPI.getmap().get(s)){
            for(Integer path2:nPI.getmap().get(e)){
                if(back(path1,path2,nPI) != -1){
                    candidate.add(back(path1,path2,nPI));
                }
            }
        }
        if(candidate.size() == 0) {
            //表示没有中间节点，无法pathjoin
            return 0;
        }else{
            for(Integer key:candidate){
               if(judge(div(add(GeShortDistance(qmap.get(s),qmap.get(key)),GeShortDistance(qmap.get(key),qmap.get(e))),GeShortDistance(qmap.get(s),qmap.get(e))),N)
                       && judge(add(GeShortDistance(qmap.get(s),qmap.get(key)),GeShortDistance(qmap.get(key),qmap.get(e))),max)){
                   max =  add(GeShortDistance(qmap.get(s),qmap.get(key)),GeShortDistance(qmap.get(key),qmap.get(e)));
                   vm = key;
               }
            }
            if(vm == -1){
                //表示无法pathjoin
                return 0;
            }else {
                //进行搜索，并将搜索结果进行合并，同时返回1
                cachesearch search = new cachesearch();
                ArrayList<Integer> temp1 = new ArrayList<Integer>();
                ArrayList<Integer> temp2 = new ArrayList<Integer>();
                search.path(s, vm, nNI, PII, nPI, dt, LRU, LFU, judge, temp1,false);
                search.path(vm, e, nNI, PII, nPI, dt, LRU, LFU, judge, temp2,false);
                shortpath.addAll(temp1);
                shortpath.addAll(temp2);
                return 1;
            }
        }
    }
}
