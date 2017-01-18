package function;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by lijian on 2016/12/6.
 */
public class dealtime {
    private static ArrayList<String> time = new ArrayList<String>();
    public static void readandwrite(){
        try {
            File re=new File("F:/dataset/study/question.txt");
            File wr=new File("F:/dataset/study/questiontime.txt");
            BufferedReader reader = new BufferedReader(new FileReader(re));
            BufferedWriter writer = new BufferedWriter(new FileWriter(wr));
            String temp;
            while((temp = reader.readLine()) != null){
                String []tempstring = temp.split(" ");
                String timetemp = timechange(tempstring[2]);
                String content = tempstring[0] + " "+tempstring[1]+ " "+timetemp;
                writer.write(content);
                writer.newLine();
            }
            writer.close();
            reader.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String timechange(String time){
        String []temp = time.split(":");
        int size = 4*60*60;
        int max = 24*60*60;
        int step = max / size;
        int count = Integer.parseInt(temp[0])*60*60 + Integer.parseInt(temp[1])*60 + Integer.parseInt(temp[2]);
        int h = count/step;
        return String.valueOf(h);
    }
    //将时间进行映射
    public static void main(String[] args) throws IOException {
       readandwrite();
    }
}
