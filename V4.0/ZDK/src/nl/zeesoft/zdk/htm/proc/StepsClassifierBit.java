package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;

public class StepsClassifierBit {
	protected ClassifierConfig			config		= null;
	protected int						index		= 0;
	
	protected HashMap<Object,Integer>	valueCounts	= new HashMap<Object,Integer>();
	protected HashMap<String,Integer>	labelCounts	= new HashMap<String,Integer>();
	
	protected StepsClassifierBit(ClassifierConfig config,int index) {
		this.config = config;
		this.index = index;
	}

	protected ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("" + index);
		r.append(";");
		boolean first = true;
		for (Object value: valueCounts.keySet()) {
			if (first) {
				first = false;
				r.append(value.getClass().getSimpleName());
			}
			r.append(";");
			r.append("" + value.toString());
			r.append(",");
			r.append("" + valueCounts.get(value));
		}
		if (r.length()>0) {
			r.append("|");
		}
		first = true;
		for (String label: labelCounts.keySet()) {
			if (first) {
				first = false;
			} else {
				r.append(";");
			}
			r.append(label);
			r.append(",");
			r.append("" + labelCounts.get(label));
		}
		return r;
	}
	
	protected void fromStringBuilder(ZStringBuilder str) {
		valueCounts.clear();
		labelCounts.clear();
		List<ZStringBuilder> valLbl = str.split("|");
		if (valLbl.size()==2) {
			if (valLbl.get(0).length()>0) {
				List<ZStringBuilder> valCounts = valLbl.get(0).split(";");
				index = Integer.parseInt(valCounts.get(0).toString());
				String dataType = valCounts.get(1).toString();
				if (dataType.length()>0) {
					for (ZStringBuilder valCount: valCounts) {
						List<ZStringBuilder> vC = valCount.split(",");
						if (vC.size()==2) {
							Object value = null;
							if (dataType.equals("Integer")) {
								value = Integer.parseInt(vC.get(0).toString());
							} else if (dataType.equals("Long")) {
								value = Long.parseLong(vC.get(0).toString());
							} else if (dataType.equals("Float")) {
								value = Float.parseFloat(vC.get(0).toString());
							}
							int count = Integer.parseInt(vC.get(1).toString());
							valueCounts.put(value,count);
						}
					}
				}
			}
			if (valLbl.get(1).length()>0) {
				List<ZStringBuilder> lblCounts = valLbl.get(1).split(";");
				for (ZStringBuilder lblCount: lblCounts) {
					List<ZStringBuilder> lC = lblCount.split(",");
					if (lC.size()==2) {
						String label = lC.get(0).toString();
						int count = Integer.parseInt(lC.get(1).toString());
						labelCounts.put(label,count);
					}
				}
			}
		}
	}

	protected void associate(SDR activationSDR,DateTimeSDR inputSDR) {
		Object value = inputSDR.keyValues.get(config.valueKey);
		if (value!=null) {
			Integer count = valueCounts.get(value);
			if (count==null) {
				count = new Integer(0);
			}
			count++;
			valueCounts.put(value,count);
			if (count>=config.maxCount) {
				divideValueCountsBy(2);
			}
		}
		String label = (String) inputSDR.keyValues.get(config.labelKey);
		if (label!=null && label.length()>0) {
			Integer count = labelCounts.get(label);
			if (count==null) {
				count = new Integer(0);
			}
			count++;
			labelCounts.put(label,count);
			if (count>=config.maxCount) {
				divideLabelCountsBy(2);
			}
		}
	}
	
	protected void divideValueCountsBy(int div) {
		if (div>1) {
			List<Object> values = new ArrayList<Object>(valueCounts.keySet());
			for (Object value: values) {
				Integer count = valueCounts.get(value);
				if (count<div) {
					valueCounts.remove(value);
				} else {
					count = count / 2;
					valueCounts.put(value,count);
				}
			}
		}
	}
	
	protected void divideLabelCountsBy(int div) {
		if (div>1) {
			List<String> labels = new ArrayList<String>(labelCounts.keySet());
			for (String label: labels) {
				Integer count = labelCounts.get(label);
				if (count<div) {
					labelCounts.remove(label);
				} else {
					count = count / 2;
					labelCounts.put(label,count);
				}
			}
		}
	}
}
