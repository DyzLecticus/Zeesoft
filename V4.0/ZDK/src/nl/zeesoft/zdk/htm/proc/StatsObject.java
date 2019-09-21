package nl.zeesoft.zdk.htm.proc;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.ZStringBuilder;

public abstract class StatsObject {
	protected	DecimalFormat 	df		= new DecimalFormat("0.000");

	public 		int				total	= 0;
	public 		long			totalNs	= 0;
	
	public ZStringBuilder getDescription() {
		return new ZStringBuilder();
	}
	
	public abstract StatsObject copy();
	
	protected void copyTo(StatsObject copy) {
		copy.total = total;
		copy.totalNs = totalNs;
	}
	
	protected void appendValue(ZStringBuilder str,String description,long val) {
		str.append(description);
		str.append(" ");
		ZStringBuilder strVal = new ZStringBuilder(df.format(val / 1000000F));
		while (strVal.length() < 10) {
			strVal.insert(0," ");
		}
		str.append(strVal);
		str.append(" ms");
	}
	
	protected void appendTotal(ZStringBuilder str,String description) {
		str.append(description);
		str.append(" ");
		ZStringBuilder strVal = new ZStringBuilder("" + total);
		while (strVal.length() < 10) {
			strVal.insert(0," ");
		}
		str.append(strVal);
	}
	
	protected void appendNsPerTotal(ZStringBuilder str,String description) {
		str.append(description);
		str.append(" ");
		long val = 0;
		if (total>0) {
			val = (totalNs / total);
		}
		ZStringBuilder strVal = new ZStringBuilder(df.format(val / 1000000F));
		while (strVal.length() < 10) {
			strVal.insert(0," ");
		}
		str.append(strVal);
		str.append(" ms");
	}
}
