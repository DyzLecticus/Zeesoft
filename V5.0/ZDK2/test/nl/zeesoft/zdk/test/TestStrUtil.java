package nl.zeesoft.zdk.test;

import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.StrUtil;

public class TestStrUtil {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
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
		assert !StrUtil.startsWith(str, "Testers");
		assert StrUtil.equals(str, "Test");
		assert !StrUtil.equals(str, "Qwer");
		assert !StrUtil.equals(str, "Testers");
		assert StrUtil.equalsIgnoreCase(str, "TEST");
		assert !StrUtil.equalsIgnoreCase(str, "TEXT");
		StrUtil.trim(str,"t");
		assert StrUtil.equals(str, "Tes");
		
		str = new StringBuilder("Test");
		List<StringBuilder> elems = StrUtil.split(str, "e");
		assert elems.size() == 2;
		assert elems.get(0).toString().equals("T");
		assert elems.get(1).toString().equals("st");
		assert StrUtil.split(new StringBuilder(), "").size() == 1;
		str = new StringBuilder("Test1,Test2,");
		assert StrUtil.split(str,",").size() == 3;
	}
}
