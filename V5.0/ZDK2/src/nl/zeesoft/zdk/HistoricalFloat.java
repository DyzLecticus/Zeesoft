package nl.zeesoft.zdk;

import java.util.ArrayList;
import java.util.List;

public class HistoricalFloat {
	public int			capacity	= 100;

	public float		total		= 0F;
	public List<Float>	floats		= new ArrayList<Float>();
	
	public void push(float val) {
		total += val;
		floats.add(0,val);
		applyCapacity();
	}
	
	public void applyCapacity() {
		while(floats.size() > capacity) {
			Float val = floats.remove((int)(floats.size() - 1));
			total -= val;
		}
	}
	
	public float getAverage() {
		return total / (float)floats.size();
	}
}
