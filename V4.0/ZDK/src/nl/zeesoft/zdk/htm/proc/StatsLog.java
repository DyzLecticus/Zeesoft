package nl.zeesoft.zdk.htm.proc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

public class StatsLog {
	protected static final DecimalFormat	df 		= new DecimalFormat("0.000");
	
	public Object							source	= null;
	public List<Stats>						log		= new ArrayList<Stats>();
	
	public StatsLog(Object source) {
		this.source = source;
	}
	
	public StatsLog copy() {
		StatsLog copy = new StatsLog(source);
		for (Stats stats: log) {
			copy.log.add(stats.copy());
		}
		return copy;
	}
	
	public HashMap<String,Long> getTotals() {
		return getTotals(log);
	}
	
	public ZStringBuilder getSummary() {
		return getSummary(log);
	}
	
	public static HashMap<String,Long> getTotals(List<Stats> log) {
		HashMap<String,Long> r = new HashMap<String,Long>();
		List<String> names = log.get(0).names;
		for (Stats stats: log) {
			for (String name: names) {
				Long total = r.get(name);
				if (total==null) {
					total = new Long(0);
				}
				total += stats.getValue(name);
				r.put(name,total);
			}
		}
		return r;
	}
	
	public static ZStringBuilder getSummary(List<Stats> log) {
		ZStringBuilder r = new ZStringBuilder();
		
		if (log.size()>0) {
			List<String> names = log.get(0).names;
			int maxNameLength = 0;
			for (String name: names) {
				if (name.length()>maxNameLength) {
					maxNameLength = name.length();
				}
			}
			int maxValueLength = 0;
			HashMap<String,Long> totals = getTotals(log);
			for (Long total: totals.values()) {
				if (total.toString().length()>maxValueLength) {
					maxValueLength = total.toString().length();
				}
			}
			for (String name: names) {
				Long total = totals.get(name);
				ZStringBuilder labelValue = new ZStringBuilder(name);
				labelValue.append(":");
				for (int i = 0; i < maxNameLength - name.length(); i++) {
					labelValue.append(" ");
				}
				labelValue.append(" ");
				for (int i = 0; i < maxValueLength - total.toString().length(); i++) {
					labelValue.append(" ");
				}
				labelValue.append("" + getStringValueForStatsValue(total));
				
				if (r.length()>0) {
					r.append("\n");
				}
				r.append(labelValue);
			}
		}
		return r;
	}
	
	protected static String getStringValueForStatsValue(Long value) {
		return df.format(value / 1000000F) + " ms";
	}
}
