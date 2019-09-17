package nl.zeesoft.zdk.htm.pool;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.ZStringBuilder;

public class StatsObject {
	protected	DecimalFormat 	df		= new DecimalFormat("0.000");

	public 		int				total	= 0;
	public 		long			totalNs	= 0;
	
	public ZStringBuilder getDescription() {
		return new ZStringBuilder();
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
	
	protected void appendNsPerTotal(ZStringBuilder str,String description) {
		str.append(description);
		str.append(" ");
		ZStringBuilder strVal = new ZStringBuilder(df.format((totalNs / total) / 1000000F));
		while (strVal.length() < 10) {
			strVal.insert(0," ");
		}
		str.append(strVal);
		str.append(" ms");
	}
}
