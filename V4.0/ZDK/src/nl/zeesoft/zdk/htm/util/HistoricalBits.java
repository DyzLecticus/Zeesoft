package nl.zeesoft.zdk.htm.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.zeesoft.zdk.ZStringBuilder;

public class HistoricalBits {
	public 	int				window		= 1000;

	private	Queue<Boolean>	history		= new LinkedList<Boolean>();
	private	float			totalOn		= 0;
	
	public	float			average		= 0;

	public float addBit(boolean on) {
		average = 0;
		history.add(on);
		if (on) {
			totalOn++;
		}
		while (history.size() > window) {
			boolean wasOn = history.remove();
			if (wasOn) {
				totalOn--;
			}
		}
		if (totalOn>0) {
			average = totalOn / (float) history.size();
		}
		return average;
	}
	
	public void clear() {
		history.clear();
		totalOn = 0;
		average = 0;
	}
	
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("" + average);
		r.append(",");
		r.append("" + totalOn);
		for (Boolean b: history) {
			if (b) {
				r.append(",1");
			} else {
				r.append(",0");
			}
		}
		return r;
	}
	
	public void fromStringBuilder(ZStringBuilder str) {
		clear();
		List<ZStringBuilder> elems = str.split(",");
		if (elems.size()>=2) {
			average = Float.parseFloat(elems.get(0).toString());
			totalOn = Float.parseFloat(elems.get(1).toString());
			for (int i = 2; i < elems.size(); i++) {
				int v = Integer.parseInt(elems.get(i).toString());
				if (v==0) {
					history.add(false);
				} else {
					history.add(true);
				}
			}
		}
	}
}
