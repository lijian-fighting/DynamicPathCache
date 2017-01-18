package function;

import object.Query;

import java.io.*;

/**
 * Created by lijian on 2016/12/6.
 */
public class getquestion {
    public static Query qs = new Query();// 之后路径

    public static void read() throws InterruptedException {
        int i = 0;
        for (; ; ) {
            http p = new http(i);
            p.start();
            if(i == 4*60*60){
                break;
            }
            i = i+1;
            Thread.sleep(1000);
        }
    }
    public static void main(String[] args) throws IOException{
        try {
            read();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class http extends Thread{
    private int i;
    public http(int i) {
        this.i = i;
    }
    public void run(){
        File re = new File("F:/dataset/study/questiontime.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(re));
            String temp;
            while ((temp = reader.readLine()) != null) {
                String []sb = temp.split(" ");
                if(Integer.parseInt(sb[2]) == i) {
                    System.out.println(sb[0]+","+sb[1]);
                    find h = new find(sb[0],sb[1]);
                    h.start();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class find extends Thread{
    private String start;
    private String end;
    public find(String s,String e) {
        this.start = s;
        this.end = e;
    }
    public void run(){
        try {
            Try t = new Try();
            t.request(start,end);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
