package object;

/**
 * @Author: LiJian
 * @Description:
 * @Date: Created in 17:57 2017/2/19
 * @Modified By:
 */
public class questiontime {
    String startpoint;
    String endpoint;
    int time;
    public questiontime(String s,String e,int t){
        this.startpoint = s;
        this.endpoint = e;
        this.time = t;
    }
    public String getStartpoint(){
        return startpoint;
    }
    public  String getEndpoint(){
        return  endpoint;
    }
    public  int getTime(){
        return  time;
    }
}
