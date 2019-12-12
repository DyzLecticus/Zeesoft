package nl.zeesoft.zdk.htm.proc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

/**
 * A StatsLog contains a list of Stats objects used to log processor performance statistics.
 */
public class StatsLog {
	protected static final DecimalFormat	df 			= new DecimalFormat("0.000");
	
	public Object							source		= null;
	public int								maxLogSize	= 99999;
	public List<Stats>						log			= new ArrayList<Stats>();
	public List<String>						names		= new ArrayList<String>();
	
	public StatsLog(Object source) {
		this.source = source;
	}
	
	public void addStats(Stats stats) {
		log.add(stats);
		while(log.size()>maxLogSize) {
			log.remove(0);
		}
		for (String name: stats.names) {
			if (!names.contains(name)) {
				names.add(name);
			}
		}
	}
	
	public void clear() {
		log.clear();
		names.clear();
	}
	
	public StatsLog copy() {
		StatsLog copy = new StatsLog(source);
		copy.maxLogSize = maxLogSize;
		for (Stats stats: log) {
			copy.log.add(stats.copy());
		}
		for (String name: names) {
			copy.names.add(name);
		}
		return copy;
	}
	
	public HashMap<String,Long> getTotals() {
		return getTotals(log,names);
	}
	
	public ZStringBuilder getSummary() {
		return getSummary(log,names);
	}
	
	public static HashMap<String,Long> getTotals(List<Stats> log,List<String> names) {
		HashMap<String,Long> r = new HashMap<String,Long>();
		if (log.size()>0) {
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
		}
		return r;
	}
	
	public static ZStringBuilder getSummary(List<Stats> log,List<String> names) {
		ZStringBuilder r = new ZStringBuilder();
		
		if (log.size()>0) {
			int maxNameLength = 10;
			for (String name: names) {
				if (name.length()>maxNameLength) {
					maxNameLength = name.length();
				}
			}
			int maxValueLength = 0;
			HashMap<String,Long> totals = getTotals(log,names);
			for (Long total: totals.values()) {
				int totalLength = getStringValueForStatsValue(total).length();
				if (totalLength>maxValueLength) {
					maxValueLength = totalLength;
				}
			}
			for (String name: names) {
				Long total = totals.get(name);
				if (r.length()>0) {
					r.append("\n");
				}
				r.append(getLabelValue(name,maxNameLength,total,maxValueLength,true));
			}
			if (names.contains("total")) {
				long totalAvg = totals.get("total") / log.size();
				if (r.length()>0) {
					r.append("\n");
				}
				r.append(getLabelValue("logSize",maxNameLength,(long)log.size(),maxValueLength,false));
				r.append("\n");
				r.append(getLabelValue("avgPerLog",maxNameLength,totalAvg,maxValueLength,true));
			}
		}
		return r;
	}
	
	protected static ZStringBuilder getLabelValue(String name,int maxNameLength,Long total,int maxValueLength,boolean isNanoSeconds) {
		ZStringBuilder r = new ZStringBuilder(name);
		r.append(":");
		for (int i = 0; i < maxNameLength - name.length(); i++) {
			r.append(" ");
		}
		r.append(" ");
		
		String strVal = "" + total + "   ";
		if (isNanoSeconds) {
			strVal = getStringValueForStatsValue(total);
		}
		int strLength = strVal.length();
		for (int i = 0; i < maxValueLength - strLength; i++) {
			r.append(" ");
		}
		r.append(strVal);
		return r;
	}
	
	protected static String getStringValueForStatsValue(Long value) {
		return df.format(value / 1000000F) + " ms";
	}
}
