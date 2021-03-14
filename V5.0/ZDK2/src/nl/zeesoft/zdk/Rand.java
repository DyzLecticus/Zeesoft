package nl.zeesoft.zdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rand {
	private static Random 	random		= new Random();
	
	public static float getRandomFloat(float min, float max) {
		float r = min;
		if (min<max) {
			r = min + random.nextFloat() * (max - min);
		}
		return r;
	}

	public static int getRandomInt(int min, int max) {
		int r = min;
		if (min<max) {
			r = Math.round(getRandomFloat(min,max));
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
	
	@SuppressWarnings({ "rawtypes" })
	public static Object selectRandomFromList(List list) {
		return list.get(Rand.getRandomInt(0, list.size() - 1));
	}
}
