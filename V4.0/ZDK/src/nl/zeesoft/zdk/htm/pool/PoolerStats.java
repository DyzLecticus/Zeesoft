package nl.zeesoft.zdk.htm.pool;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.ZStringBuilder;

public class PoolerStats {
	public int	total					= 0;
	public int	totalLearned			= 0;
	
	public long	calculateOverlapNs		= 0;
	public long	selectActiveNs			= 0;
	public long	learnActiveNs			= 0;
	public long	logActiveNs				= 0;
	public long	calculateActivityNs		= 0;
	public long	updateBoostNs			= 0;
	
	public ZStringBuilder getDescription() {
		DecimalFormat df = new DecimalFormat("0.000");
		ZStringBuilder r = new ZStringBuilder();
		
		appendValue(r,"- Calculating input overlaps took:        ",df,calculateOverlapNs);
		r.append("\n");
		appendValue(r,"- Selecting active columns took:          ",df,selectActiveNs);
		r.append("\n");
		appendValue(r,"- Learning active columns took:           ",df,learnActiveNs);
		r.append("\n");
		appendValue(r,"- Logging column activity took:           ",df,logActiveNs);
		r.append("\n");
		appendValue(r,"- Calculating column group activity took: ",df,calculateActivityNs);
		r.append("\n");
		appendValue(r,"- Updating boost factors took:            ",df,updateBoostNs);
		
		return r;
	}
	
	private void appendValue(ZStringBuilder str,String description,DecimalFormat df,long val) {
		str.append(description);
		ZStringBuilder strVal = new ZStringBuilder(df.format(val / 1000000F));
		while (strVal.length() < 10) {
			strVal.insert(0," ");
		}
		str.append(strVal);
		str.append(" ms");
	}
}
