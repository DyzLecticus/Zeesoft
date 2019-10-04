package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;

public class ClassifierConfig {
	protected boolean		initialized							= false;
	
	protected int			steps								= 1;
	protected String		valueKey							= DateTimeSDR.VALUE_KEY;
	protected String		labelKey							= DateTimeSDR.LABEL_KEY;
	
	public void setSteps(int steps) {
		if (!initialized) {
			this.steps = steps;
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
}
