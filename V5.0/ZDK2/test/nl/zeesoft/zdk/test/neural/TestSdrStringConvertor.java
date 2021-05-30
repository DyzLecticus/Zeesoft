package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrHistory;
import nl.zeesoft.zdk.neural.SdrHistoryStringConvertor;
import nl.zeesoft.zdk.neural.SdrStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;

public class TestSdrStringConvertor {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		Sdr sdr1 = new Sdr(10);
		sdr1.setBit(1,true);
		sdr1.setBit(3,true);
		
		Sdr sdr2 = new Sdr(10);
		sdr2.setBit(2,true);
		sdr2.setBit(4,true);
		
		SdrHistory sh1 = new SdrHistory();
		sh1.initialize(10);
		
		sh1.push(sdr1);
		sh1.push(sdr2);
		
		SdrHistoryStringConvertor shc = (SdrHistoryStringConvertor) ObjectStringConvertors.getConvertor(SdrHistory.class);
		StringBuilder str = shc.toStringBuilder(sh1);
		
		SdrHistory sh2 = shc.fromStringBuilder(str);
		assert sh2.length == sh1.length;
		assert sh2.capacity == sh1.capacity;
		for (int i = 0; i < sh1.totals.length; i++) {
			assert sh2.totals[i] == sh1.totals[i];
		}
		assert sh2.sdrs.get(0).equals(sh1.sdrs.get(0));

		assert shc.fromStringBuilder(new StringBuilder()) == null;
		assert shc.fromStringBuilder(new StringBuilder("10;3")) != null;
		assert shc.fromStringBuilder(new StringBuilder("qwer;3")) == null;
		assert shc.toStringBuilder(sdr1).length() == 0;
		
		SdrStringConvertor sdc = (SdrStringConvertor) ObjectStringConvertors.getConvertor(Sdr.class);
		assert sdc.fromStringBuilder(new StringBuilder()) == null;
		assert sdc.fromStringBuilder(new StringBuilder("10,3")) != null;
		assert sdc.fromStringBuilder(new StringBuilder("qwer,3")) == null;
		assert sdc.toStringBuilder(sh1).length() == 0;
	}
}
