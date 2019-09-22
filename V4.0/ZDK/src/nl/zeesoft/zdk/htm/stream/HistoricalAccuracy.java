package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

public class HistoricalAccuracy {
	public 	int								window			= 1000;

	private List<Float>						history			= new ArrayList<Float>();
	
	public	float							average			= 0;

	public float addAccuracy(float f) {
		average = 0;
		history.add(f);
		while (history.size()>window) {
			history.remove(0);
		}
		average = 0;
		for (Float hist: history) {
			average += hist;
		}
		if (average > 0) {
			average = average / window;
		}
		return average;
	}
}
