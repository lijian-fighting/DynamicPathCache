package function;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lijian on 2016/12/6.
 */
public class test {
    public int[] randomArray(int min,int max,int n){
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
    public void write(String a){
        FileWriter writer = null;
        try { //
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件,false表示覆盖的方式写入
            writer = new FileWriter("F:/dataset/study/seed.txt", true);
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
    public static void main(String[] args) throws ParseException {
        test t = new test();
        int[] reult = t.randomArray(1,17640,17640); // 生成一组随机序列
        for (int key : reult) {
            t.write(String.valueOf(key));
        }
    }
}
