package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.MathUtil;

public class MsLogger {
	protected List<Float>	log			= new ArrayList<Float>();
	protected int			maxSize		= 1000;
	
	public synchronized void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	
	@Override
	public synchronized String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Size: ");
		str.append(log.size());
		if (log.size()>0) {
			str.append(", average: ");
			str.append(getAverage());
			str.append(" ms, standard deviation: ");
			str.append(getStdDev());
			str.append(" ms");
		}
		return str.toString();
	}
	
	public synchronized void add(Float timeMs) {
		log.add(0,timeMs);
		while(log.size()>maxSize) {
			log.remove(log.size() - 1);
		}
	}
	
	public synchronized float getAverage() {
		return MathUtil.getAverage(log);
	}
	
	public synchronized float getStdDev() {
		return MathUtil.getStandardDeviation(log);
	}
}
