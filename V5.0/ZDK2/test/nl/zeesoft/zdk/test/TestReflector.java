package nl.zeesoft.zdk.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.HistoricalFloat;
import nl.zeesoft.zdk.Lock;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Reflector;

public class TestReflector {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		Logger.setLoggerConsole(new MockConsole());
		
		assert new Reflector() != null;
		
		HistoricalFloat hist = new HistoricalFloat() {
			@SuppressWarnings("unused")
			private int number = 123;
		};
		hist.push(0.0F);
		hist.push(1.0F);
		
		Field fake = Reflector.getFields(new Lock(hist)).get(0);
		fake.setAccessible(true);
		assert Reflector.getFieldValue(hist, fake) == null;
		Reflector.setFieldValue(hist, fake, "");
		
		List<Field> fields = Reflector.getFields(hist);
		Reflector.setFieldValue(hist, fields.get(0), 123, false);
		assert fields.size() == 4;
		fields.get(0).setAccessible(false);
		fields.get(1).setAccessible(true);
		fields.get(2).setAccessible(true);
		fields.get(3).setAccessible(true);
		assert Reflector.getFieldValue(hist, fields.get(0)) == null;
		Reflector.setFieldValue(hist, fields.get(0), 321);
		assert (int)Reflector.getFieldValue(hist, fields.get(0)) == 321;
		
		SortedMap<String,Object> keyValues = Reflector.getFieldValues(hist);
		assert keyValues.size() == 4;
		assert (int)keyValues.get("capacity") == 100;
		assert keyValues.get("floats") instanceof ArrayList;
		assert (int)keyValues.get("number") == 321;
		assert (float)keyValues.get("total") == 1.0F;
	}
}
