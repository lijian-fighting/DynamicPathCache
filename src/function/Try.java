package function;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.json.JSONArray;
import org.json.JSONObject;

import object.Query;

//http://dev.virtualearth.net/REST/V1/Routes?wp.0=40.91815,111.709216666667&wp.1=40.9109333333333,111.711616666667&key=AsZOWyYLr14AKviF4sCb1pjf6Mxp4U79toQ9hpFlAawE9V6tGnjPCq5JV4hBHIOG
public class Try {
	//private static Query qs = new Query();// 之后路径
	//private Map<Integer, String> pointmap = new LinkedHashMap<Integer, String>();
	public static String[] urls = { "AsZOWyYLr14AKviF4sCb1pjf6Mxp4U79toQ9hpFlAawE9V6tGnjPCq5JV4hBHIOG",
			"AiF5rjhPwqK8UOyzmg8yxFqILQcRouLZiz8JeoEORTRGzpFjAnriYJoRg49KgctN",
			"AvBMPrSwSYiANzMNuBvzk3zEfv1jGbGSmTr9wU7Tv_qiXTVDdH5nKDnaUJQlJ3vP",
			"ApPND2d30kmDtW6AFDYOCNcIBu753PHSYuMtTmfBvIfa7Airtlejo8YLZqYBKWD1",
			"Apenr-qQjqHmaKqELsCTx3MD9KrqqIJvkDv-xcRVJcDK09SYJwWmIA40z2jNiqik",
			"AutcsYM3LnXhou46MImyJFSieb_gHzk_OFfNkd3VJb7EH5y5hMdCRwCt_Qalm5ef", };

	public static int keyNum = 0;

	public static void write(ArrayList<String> qs) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件,false表示覆盖的方式写入
			writer = new FileWriter("F:/dataset/study/newdata3.txt", true);
			String content = "";
			for (int i = 0; i < qs.size(); i++) {
				if (i != qs.size() - 1) {
					if ((i % 5) == 4) {
						content += qs.get(i) + "\r\n";
					} else {
						content += qs.get(i) + " ";
					}
				} else {
					if ((i % 5) != 4) {
						content += qs.get(i) + " " + "end" + "\r\n";
					}else {
						content += qs.get(i) + "\r\n"+ "end" + "\r\n";
					}
				}
			}
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

	public synchronized static String getUrl() {
		String res = urls[keyNum];
		keyNum++;
		if (keyNum >= urls.length) {
			keyNum = 0;
		}
		return res;
	}

	/*public void back(Map<Integer, ArrayList<String>> qmap, Query qs) {
		for (Integer key : qmap.keySet()) {
			String start = qmap.get(key).get(0);
			String end = qmap.get(key).get(1);
			System.out.println(start+","+end);
			try {
				request(start, end, qs);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/
	
	/*public int check(String m){
		for(Integer key:pointmap.keySet()){
			if(m.equals(pointmap.get(key))){
				return key;
			}
		}
		return -1;
	}*/
	
	public void request(String start, String end)throws IOException {
		String result = "";
		BufferedReader in = null;

		String url = "http://dev.virtualearth.net/REST/V1/Routes?";
		url += "wp.0=" + start + "&wp.1=" + end + "&";
		url += "key=" + getUrl();
		// url =
		// "http://dev.virtualearth.net/REST/V1/Routes?wp.0=39.9744616666667,116.305526666667&wp.1=39.9772633333333,116.304821666667&key=AsZOWyYLr14AKviF4sCb1pjf6Mxp4U79toQ9hpFlAawE9V6tGnjPCq5JV4hBHIOG";
		URL realUrl = new URL(url);
		URLConnection connection = realUrl.openConnection();
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		try {
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			JSONObject json = new JSONObject(result);
			JSONArray arr = json.getJSONArray("resourceSets");
			json = arr.getJSONObject(0);
			arr = json.getJSONArray("resources");
			json = arr.getJSONObject(0);
			arr = json.getJSONArray("routeLegs");
			json = arr.getJSONObject(0);
			arr = json.getJSONArray("itineraryItems");
			System.out.println("search:");
			ArrayList<String> point = new ArrayList<String>();
			for (int i = 1; i < arr.length() - 1; i++) {
				json = arr.getJSONObject(i);
				JSONArray temp = json.getJSONObject("maneuverPoint").getJSONArray("coordinates");
				String degree = temp.toString().substring(1, temp.toString().length() - 1);
				if(!degree.equals("")){

					//int size = pointmap.size()+1;
					//int flag = check(degree);
					/*if( flag == -1 ){
						pointmap.put(size, degree);
						System.out.println(pointmap.size());
					}else{
						size = flag;
					}*/
					System.out.print(degree);
					point.add(degree);
				}
			}
			System.out.println();
			write(point);
			/*System.out.println();
			if (qslist.size() != 0) {
				qs.mapput(qslist);
			}*/
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// clock.show("Request in ");
	}
	/*public Query getqs(){
		return qs;
	}*/
}
