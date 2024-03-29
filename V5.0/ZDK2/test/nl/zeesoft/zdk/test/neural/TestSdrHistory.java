package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrHistory;

public class TestSdrHistory {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		Sdr sdr = new Sdr(10);
		sdr.setBit(0,true);
		sdr.setBit(9,true);
		
		SdrHistory history = new SdrHistory();
		
		history.push(sdr);
		history.applyCapacity();
		assert history.sdrs.size() == 0;
		assert history.getAverage(0) == 0F;
		assert history.getTotalAverage() == 0F;
		
		history.initialize(0);
		assert history.length == 1;
		assert history.sdrs.size() == 0;
		assert history.getAverage(0) == 0F;
		assert history.getTotalAverage() == 0F;
		
		history.initialize(10);
		history.capacity = 4;

		history.push(new Sdr(11));
		assert history.sdrs.size() == 0;

		history.push(new Sdr(10));
		assert history.getTotalAverage() == 0F;

		for (int i = 0; i < 10; i++) {
			history.push(sdr);
		}
		assert history.sdrs.size() == 4;
		assert history.totals[0] == 4;
		assert history.totals[1] == 0;
		assert history.totals[9] == 4;
		
		assert history.getAverage(0) == 1F;
		assert history.getAverage(1) == 0F;
		assert history.getAverage(9) == 1F;
		assert history.getTotalAverage() == 0.2F;

		history.push(new Sdr(10));
		history.push(new Sdr(10));

		assert history.getAverage(0) == 0.5F;
		assert history.getAverage(1) == 0F;
		assert history.getAverage(9) == 0.5F;
		assert history.getTotalAverage() == 0.1F;

		history.capacity = 2;
		history.totals = null;
		history.applyCapacity();
		assert history.sdrs.size() == 4;
	}
}
