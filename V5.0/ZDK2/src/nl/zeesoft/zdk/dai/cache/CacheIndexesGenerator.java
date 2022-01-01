package nl.zeesoft.zdk.dai.cache;

import java.util.ArrayList;
import java.util.List;

public class CacheIndexesGenerator {
	public static List<Integer> generate() {
		return (new CacheIndexesGenerator()).generate(4, 8);
	}
	
	public List<Integer> generate(int num, int depth) {
		List<Integer> r = new ArrayList<Integer>();
		addDefault(r, depth);
		addFibonacci(r, depth);
		for (int i = 2; i < num; i++) {
			addPower(r, depth, i);
		}
		return r;
	}
	
	public void addDefault(List<Integer> list, int depth) {
		for (int i = 0; i < depth; i++) {
			addToList(list, i);
		}
	}
	
	public void addFibonacci(List<Integer> list, int depth) {
		List<Integer> sequence = getFibonacci(depth);
		for (Integer i: sequence) {
			addToList(list, i);
		}
	}
	
	public void addPower(List<Integer> list, int depth, int factor) {
		int i = 0;
		for (int d = 0; d < depth; d++) {
			addToList(list, i);
			if (i==0) {
				i = 1;
			} else {
				i = i * factor;
			}
		}
	}

	public List<Integer> getFibonacci(int depth) {
		List<Integer> r = new ArrayList<Integer>();
		int i = 0;
		for (int d = 0; d <= depth; d++) {
			r.add(i);
			if (i==0) {
				i = 1;
			} else {
				i += r.get(d - 1);
			}
		}
		if (r.size()>=2) {
			r.remove(1);
		}
		return r;
	}

	private void addToList(List<Integer> list, int val) {
		if (!list.contains(val)) {
			list.add(val);
		}
	}
}
