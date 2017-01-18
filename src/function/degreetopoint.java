package function;

import object.Query;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static function.Test2.qmap;

/**
 * Created by lijian on 2016/12/7.
 */
public class degreetopoint {
    public static Query qs = new Query();// 之后路径
    public static Query q = new Query();// 之后路径


    public static void write(Query q,String path) {
        FileWriter writer = null;
        File file = new File(path);
        try { //
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件,false表示覆盖的方式写入
            writer = new FileWriter(file, true);
            for (Integer key : q.getmap().keySet()) {
                    String content = "";
                    for (int i = 0; i < q.getmap().get(key).size(); i++) {
                        if (i != q.getmap().get(key).size() - 1) {
                            if ((i % 5) == 4) {
                                content += q.getmap().get(key).get(i) + "\r\n";
                            } else {
                                content += q.getmap().get(key).get(i) + " ";
                            }
                        } else {
                            if ((i % 5) != 4) {
                                content += q.getmap().get(key).get(i) + " " + "end" + "\r\n";
                            } else {
                                content += q.getmap().get(key).get(i) + "\r\n" + "end" + "\r\n";
                            }
                        }
                    }
                    writer.write(content);
                }
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

    public static void main(String[] args) throws IOException {
        loadpoint l = new loadpoint();
        l.load(qs, q, qmap);
        System.out.println(qs.getmap().size());
        System.out.println(q.getmap().size());
        //write(qs,"F:/dataset/study/qs.txt");
        //write(q,"F:/dataset/study/q.txt");
        int size = l.getsize();
        System.out.println(size);
        System.out.println("初始化查询起终点成功");
    }
}
