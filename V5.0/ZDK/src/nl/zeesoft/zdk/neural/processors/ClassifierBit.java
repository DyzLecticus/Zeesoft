package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;

public class ClassifierBit implements StrAble {
	// Configuration
	protected int						index				= 0;
	protected int						maxCount			= 0;
	
	// State
	protected HashMap<Object,Integer>	valueCounts			= new HashMap<Object,Integer>();
	
	protected ClassifierBit(int index, int maxCount) {
		this.index = index;
		this.maxCount = maxCount;
	}

	@Override
	public Str toStr() {
		Str r = new Str();
		r.sb().append(index);
		r.sb().append(";");
		r.sb().append(maxCount);
		r.sb().append(";");
		if (valueCounts!=null && valueCounts.size()>0) {
			Str valCounts = new Str();
			String className = "";
			for (Entry<Object,Integer> entry: valueCounts.entrySet()) {
				if (valCounts.length()>0) {
					valCounts.sb().append("%");
				}
				valCounts.sb().append(entry.getKey());
				valCounts.sb().append(",");
				valCounts.sb().append(entry.getValue());
				className = entry.getKey().getClass().getName();
			}
			valCounts.sb().insert(0, ";");
			valCounts.sb().insert(0, className);
			r.sb().append(valCounts.sb());
		} else {
			r.sb().append(";");
		}
		return r;
	}

	@Override
	public void fromStr(Str str) {
		List<Str> elems = str.split(";");
		if (valueCounts==null) {
			valueCounts = new HashMap<Object,Integer>();
		} else {
			valueCounts.clear();
		}
		index = Integer.parseInt(elems.get(0).toString());
		maxCount = Integer.parseInt(elems.get(1).toString());
		String className = elems.get(2).toString();
		List<Str> valCounts = elems.get(3).split("%");
		for (Str valCount: valCounts) {
			List<Str> vc = valCount.split(",");
			Integer count = Integer.parseInt(vc.get(1).toString());
			Object value = null;
			if (className.equals(String.class.getName())) {
				value = vc.get(0).toString();
			} else if (className.equals(Str.class.getName())) {
				value = vc.get(0);
			} else if (className.equals(Long.class.getName())) {
				value = Long.parseLong(vc.get(0).toString());
			} else if (className.equals(Integer.class.getName())) {
				value = Integer.parseInt(vc.get(0).toString());
			} else if (className.equals(Double.class.getName())) {
				value = Double.parseDouble(vc.get(0).toString());
			} else if (className.equals(Float.class.getName())) {
				value = Float.parseFloat(vc.get(0).toString());
			} else if (className.equals(Boolean.class.getName())) {
				value = Boolean.parseBoolean(vc.get(0).toString());
			}
			if (value!=null) {
				valueCounts.put(value, count);
			}
		}
	}

	protected boolean associate(Object value) {
		boolean r = false;
		if (value!=null) {
			Integer count = valueCounts.get(value);
			if (count==null) {
				count = new Integer(4);
			}
			count++;
			valueCounts.put(value,count);
			if (count>=maxCount) {
				r = true;
			}
		}
		return r;
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
}
