package function;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lijian on 2016/12/6.
 */
public class test {
    public static void main(String[] args) throws ParseException {
        Queue<Key_value> H = new PriorityQueue<Key_value>(new Comparator<Key_value>() {
            @Override
            public int compare(Key_value o1, Key_value o2) {
                BigDecimal data1 = new BigDecimal(o1.getvalue());
                BigDecimal data2 = new BigDecimal(o2.getvalue());
                int flag = data1.compareTo(data2);
                if (flag < 0)
                    return 1;
                else if (flag == 0)
                    return 0;
                else
                    return -1;
            }
        });
        Key_value a = new Key_value(1,2.0);
        Key_value b = new Key_value(2,3.0);
        Key_value c = new Key_value(3,2.5);
        H.add(a);
        H.add(b);
        H.add(c);
        Key_value temp = H.poll();
        System.out.println(temp.getkey()+"+"+temp.getvalue());
        Key_value d = new Key_value(4,2.3);
        H.add(d);
        while(!H.isEmpty()){
            Key_value e = H.poll();
            System.out.println(e.getkey()+"+"+e.getvalue());
        }
    }
}
