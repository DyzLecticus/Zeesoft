package nl.zeesoft.zdk.test;

import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;
import nl.zeesoft.zdk.test.json.MockObject;

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
		
		assert new ObjectStringConvertors() != null;
		testValueConvertor("Pizza", String.class, "Pizza");
		testValueConvertor("1", Integer.class, 1);
		testValueConvertor("2", Long.class, 2L);
		testValueConvertor("0.1", Float.class, 0.1F);
		testValueConvertor("0.2", Double.class, 0.2D);
		testValueConvertor("true", Boolean.class, true);
		testValueConvertor("3", Byte.class, new Integer(3).byteValue());
		testValueConvertor("4", Short.class, new Integer(4).shortValue());
		str = new StringBuilder("Stuff"); 
		Object value = ObjectStringConvertor.convertStringBuilderToPrimitive(str, StringBuilder.class);
		assert StrUtil.equals(str, (StringBuilder)value);
		assert ObjectStringConvertor.convertStringBuilderToPrimitive(str, StringBuffer.class) == str;
		assert ObjectStringConvertor.convertStringBuilderToPrimitive(str, null) == str;
		assert ObjectStringConvertor.convertStringBuilderToPrimitive(null, null) == null;
		assert ObjectStringConvertors.getConvertor(MockObject.class) == null;
		
		str = ObjectStringConvertor.getDataTypeStringBuilderForObject("Pizza", "#");
		assert str.toString().equals("java.lang.String#Pizza");
		assert ObjectStringConvertor.getObjectForDataTypeStringBuilder(str, "#").equals("Pizza");
		assert StrUtil.equals(ObjectStringConvertor.getDataTypeStringBuilderForObject(null, "#"), StrUtil.NULL);
		assert ObjectStringConvertor.getObjectForDataTypeStringBuilder(StrUtil.NULL, "#") == null;
	}
	
	public static void testValueConvertor(String value, Class<?> cls, Object exp) {
		Object val = ObjectStringConvertor.convertStringBuilderToPrimitive(new StringBuilder(value), cls);
		assert val.equals(exp);
	}
}
