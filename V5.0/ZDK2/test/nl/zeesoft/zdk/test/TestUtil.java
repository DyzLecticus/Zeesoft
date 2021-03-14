package nl.zeesoft.zdk.test;

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
	}
}
