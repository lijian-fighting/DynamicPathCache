package function;

import java.util.Map;
import java.util.HashMap;

public class Time {
	private long time = 0;
	private long start = 0;
	private String name;
	private static Map<String, Time> map = new HashMap<String, Time>();

	private Time(String name) {
		this.name = name;
	}

	public synchronized static Time getClock(String name) {

		if (map.containsKey(name)) {
			return map.get(name);
		} else {
			Time newClock = new Time(name);
			map.put(name, newClock);
			return newClock;
		}
	}

	public void close() {
		map.remove(name);
	}

	public void reset() {
		time = 0;
	}

	public void start() {
		start = System.currentTimeMillis();
	}

	public void end() {
		time += System.currentTimeMillis() - start;
	}

	public void show(String s) {
		System.out.println(s + time + "ms");
	}

	public void show(String s, String s2) {
		System.out.println(s + time + "ms" + s2);
	}

	public long getTime() {
		return time;
	}
}
