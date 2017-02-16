package function;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import object.Result;

import java.io.File;
import java.util.List;

/**
 * @Author: LiJian
 * @Description:
 * @Date: Created in 16:01 2017/2/15
 * @Modified By: Lijian
 */
public class ResultToExcel {
    public void print(String location,List<Result> res){
        try {
            WritableWorkbook book = Workbook.createWorkbook(new File(location));
            // 生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet("第一页", 0);
            // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
            // 以及单元格内容为test

            Label labe1 = new Label(0, 0, "是否权重更新");
            Label labe2 = new Label(1, 0, "是否pathjoin");
            Label labe3 = new Label(2, 0, "querysize");
            Label labe4 = new Label(3, 0, "cachesize");
            Label labe5 = new Label(4, 0, "Tmax(m)");
            Label labe6 = new Label(5, 0, "A");
            Label labe7 = new Label(6, 0, "N(负数表示不相关)");

            Label labe8  = new  Label(7, 0,  "Ourhit");
            Label labe9  = new  Label(8, 0,  "LRUhit");
            Label labe10 = new  Label(9, 0,  "LFUhit");
            Label labe11 = new  Label(10, 0, "SPChit");

            Label labe12 = new  Label(11, 0,  "Ourtime(ms)");
            Label labe13 = new  Label(12, 0,  "LRUtime(ms)");
            Label labe14 = new  Label(13, 0,  "LFUtime(ms)");
            Label labe15 = new  Label(14, 0,  "SPCtime(ms)");

            Label labe16 = new  Label(15, 0, "Ouraccur(无偏差)");
            Label labe17 = new  Label(16, 0,  "LRUaccur(无偏差)");
            Label labe18 = new  Label(17, 0,  "LFUaccur(无偏差)");
            Label labe19 = new  Label(18, 0,  "SPCaccur(无偏差)");

            Label labe20 = new  Label(19, 0,  "Our5accur(5%无偏差)");
            Label labe21 = new  Label(20, 0,  "LRU5accur(5%无偏差)");
            Label labe22 = new  Label(21, 0,  "LFU5accur(5%无偏差)");
            Label labe23 = new  Label(22, 0,  "SPC5accur(5%无偏差)");

            Label labe24 = new  Label(23, 0,  "Our10accur(10%无偏差)");
            Label labe25 = new  Label(24, 0,  "LRU10accur(10%无偏差)");
            Label labe26 = new  Label(25, 0,  "LFU10accur(10%无偏差)");
            Label labe27 = new  Label(26, 0,  "SPC10accur(10%无偏差)");
            Label labe28 = new  Label(27, 0,  "MAXWEIGHT");
            // 将定义好的单元格添加到工作表中
            sheet.addCell(labe1);
            sheet.addCell(labe2);
            sheet.addCell(labe3);
            sheet.addCell(labe4);
            sheet.addCell(labe5);
            sheet.addCell(labe6);
            sheet.addCell(labe7);
            sheet.addCell(labe8);
            sheet.addCell(labe9);
            sheet.addCell(labe10);
            sheet.addCell(labe11);
            sheet.addCell(labe12);
            sheet.addCell(labe13);
            sheet.addCell(labe14);
            sheet.addCell(labe15);
            sheet.addCell(labe16);
            sheet.addCell(labe17);
            sheet.addCell(labe18);
            sheet.addCell(labe19);
            sheet.addCell(labe20);
            sheet.addCell(labe21);
            sheet.addCell(labe22);
            sheet.addCell(labe23);
            sheet.addCell(labe24);
            sheet.addCell(labe25);
            sheet.addCell(labe26);
            sheet.addCell(labe27);
            sheet.addCell(labe28);
            /**//*
             * 生成一个保存数字的单元格 必须使用Number的完整包路径，否则有语法歧义 单元格位置是第二列，第一行，值为789.123
             */
            int n = 0;
            for(Result result:res){
                String temp1;
                String temp2;
                if(result.getIsweightupdat()){
                    temp1 = "true";
                }else{
                    temp1 = "false";
                }
                if(result.getIspathjoin()){
                    temp2 = "true";
                }else{
                    temp2 = "false";
                }
                Label labeL1 = new Label(0, n+1, temp1);
                sheet.addCell(labeL1);
                Label labeL2 = new Label(1, n+1, temp2);
                sheet.addCell(labeL2);
                Label labeL3 = new Label(2, n+1, String.valueOf(result.getQuerysize()));
                sheet.addCell(labeL3);
                Label labeL4 = new Label(3, n+1, String.valueOf(result.getCachesize()));
                sheet.addCell(labeL4);
                Label labeL5 = new Label(4, n+1, String.valueOf(result.getTmax()));
                sheet.addCell(labeL5);
                Label labeL6 = new Label(5, n+1, String.valueOf(result.getA()));
                sheet.addCell(labeL6);
                Label labeL7 = new Label(6, n+1, String.valueOf(result.getN()));
                sheet.addCell(labeL7);

                Label labeL8 = new Label(7, n+1, String.valueOf(result.getOurhit()));
                sheet.addCell(labeL8);
                Label labeL9 = new Label(8, n+1, String.valueOf(result.getLRUhit()));
                sheet.addCell(labeL9);
                Label labeL10 = new Label(9, n+1, String.valueOf(result.getLFUhit()));
                sheet.addCell(labeL10);
                Label labeL11 = new Label(10, n+1, String.valueOf(result.getSPChit()));
                sheet.addCell(labeL11);

                Label labeL12 = new Label(11, n+1, String.valueOf(result.getOurtime()));
                sheet.addCell(labeL12);
                Label labeL13 = new Label(12, n+1, String.valueOf(result.getLRUtime()));
                sheet.addCell(labeL13);
                Label labeL14 = new Label(13, n+1, String.valueOf(result.getLFUtime()));
                sheet.addCell(labeL14);
                Label labeL15 = new Label(14, n+1, String.valueOf(result.getSPCtime()));
                sheet.addCell(labeL15);

                Label labeL16 = new Label(15, n+1, String.valueOf(result.getOuraccur()));
                sheet.addCell(labeL16);
                Label labeL17 = new Label(16, n+1, String.valueOf(result.getLRUaccur()));
                sheet.addCell(labeL17);
                Label labeL18 = new Label(17, n+1, String.valueOf(result.getLFUaccur()));
                sheet.addCell(labeL18);
                Label labeL19 = new Label(18, n+1, String.valueOf(result.getSPCaccur()));
                sheet.addCell(labeL19);

                Label labeL20 = new Label(19, n+1, String.valueOf(result.getOur5accur()));
                sheet.addCell(labeL20);
                Label labeL21 = new Label(20, n+1, String.valueOf(result.getLRU5accur()));
                sheet.addCell(labeL21);
                Label labeL22 = new Label(21, n+1, String.valueOf(result.getLFU5accur()));
                sheet.addCell(labeL22);
                Label labeL23 = new Label(22, n+1, String.valueOf(result.getSPC5accur()));
                sheet.addCell(labeL23);

                Label labeL24 = new Label(23, n+1, String.valueOf(result.getOur10accur()));
                sheet.addCell(labeL24);
                Label labeL25 = new Label(24, n+1, String.valueOf(result.getLRU10accur()));
                sheet.addCell(labeL25);
                Label labeL26 = new Label(25, n+1, String.valueOf(result.getLFU10accur()));
                sheet.addCell(labeL26);
                Label labeL27 = new Label(26, n+1, String.valueOf(result.getSPC10accur()));
                sheet.addCell(labeL27);
                Label labeL28 = new Label(27,n+1,String.valueOf(result.getMaxweight()));
                sheet.addCell(labeL28);
                n++;
            }
            // 写入数据并关闭文件
            book.write();
            book.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
