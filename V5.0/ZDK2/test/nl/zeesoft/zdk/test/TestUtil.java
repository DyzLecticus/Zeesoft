package nl.zeesoft.zdk.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;

public class TestUtil {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		Util util1 = new Util();
		Util util2 = new Util();
		
		assert Util.equals(util1, util1);
		assert !Util.equals(util1, util2);
		assert Util.equals(null, null);
		assert !Util.equals(util1, null);
		assert !Util.equals(null, util1);
		
		List<Util> utils = new ArrayList<Util>();
		for (int i = 0; i < 100; i++) {
			utils.add(new Util());
		}
		List<Util> utilsBefore = new ArrayList<Util>(utils);
		Util.randomizeList(utils);
		int same = 0;
		for (Util util: utils) {
			if (utilsBefore.get(same)!=util) {
				break;
			}
			same++;
		}
		assert same < 100;
	}
}
