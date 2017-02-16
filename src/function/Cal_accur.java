package function;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.*;
/**
 * Created by lijian-mac on 17/1/23.
 */
public class Cal_accur {
    private static double EARTH_RADIUS = 6378137; // radius of earth
    private static final int DEF_DIV_SCALE = 10;
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
    public double GeShortDistance(String point1, String point2)
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
     * 计算某个路径的实际距离
     * @param path
     * @return
     */

    public double getdis(ArrayList<Integer> path,Map<Integer,String> qmap){
        double dis = 0;
        for(int i = 0;i<path.size()-1;i++){
            dis = dis + GeShortDistance(qmap.get(path.get(i)),qmap.get(path.get(i+1)));
        }
        return dis;
    }

    /**
     * 浮点数相减
     * @param a
     * @param b
     */
    public double subtract(double a,double b){
        BigDecimal data1 = new BigDecimal(a);
        BigDecimal data2 = new BigDecimal(b);
        return Math.abs(data1.subtract(data2).doubleValue());
    }
    /**
     * double类型数据相除
     * @param v1 第一个数
     * @param v2 第二个数
     * @return 相除的结果
     */
    public double div(double v1, double v2)
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
    public static double div(double v1, double v2, int scale)
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
    public static  boolean judge(double a,double b){
        BigDecimal data1 = new BigDecimal(a);
        BigDecimal data2 = new BigDecimal(b);
        if(data1.compareTo(data2)<=0){
            return true;
        }
        return false;
    }

    /**
     * 用于判断是否X偏差相等
     *
     * @param path
     * @param truepath
     * @param x
     * @return
     */

    public boolean isX_equal(ArrayList<Integer> path, ArrayList<Integer> truepath, double x, Map<Integer, String> map) {
        double test = getdis(path,map);
        double actu = getdis(truepath,map);
        if (judge(div(subtract(test, actu), actu), x)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 用于进行判断是否都是一样
     */
    public boolean isequal(ArrayList<Integer> path,ArrayList<Integer> truepath){
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
}
