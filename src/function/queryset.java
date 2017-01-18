package function;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import object.Query;



//用于提取用户的轨迹和询问的时间，并且放到question.txt中
public class queryset {
	private static ArrayList<String> question = new ArrayList<String>();

	public static void loadquery(String queryfile) {
		Try r = new Try();
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(queryfile, "r");
			String temp = null;
			String start = null, end = null;
			ArrayList<String> d = new ArrayList<String>();
			int count = 0;
			while ((temp = rf.readLine()) != null) {
				if (count == 6) {
					start = temp;
				}
				end = temp;
				count++;
			}
			String[] stemp = start.split(",");
			String[] etemp = end.split(",");
			String time = stemp[6];
			String startpoint = stemp[0] + "," + stemp[1];
			String endpoint = etemp[0] + "," + etemp[1];
			String content = startpoint+" "+endpoint+" "+time;
			question.add(content);
			rf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rf != null)
					rf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writequestion(ArrayList<String> question) {
		File file = new File("F:/dataset/study/question.txt");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			for(int i = 0;i<question.size();i++) {
				writer.write(question.get(i));
				writer.newLine();
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

	public static void printDirectory(File f, String temp) {
		if (!f.isDirectory()) {// 如果不是目录，则打印输出
			temp = temp + "/" + f.getName();
			if (f.getName().contains("plt")) {
				System.out.println(temp);
				loadquery(temp);
			}
		} else {
			File[] fs = f.listFiles();
			temp = temp + "/" + f.getName();
			for (int i = 0; i < fs.length; ++i) {
				File file = fs[i];
				printDirectory(file, temp);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		File f = new File("F:/dataset/study/querypoint/Data");
		printDirectory(f, "F:/dataset/study/querypoint");
		writequestion(question);
	}
}
