package nl.zeesoft.zdk.htm.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.zeesoft.zdk.ZStringBuilder;

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
	
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("" + average);
		r.append(",");
		r.append("" + minimum);
		r.append(",");
		r.append("" + maximum);
		for (Float f: history) {
			r.append(",");
			r.append("" + f);
		}
		return r;
	}
	
	public void fromStringBuilder(ZStringBuilder str) {
		clear();
		List<ZStringBuilder> elems = str.split(",");
		if (elems.size()>=3) {
			average = Float.parseFloat(elems.get(0).toString());
			minimum = Float.parseFloat(elems.get(1).toString());
			maximum = Float.parseFloat(elems.get(2).toString());
			for (int i = 3; i < elems.size(); i++) {
				float f = Float.parseFloat(elems.get(i).toString());
				history.add(f);
			}
		}
	}
}
