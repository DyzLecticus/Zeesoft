package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.HistoricalFloat;
import nl.zeesoft.zdk.Logger;

public class TestHistoricalFloat {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		HistoricalFloat hist = new HistoricalFloat();
		hist.capacity = 4;
		hist.push(1.0F);
		hist.push(1.0F);
		hist.push(1.0F);
		hist.push(0.0F);
		
		assert hist.getAverage() == 0.75F;
		hist.push(0.0F);
		assert hist.floats.size() == 4;
		assert hist.getAverage() == 0.5F;
	}
}
