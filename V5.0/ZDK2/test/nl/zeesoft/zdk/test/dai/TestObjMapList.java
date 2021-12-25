package nl.zeesoft.zdk.test.dai;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;

public class TestObjMapList {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		ObjMap om = new ObjMap(0.1F, -2F);
		assert om.equals(om);
		assert !om.equals(new ObjMap(0.1F, -2.1F));
		assert !om.equals(new ObjMap(0.1F));
		assert !om.equals(null);
		assert !om.equals(new Object());
		assert om.toString().equals("{1:0.1, 2:-2.0}");

		ObjMap om2 = new ObjMap(0.1F);
		om2.values.put("tag", "Pizza");
		assert !om.equals(om2);
		assert !om2.equals(om);
		
		List<Object> values = new ArrayList<Object>();
		values.add(0.1F);
		values.add(-2F);
		om2 = new ObjMap(values);
		assert om2.equals(om);
		om2 = new ObjMap(0.1F, null);
		assert !om2.equals(om);
		assert !om.equals(om2);
		
		testSimilarity(om, om, 1F);
		testSimilarity(om, new ObjMap(0.1F, -2.1F), 0.9883721F);
		testSimilarity(om, new ObjMap(0.1F, 3F), 0.7222222F);
		testSimilarity(om, new ObjMap(0.1F, 40F), 0.54347825F);
		testSimilarity(om, new ObjMap(0.1F), 0.5F);
		
		testSimilarity(om, new ObjMap(null, "Pizza", 5), 0F);
		om = new ObjMap(null, "Pizza");
		testSimilarity(om, new ObjMap(null, "Pizza", 5), 0.6666667F);
		testSimilarity(om, new ObjMap(null, "Pizz", 5), 0.33333334F);
		testSimilarity(om, new ObjMap(null, 1, 5), 0.33333334F);
		testSimilarity(om, new ObjMap("Pizza", null), 0F);
		testSimilarity(new ObjMap(), new ObjMap(), 1F);
		
		ObjMapList list = new ObjMapList();
		list.maxSize = 3;
		list.add(new ObjMap(2F, 1F, 0F));
		assert list.keys.size() == 3;
		list.add(new ObjMap(0F, 0.5F, 0F));
		list.add(new ObjMap(0F, 2F, 0F));
		list.add(new ObjMap(0F, 0.5F, 0F));
		assert list.list.size() == 3;
		assert list.keys.size() == 3;
		assert list.toString().equals("{1:0.0, 2:0.5, 3:0.0}\n{1:0.0, 2:2.0, 3:0.0}\n{1:0.0, 2:0.5, 3:0.0}");
		
		ObjMapList list2 = new ObjMapList();
		list2.add(new ObjMap(0F, 0.5F, 0F));
		list2.add(new ObjMap(0F, 2F, 0F));
		list2.add(new ObjMap(0F, 0.5F));
		ObjMapComparator comparator = new ObjMapComparator();
		assert comparator.calculateSimilarity(list, list2) == 0.8888889F;

		list2.list.add(new ObjMap(2F, 1F, 0F));
		assert comparator.calculateSimilarity(list, list2) == 0.6666667F;
	}
	
	private static void testSimilarity(ObjMap a, ObjMap b, float expectedSimilarity) {
		ObjMapComparator calculator = new ObjMapComparator();
		assert calculator.calculateSimilarity(a, b) == expectedSimilarity;
	}
}
