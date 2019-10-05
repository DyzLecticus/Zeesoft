package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.util.DateTimeSDR;

public class ClassifierConfig {
	protected boolean			initialized		= false;
	
	protected List<Integer>		predictSteps	= new ArrayList<Integer>();
	protected String			valueKey		= DateTimeSDR.VALUE_KEY;
	protected String			labelKey		= DateTimeSDR.LABEL_KEY;
	protected int				maxCount		= 40;
	
	public ClassifierConfig() {
		predictSteps.add(0);
	}
	
	public ClassifierConfig(int steps) {
		if (steps < 0) {
			steps = 0;
		}
		predictSteps.add(steps);
	}
	
	public void addPredictSteps(int steps) {
		if (!initialized) {
			predictSteps.add(steps);
		}
	}
	
	public void setValueKey(String valueKey) {
		if (!initialized) {
			this.valueKey = valueKey;
		}
	}
	
	public void setLabelKey(String labelKey) {
		if (!initialized) {
			this.labelKey = labelKey;
		}
	}
	
	public void setMaxCount(int maxCount) {
		if (!initialized) {
			this.maxCount = maxCount;
		}
	}
}
