package function;

import object.Query;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lijian on 2016/12/6.
 */
public class dealdata {
    private static Query qs = new Query();// 之后路径
    private static Query q = new Query();// 之后路径
    private static Map<Integer,String> qmap = new HashMap<Integer,String>();

    public void write(ArrayList<String> temp,ArrayList<String> temp1) {
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件,false表示覆盖的方式写入
            writer = new FileWriter("F:/dataset/study/temp1.txt", true);
            String content = "";
            for (int i = 0; i < temp.size(); i++) {
                if (i != temp.size() - 1) {
                    if ((i % 5) == 4) {
                        content += qmap.get(Integer.parseInt(temp.get(i))) + "\r\n";
                    } else {
                        content += qmap.get(Integer.parseInt(temp.get(i))) + " ";
                    }
                } else {
                    if ((i % 5) != 4) {
                        content += qmap.get(Integer.parseInt(temp.get(i))) + " " + "end"+":"+temp1.get(2) + "\r\n";
                    }else {
                        content += qmap.get(Integer.parseInt(temp.get(i))) + "\r\n"+ "end"+":"+temp1.get(2)  + "\r\n";
                    }
                }
            }
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

    private boolean isequal(ArrayList<String> path,ArrayList<String> truepath){
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

    public void deal(Query temp,Query temp1){
        boolean flag = false;
        for(Integer key1:qs.getmap().keySet()){
            for(Integer key2:temp.getmap().keySet()){
                if(isequal(qs.getmap().get(key1),temp.getmap().get(key2))){
                    flag = true;
                }
            }
            if(!flag){
                if(qs.getmap().get(key1).size() == 1){
                    flag = false;
                }else {
                    temp.mapput(qs.getmap().get(key1));
                    temp1.mapput(q.getmap().get(key1));
                }
            }else{
                flag = false;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("inia data...");
        loadpoint l = new loadpoint();
        l.load(qs, q, qmap);
        System.out.println("inia data successfully!");
        System.out.println(qs.getmap().size());
        System.out.println(q.getmap().size());
        Query temp = new Query();
        Query temp1 = new Query();
        dealdata d = new dealdata();
        d.deal(temp,temp1);
        System.out.println(temp.getmap().size());
        for(Integer key:temp.getmap().keySet()){
            d.write(temp.getmap().get(key),temp1.getmap().get(key));
        }
    }
}
