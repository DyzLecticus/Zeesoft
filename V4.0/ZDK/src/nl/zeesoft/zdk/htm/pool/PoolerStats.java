package nl.zeesoft.zdk.htm.pool;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.ZStringBuilder;

public class PoolerStats {
	public long	calculateOverlapTotal	= 0;
	public long	selectActiveTotal		= 0;
	public long	learnActiveTotal		= 0;
	public long	logActiveTotal			= 0;
	public long	calculateActivityTotal	= 0;
	public long	boostFactorTotal		= 0;
	
	public ZStringBuilder getDescription() {
		DecimalFormat df = new DecimalFormat("0.000");
		ZStringBuilder r = new ZStringBuilder();
		
		r.append("- Calculating input overlaps took:        ");
		r.append(df.format(calculateOverlapTotal / 1000000F));
		r.append(" ms");

		r.append("\n");
		r.append("- Selecting active columns took:          ");
		r.append(df.format(selectActiveTotal / 1000000F));
		r.append(" ms");

		r.append("\n");
		r.append("- Learning active columns took:           ");
		r.append(df.format(learnActiveTotal / 1000000F));
		r.append(" ms");

		r.append("\n");
		r.append("- Logging column activity took:           ");
		r.append(df.format(logActiveTotal / 1000000F));
		r.append(" ms");

		r.append("\n");
		r.append("- Calculating column group activity took: ");
		r.append(df.format(calculateActivityTotal / 1000000F));
		r.append(" ms");

		r.append("\n");
		r.append("- Updating boost factors took:            ");
		r.append(df.format(boostFactorTotal / 1000000F));
		r.append(" ms");
		
		return r;
	}
}
