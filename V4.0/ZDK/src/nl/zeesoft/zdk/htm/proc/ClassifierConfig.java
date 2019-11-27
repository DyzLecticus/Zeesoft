package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

/**
 * A ClassifierConfig is used to configure a single classifier.
 * The configuration cannot be changed once it has been used to instantiate a temporal memory.
 * A classifier can create classifications (or prediction) for multiple prediction steps.
 */
public class ClassifierConfig implements JsAble {
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
	
	/**
	 * Returns a description of this configuration.
	 * 
	 * @return A description
	 */
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Classifier value key: ");
		r.append("" + valueKey);
		r.append(", predict steps: ");
		ZStringBuilder pSteps = new ZStringBuilder();
		for (Integer steps: predictSteps) {
			if (pSteps.length()>0) {
				pSteps.append(", ");
			}
			pSteps.append("" + steps);
		}
		r.append(pSteps);
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		ZStringBuilder pSteps = new ZStringBuilder();
		for (Integer steps: predictSteps) {
			if (pSteps.length()>0) {
				pSteps.append(",");
			}
			pSteps.append("" + steps);
		}
		json.rootElement.children.add(new JsElem("predictSteps",pSteps,true));
		json.rootElement.children.add(new JsElem("valueKey",valueKey,true));
		json.rootElement.children.add(new JsElem("labelKey",labelKey,true));
		json.rootElement.children.add(new JsElem("maxCount","" + maxCount));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			ZStringBuilder pSteps = json.rootElement.getChildZStringBuilder("predictSteps");
			predictSteps.clear();
			if (pSteps.length()>0) {
				List<ZStringBuilder> pElems = pSteps.split(",");
				for (ZStringBuilder pStep: pElems) {
					predictSteps.add(Integer.parseInt(pStep.toString()));
				}
			}
			valueKey = json.rootElement.getChildString("valueKey",valueKey);
			labelKey = json.rootElement.getChildString("labelKey",labelKey);
			maxCount = json.rootElement.getChildInt("maxCount",maxCount);
		}
	}
}
