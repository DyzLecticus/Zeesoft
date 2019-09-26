package nl.zeesoft.zdk.htm.proc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

public class Stats {
	private static final DecimalFormat	df 		= new DecimalFormat("0.000");
	
	public List<String>					names	= new ArrayList<String>();
	public List<Long>					values	= new ArrayList<Long>();
	
	public Stats copy() {
		Stats copy = new Stats();
		copy.names = new ArrayList<String>(names);
		copy.values = new ArrayList<Long>(values);
		return copy;
	}
	
	public void setValue(String name,long value) {
		names.add(name);
		values.add(value);
	}
	
	public Long getValue(String name) {
		Long r = null;
		int index = names.indexOf(name);
		if (index>=0 && index < values.size()) {
			r = values.get(index);
		}
		return r;
	}
	
	public static ZStringBuilder getSummary(List<Stats> statsLog) {
		ZStringBuilder r = new ZStringBuilder();
		
		if (statsLog.size()>0) {
			List<String> names = statsLog.get(0).names;
			int maxNameLength = 0;
			for (String name: names) {
				if (name.length()>maxNameLength) {
					maxNameLength = name.length();
				}
			}
			int maxValueLength = 0;
			HashMap<String,Long> totals = new HashMap<String,Long>();
			for (Stats stats: statsLog) {
				for (String name: names) {
					Long total = totals.get(name);
					if (total==null) {
						total = new Long(0);
					}
					total += stats.getValue(name);
					totals.put(name,total);
					if (total.toString().length()>maxValueLength) {
						maxValueLength = total.toString().length();
					}
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
