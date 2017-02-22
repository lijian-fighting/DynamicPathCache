package function;

import object.Query;
import object.Result;
import object.questiontime;
import sun.swing.BakedArrayList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijian on 2016/12/6.
 */
public class getquestion {
    public static List<questiontime> question = new ArrayList<questiontime>();

    public static void read() throws InterruptedException {
        int i = 0;
        for (; ; ) {
            http p = new http(i,question);
            p.start();
            if(i == 4*60*60){
                break;
            }
            i = i+1;
            Thread.sleep(1000);
        }
    }
    public static void getquestion(){
        File re = new File("F:/dataset/study/questiontime.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(re));
            String temp;
            while ((temp = reader.readLine()) != null) {
                String []sb = temp.split(" ");
                questiontime ques = new questiontime(sb[0],sb[1],Integer.parseInt(sb[2]));
                question.add(ques);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException{
        try {
            getquestion();
            System.out.println(question.size());
            read();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class http extends Thread{
    private int i;
    private List<questiontime> question;
    public http(int i,List<questiontime> query) {
        this.i = i;
        this.question = query;
    }
    public void run(){
        for(int j = 0;j<question.size();j++){
            if( i == question.get(j).getTime()){
                find h = new find(question.get(j).getStartpoint(),question.get(j).getEndpoint(),i);
                h.start();
            }
        }
    }
}
class find extends Thread{
    private String start;
    private String end;
    private int time;
    public find(String s,String e,int time) {
        this.start = s;
        this.end = e;
        this.time =time;
    }
    public void run(){
        try {
            Try t = new Try();
            t.request(start,end,time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
