package nl.zeesoft.zdk.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.StrUtil;
import nl.zeesoft.zdk.Util;

public class TestUtil {
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
		
		Util.applySizeLimitToList(utils, 0);
		assert utils.size() == 0;
		utils.add(new Util());
		utils.add(new Util());
		Util.applySizeLimitToList(utils, 1);
		assert utils.size() == 1;
		Util.applySizeLimitToList(utils, 1);
		assert utils.size() == 1;
		

		Exception ex = Util.sleep(1);
		ZdkTests.sleep(10);
		assert ex == null;
		
		Thread runner = new Thread() {
			@Override
			public void run() {
				caughtException = Util.sleep(100);
			}
		};
		runner.start();
		ZdkTests.sleep(1);
		runner.interrupt();
		ZdkTests.sleep(1);
		assert caughtException != null;

		ex = Util.sleepNs(1);
		ZdkTests.sleep(1);
		assert ex == null;
		
		caughtException = null;
		runner = new Thread() {
			@Override
			public void run() {
				caughtException = Util.sleepNs(100000);
			}
		};
		runner.start();
		runner.interrupt();
		ZdkTests.sleep(1);
		assert caughtException != null;
		
		List<Float> values = new ArrayList<Float>();
		assert Util.getStandardDeviation(values) == 0F;
		values.add(1F);
		assert Util.getStandardDeviation(values) == 0F;
		values.add(1F);
		assert Util.getStandardDeviation(values) == 0F;
		values.add(2F);
		assert Util.getStandardDeviation(values) == 0.57735026F;
		
		// Test StrUtil
		StrUtil strUtil = new StrUtil();
		assert strUtil != null;
		StringBuilder str = new StringBuilder();
		StrUtil.trim(str);
		assert str.length()==0;
		assert StrUtil.indexOf(str, "qwer", 0) == -1;
		str = new StringBuilder("  Test  ");
		assert StrUtil.indexOf(str, "qwer", 0) == -1;
		StrUtil.trim(str);
		assert str.toString().equals("Test");
		assert StrUtil.startsWith(str, "T");
		assert !StrUtil.startsWith(str, "A");
		List<StringBuilder> elems = StrUtil.split(str, "e");
		assert elems.size() == 2;
		assert elems.get(0).toString().equals("T");
		assert elems.get(1).toString().equals("st");
		assert StrUtil.split(new StringBuilder(), "").size() == 1;
		str = new StringBuilder("Test1,Test2,");
		assert StrUtil.split(str,",").size() == 3;
	}
}
