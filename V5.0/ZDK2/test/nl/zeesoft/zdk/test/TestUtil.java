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
				
		assert Util.getHypotenuse(3, 4) == 5;
		
		StringBuilder sb = new StringBuilder();
		Util.appendLine(sb, "Test");
		assert sb.toString().equals("Test");
		Util.appendLine(sb, "Test");
		assert sb.toString().equals("Test\nTest");
		
		List<Util> utils = new ArrayList<Util>();
		utils.add(new Util());
		utils.add(null);
		utils.add(new Util());
		Util.removeNullValuesFromList(utils);
		assert utils.size() == 2;
	}
}
