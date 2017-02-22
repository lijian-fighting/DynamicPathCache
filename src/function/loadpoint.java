package function;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import object.Query;

public class loadpoint {
	public static Map<Integer, String> pointmap = new HashMap<Integer, String>();
	
	public void writepoint(String a,String b){
		FileWriter writer = null;
		try { //
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件,false表示覆盖的方式写入
			writer = new FileWriter("F:/dataset/study/log.txt", true);
			String content = a+" "+b +"\r\n";
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

	public int check(String m){
		for(Integer key:pointmap.keySet()){
			if(m.equals(pointmap.get(key))){
				return key;
			}
		}
		return -1;
	}
	
	public void load(Query qs, Query q,Map<Integer,String> qmap) {
		BufferedReader reader = null;
		try {
//			reader = new BufferedReader(new FileReader("/root/lijian/temp.txt"));
			reader = new BufferedReader(new FileReader("F:/dataset/study/temp.txt"));
			String tempString = null;
			ArrayList<String> qslist = new ArrayList<String>();
			String time = null;
			while ((tempString = reader.readLine()) != null) {		
				String[] temp = tempString.split("\\s+");
				for (int i = 0; i < temp.length; i++) {
					String degree = temp[i];
					if(!degree.contains("end")){
						int size = pointmap.size()+1;
						int flag = check(degree);
						if( flag == -1 ){
//							writepoint(String.valueOf(size),degree);
							qmap.put(size,degree);
							pointmap.put(size, degree);
						}else{
							size = flag;
						}
						qslist.add(String.valueOf(size));
					}else{
						String []arrival = degree.split(":");
						time = arrival[1];
					}
				}
				if(tempString.contains("end")){
					qs.mapput(qslist);
					ArrayList<String> qlist = new ArrayList<String>();
					qlist.add(qslist.get(0));
					qlist.add(qslist.get(qslist.size()-1));
					qlist.add(time);
					q.mapput(qlist);
					qslist = new ArrayList<String>();
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	public void loadseed(int []temp) {
		BufferedReader reader = null;
		try {
//			reader = new BufferedReader(new FileReader("/root/lijian/seed.txt"));
			reader = new BufferedReader(new FileReader("F:/dataset/study/seed.txt"));
			String tempString = null;
			int count = 0;
			while ((tempString = reader.readLine()) != null) {
				temp[count] = Integer.parseInt(tempString.trim());
				count ++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public int getsize(){
		/*for(Integer key:pointmap.keySet()){
			System.out.println("key ="+key+"value ="+ pointmap.get(key));
		}*/
		return pointmap.size();
	}
}
