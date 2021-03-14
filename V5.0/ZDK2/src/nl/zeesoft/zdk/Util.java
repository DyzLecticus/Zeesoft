package nl.zeesoft.zdk;

import java.util.ArrayList;
import java.util.List;

public class Util {
	public static boolean equals(Object obj1, Object obj2) {
		boolean r = true;
		if ((obj1==null && obj2!=null) ||
			(obj1!=null && obj2==null) ||
			(obj1!=null && !obj1.equals(obj2)) 
			) {
			r = false;
		}
		return r;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void randomizeList(List list) {
		List<Object> temp = new ArrayList<Object>(list);
		int size = list.size();
		list.clear();
		for (int p = 0; p < size; p++) {
			Object obj = temp.remove(Rand.getRandomInt(0, temp.size() - 1));
			list.add(obj);
		}
	}
}
