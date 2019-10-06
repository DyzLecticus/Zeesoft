package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.util.DateTimeSDR;

/**
 * A ClassifierConfig is used to configure a single classifier.
 * The configuration cannot be changed once it has been used to instantiate a temporal memory.
 * A classifier can create classifications (or prediction) for multiple prediction steps.
 */
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
	
	/**
	 * Adds a number of prediction steps to the classifier.
	 * 
	 * When specifying 0 prediction steps, a classification for the current input is produced.
	 * When specifying 24 prediction steps, a prediction of 24 steps in the future is produced.
	 * 
	 * @param steps The number of prediction steps
	 */
	public void addPredictSteps(int steps) {
		if (!initialized) {
			predictSteps.add(steps);
		}
	}
	
	/**
	 * Specifies the input DateTimeSDR value key to classify
	 * 
	 * @param valueKey The value key
	 */
	public void setValueKey(String valueKey) {
		if (!initialized) {
			this.valueKey = valueKey;
		}
	}
	
	/**
	 * Specifies the input DateTimeSDR label key to classify
	 * 
	 * @param labelKey The label key
	 */
	public void setLabelKey(String labelKey) {
		if (!initialized) {
			this.labelKey = labelKey;
		}
	}
	
	/**
	 * Sets the maximum count for the classifier.
	 * When a bit value count hits this maximum, all value counts of the current bits will be divided by 2.
	 * If a bit value only has 1 count it will be removed.
	 * This allows the classifier to forget old classifications.
	 * 
	 * @param maxCount The maximum count
	 */
	public void setMaxCount(int maxCount) {
		if (!initialized) {
			this.maxCount = maxCount;
		}
	}
}
