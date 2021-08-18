package nl.zeesoft.zdk.neural.processor.cl;

public class ValueCount {
	public int		count			= 0;
	public int		lastProcessed	= 0;

	public ValueCount() {
		
	}

	public ValueCount(int count, int lastProcessed) {
		this.count = count;
		this.lastProcessed = lastProcessed;
	}
}
