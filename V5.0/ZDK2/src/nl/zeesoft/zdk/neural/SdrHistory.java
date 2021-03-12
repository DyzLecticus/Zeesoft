package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

public class SdrHistory {
	public int			length		= 1;
	public int			capacity	= 100;
	
	public List<Sdr>	sdrs		= new ArrayList<Sdr>();
	public int[]		totals		= null;
	
	public void initialize(int length) {
		sdrs.clear();
		if (length < 1) {
			length = 1;
		}
		this.length = length;
		totals = new int[length];
		for (int i = 0; i < length; i++) {
			totals[i] = 0;
		}
	}
	
	public void push(Sdr sdr) {
		if (totals!=null && sdr.length == length) {
			sdrs.add(0,sdr);
			for (Integer onBit: sdr.onBits) {
				totals[onBit]++;
			}
			applyCapacity();
		}
	}
	
	public void applyCapacity() {
		if (totals!=null && sdrs.size()>capacity) {
			int remove = sdrs.size() - capacity;
			for (int r = 0; r < remove; r++) {
				Sdr rem = sdrs.remove(sdrs.size() - 1);
				for (Integer onBit: rem.onBits) {
					totals[onBit]--;
				}
			}
		}
	}
	
	public float getAverage(Integer bit) {
		float r = 0;
		if (sdrs.size()>0) {
			r = totals[bit] / (float) sdrs.size();
		}
		return r;
	}
	
	public float getTotalAverage() {
		float r = 0;
		if (sdrs.size()>0) {
			for (int i = 0; i < totals.length; i++) {
				r += totals[i];
			}
			if (r>0) {
				r = r / (float) (totals.length * sdrs.size());
			}
		}
		return r;
	}
}
