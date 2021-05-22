package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.HistoricalFloat;
import nl.zeesoft.zdk.HistoricalFloatStringConvertor;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.str.ObjectStringConvertors;

public class TestHistoricalFloatStringConvertor {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		HistoricalFloat hist = new HistoricalFloat();
		hist.capacity = 4;
		hist.push(1.0F);
		hist.push(1.0F);
		hist.push(1.0F);
		hist.push(0.0F);
		
		HistoricalFloatStringConvertor hfsc = (HistoricalFloatStringConvertor) ObjectStringConvertors.getConvertor(HistoricalFloat.class);
		
		StringBuilder str = hfsc.toStringBuilder(hist);
		
		hist = hfsc.fromStringBuilder(str);
		assert hist.capacity == 4;
		assert hist.total == 3.0F;
		assert hist.floats.get(0) == 0.0F;
		assert hist.floats.get(1) == 1.0F;
		assert hist.floats.get(2) == 1.0F;
		assert hist.floats.get(3) == 1.0F;
		
		assert hfsc.toStringBuilder(hfsc).length() == 0;
		assert hfsc.toStringBuilder(new HistoricalFloat()).length() == 0;
		assert hfsc.fromStringBuilder(new StringBuilder()) == null;
		assert hfsc.fromStringBuilder(new StringBuilder(",")) == null;
	}
}
