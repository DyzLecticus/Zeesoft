package nl.zeesoft.zdk.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.code.CodeFile;

public class TestInstantiator {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		assert Instantiator.getNewClassInstance(System.class) == null;
		assert Instantiator.getNewClassInstance(CodeFile.class.getName()) == null;
		assert Instantiator.getNewClassInstance(Instantiator.class.getName()) != null;
		assert Instantiator.getNewClassInstance("Pizza") == null;

		assert Instantiator.getNewClassInstance(String.class).equals("");
		assert (int)Instantiator.getNewClassInstance(Integer.class) == 0;
		assert (long)Instantiator.getNewClassInstance(Long.class) == 0L;
		assert (float)Instantiator.getNewClassInstance(Float.class) == 0F;
		assert (double)Instantiator.getNewClassInstance(Double.class) == 0D;
		assert (boolean)Instantiator.getNewClassInstance(Boolean.class) == false;
		assert Instantiator.getNewClassInstance(Byte.class).equals(0);
		assert Instantiator.getNewClassInstance(Short.class).equals(0);
		
		assert Instantiator.getNewArrayInstance("class [Ljava.lang.String;", 2) instanceof String[];
		assert Instantiator.getNewArrayInstance("[Ljava.lang.String;", 2) instanceof String[];
		assert Instantiator.getNewArrayInstance("[I", 2) instanceof int[];
		assert Instantiator.getNewArrayInstance("[J", 2) instanceof long[];
		assert Instantiator.getNewArrayInstance("[F", 2) instanceof float[];
		assert Instantiator.getNewArrayInstance("[D", 2) instanceof double[];
		assert Instantiator.getNewArrayInstance("[Z", 2) instanceof boolean[];
		assert Instantiator.getNewArrayInstance("[B", 2) instanceof byte[];
		assert Instantiator.getNewArrayInstance("[S", 2) instanceof short[];
		
		assert Instantiator.getNewArrayInstance("class [LPizza;", 2) == null;
		assert Instantiator.getNewArrayInstance("[Q", 2) == null;
		
		List<Object> values = new ArrayList<Object>();
		values.add("A");
		values.add("B");
		String[] strs = (String[]) Instantiator.getNewArrayInstance("[Ljava.lang.String;", values);
		assert strs[0].equals("A");
		assert strs[1].equals("B");
		
		assert Instantiator.getNewArrayInstance("[LPizza;", values) == null;
	}
}
