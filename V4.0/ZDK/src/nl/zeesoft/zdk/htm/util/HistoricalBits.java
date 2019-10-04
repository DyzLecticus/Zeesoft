package nl.zeesoft.zdk.htm.util;

import java.util.LinkedList;
import java.util.Queue;

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
}
