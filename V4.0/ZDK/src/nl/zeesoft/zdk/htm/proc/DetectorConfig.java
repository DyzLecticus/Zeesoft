package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

/**
 * A DetectorConfig is used to configure a single detector.
 * The configuration cannot be changed once it has been used to instantiate a detector.
 */
public class DetectorConfig extends ProcessorConfigObject {
	protected int		start		= 2500;
	protected int		window		= 500;
	protected float		threshold	= 0.05F;

	public DetectorConfig() {
		
	}

	public DetectorConfig(int start,int window,float threshold) {
		this.start = start;
		this.window = window;
		this.threshold = threshold;
	}
	
	/**
	 * Returns a copy of this configuration.
	 * 
	 * @return A copy of this configuration
	 */
	public DetectorConfig copy() {
		DetectorConfig r = new DetectorConfig(start,window,threshold);
		return r;
	}
	
	/**
	 * Sets the minimum number of input SDRs required to start detection.
	 * 
	 * @param start The minimum number of input SDRs required to start detection
	 */
	public void setStart(int start) {
		if (!initialized) {
			this.start = start;
		}
	}
	
	/**
	 * Sets historical accuracy window size.
	 * 
	 * @param window The historical accuracy window size
	 */
	public void setWindow(int window) {
		if (!initialized) {
			this.window = window;
		}
	}
	
	/**
	 * Sets the accuracy difference threshold to trigger anomaly recording.
	 * 
	 * @param threshold The accuracy difference threshold to trigger anomaly recording
	 */
	public void setThreshold(float threshold) {
		if (!initialized) {
			this.threshold = threshold;
		}
	}
	
	/**
	 * Returns a description of this configuration.
	 * 
	 * @return A description
	 */
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Detector start: ");
		r.append("" + start);
		r.append(", window: ");
		r.append("" + window);
		r.append(", threshold: ");
		r.append("" + threshold);
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("start","" + start));
		json.rootElement.children.add(new JsElem("window","" + window));
		json.rootElement.children.add(new JsElem("threshold","" + threshold));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (!initialized && json.rootElement!=null) {
			start = json.rootElement.getChildInt("start",start);
			window = json.rootElement.getChildInt("window",window);
			threshold = json.rootElement.getChildFloat("threshold",threshold);
		}
	}
}
