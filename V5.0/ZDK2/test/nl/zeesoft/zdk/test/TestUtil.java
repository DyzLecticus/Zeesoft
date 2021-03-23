package nl.zeesoft.zdk.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;

public class TestUtil implements Runnable {
	private static InterruptedException caughtException = null;
	
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
		
		Exception ex = Util.sleep(1);
		AllTests.sleep(10);
		assert ex == null;
		
		Thread runner = new Thread(new TestUtil());
		runner.start();
		AllTests.sleep(1);
		runner.interrupt();
		AllTests.sleep(1);
		assert caughtException != null;
		
		List<Float> values = new ArrayList<Float>();
		assert Util.getStandardDeviation(values) == 0F;
		values.add(1F);
		assert Util.getStandardDeviation(values) == 0F;
		values.add(1F);
		assert Util.getStandardDeviation(values) == 0F;
		values.add(2F);
		assert Util.getStandardDeviation(values) == 0.57735026F;
	}

	@Override
	public void run() {
		caughtException = Util.sleep(100);
	}
}
