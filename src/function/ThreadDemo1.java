package function;

/**
 * Created by lijian on 2016/12/6.
 */
public class ThreadDemo1 {
    public static void main(String[] args){
        for(int i=0;i<60;i++){
            Demo d = new Demo();
            d.start();
            System.out.println(Thread.currentThread().getName()+i);
        }

    }
}
class Demo extends Thread{
    public void run(){
        for(int i=0;i<60;i++){
            System.out.println(Thread.currentThread().getName()+i);
        }
    }
}
