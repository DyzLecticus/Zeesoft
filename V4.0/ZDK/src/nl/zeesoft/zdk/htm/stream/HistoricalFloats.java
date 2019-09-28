package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

public class HistoricalFloats {
	public 	int								window			= 1000;

	private List<Float>						history			= new ArrayList<Float>();
	
	public	float							average			= 0;
	public	float							minimum			= 0;
	public	float							maximum			= 0;

	public float addFloat(float f) {
		average = 0;
		minimum = Float.MAX_VALUE;
		maximum = Float.MIN_VALUE;
		history.add(f);
		while (history.size()>window) {
			history.remove(0);
		}
		average = 0;
		for (Float hist: history) {
			average += hist;
			if (hist<minimum) {
				minimum = hist;
			}
			if (hist>maximum) {
				maximum = hist;
			}
		}
		if (average > 0) {
			average = average / window;
		}
		return average;
	}
	
	public void clear() {
		history.clear();
		average = 0;
		minimum = 0;
		maximum = 0;
	}
}
