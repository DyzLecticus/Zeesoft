package nl.zeesoft.zdk.str;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Instantiator;

public class ObjectStringConvertors {
	private static List<ObjectStringConvertor>	convertors	= new ArrayList<ObjectStringConvertor>();
	
	protected static synchronized void addConvertor(ObjectStringConvertor convertor) {
		ObjectStringConvertor existing = getExistingConvertor(convertor.getObjectClass());
		if (existing==null) {
			convertors.add(convertor);
		}
	}
	
	public static synchronized ObjectStringConvertor getConvertor(Class<?> cls) {
		ObjectStringConvertor r = getExistingConvertor(cls);
		if (r==null) {
			r = getNewConvertor(cls);
			if (r!=null) {
				convertors.add(r);
			}
		}
		return r;
	}
	
	private static ObjectStringConvertor getExistingConvertor(Class<?> cls) {
		ObjectStringConvertor r = null;
		for (ObjectStringConvertor convertor: convertors) {
			if (convertor.getObjectClass()==cls) {
				r = convertor;
				break;
			}
		}
		return r;
	}
	
	private static ObjectStringConvertor getNewConvertor(Class<?> cls) {
		ObjectStringConvertor r = null;
		Class<?> ccls = Instantiator.getClassForName(cls.getName() + "StringConvertor");
		if (ccls!=null) {
			Object obj = Instantiator.getNewClassInstance(ccls);
			if (obj instanceof ObjectStringConvertor) {
				r = (ObjectStringConvertor) obj;				
			}
		}
		return r;
	}
}
