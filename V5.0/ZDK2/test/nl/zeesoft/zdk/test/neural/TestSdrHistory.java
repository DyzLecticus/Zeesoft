package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrHistory;

public class TestSdrHistory {
	public static void main(String[] args) {
		Sdr sdr = new Sdr(10);
		sdr.setBit(0,true);
		sdr.setBit(9,true);
		
		SdrHistory history = new SdrHistory();
		
		history.push(sdr);
		history.applyCapacity();
		assert history.sdrs.size() == 0;
		
		history.initialize(0);
		assert history.length == 1;
		
		history.initialize(10);
		history.capacity = 4;

		history.push(new Sdr(11));
		assert history.sdrs.size() == 0;

		for (int i = 0; i < 10; i++) {
			history.push(sdr);
		}
		assert history.sdrs.size() == 4;
		assert history.totals[0] == 4;
		assert history.totals[1] == 0;
		assert history.totals[9] == 4;
		
		history.capacity = 2;
		history.totals = null;
		history.applyCapacity();
		assert history.sdrs.size() == 4;
	}
}
