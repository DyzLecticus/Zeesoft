package nl.zeesoft.zdk.htm.stream;

import java.util.LinkedList;
import java.util.Queue;

public class HistoricalFloats {
	public 	int				window		= 1000;

	private	Queue<Float>	history		= new LinkedList<Float>();
	
	public	float			average		= 0;
	public	float			minimum		= 0;
	public	float			maximum		= 0;

	public float addFloat(float f) {
		average = 0;
		minimum = Float.MAX_VALUE;
		maximum = Float.MIN_VALUE;
		history.add(f);
		while (history.size()>window) {
			history.remove();
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
			average = average / (float) history.size();
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
