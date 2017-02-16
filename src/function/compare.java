package function;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Author: LiJian
 * @Description:
 * @Date: Created in 14:40 2017/2/16
 * @Modified By:
 */
public class compare {
    public static void main(String[] args) {
        BufferedReader reader = null;
        BufferedReader reader1 = null;
        try {
            reader = new BufferedReader(new FileReader("F:/dataset/study/lijian.txt"));
            reader1 = new BufferedReader(new FileReader("F:/dataset/study/zhouyy.txt"));
            String tempString = null;
            String tempString1 = null;
            int count = 0;
            while (((tempString = reader.readLine()) != null)&&((tempString1 = reader1.readLine()) != null)) {
                if(!tempString.equals(tempString1)){
                    System.out.println("no");
                    System.out.println(tempString+","+tempString1);
                    return;
                }
            }
            System.out.println("yes");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null&&reader1!=null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
