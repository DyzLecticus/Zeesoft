package nl.zeesoft.zdk.test.dai;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.ObjMapTransformer;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;

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
		
		Json json = JsonConstructor.fromObjectUseConvertors(om);
		ObjMap omc = (ObjMap) ObjectConstructor.fromJson(json);
		assert omc.equals(omc);
		
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
	
		json = JsonConstructor.fromObjectUseConvertors(list);
		ObjMapList lc = (ObjMapList) ObjectConstructor.fromJson(json);
		assert lc.maxSize == list.maxSize;
		assert lc.keys.toString().equals(list.keys.toString());
		assert lc.toString().equals(list.toString());
		
		ObjMapList list2 = new ObjMapList();
		list2.add(new ObjMap(0F, 0.5F, 0F));
		list2.add(new ObjMap(0F, 2F, 0F));
		list2.add(new ObjMap(0F, 0.5F));
		ObjMapComparator comparator = new ObjMapComparator();
		assert comparator.calculateSimilarity(list, list2) == 0.8888889F;

		list2.list.add(new ObjMap(2F, 1F, 0F));
		assert comparator.calculateSimilarity(list, list2) == 0.6666667F;
		
		ObjMapTransformer transformer = new ObjMapTransformer();
		ObjMap from = new ObjMap(2F,1,"");
		ObjMap to = new ObjMap(3F,3,"");
		testTransformation(transformer, from, to, comparator);

		to = new ObjMap(-2.5F,-3,"");
		testTransformation(transformer, from, to, comparator);
		
		from = new ObjMap(0F,0,"");
		to = new ObjMap(1F,1,"");
		testTransformation(transformer, from, to, comparator);
		
		to = new ObjMap(10F,10,"");
		testTransformation(transformer, from, to, comparator);

		from = new ObjMap(transformer.minValue / -10F ,-0,"");
		to = new ObjMap(10F,10,"");
		testTransformation(transformer, from, to, comparator);
		
		from = new ObjMap(1F,1,"");
		to = new ObjMap(0F,0,"");
		testTransformation(transformer, from, to, comparator);
	}
	
	private static void testSimilarity(ObjMap a, ObjMap b, float expectedSimilarity) {
		ObjMapComparator calculator = new ObjMapComparator();
		assert calculator.calculateSimilarity(a, b) == expectedSimilarity;
	}
	
	private static void testTransformation(ObjMapTransformer transformer, ObjMap from, ObjMap to, ObjMapComparator comparator) {
		ObjMap transformation = transformer.getTransformation(from, to);
		ObjMap transformed = transformer.applyTransformation(from, transformation);
		assert comparator.calculateSimilarity(to, transformed) == 1.0F;
	}
}
