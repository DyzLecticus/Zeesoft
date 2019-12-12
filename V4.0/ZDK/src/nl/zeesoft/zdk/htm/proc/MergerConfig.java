package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

/**
 * A MergerConfig is used to configure a single merger.
 * The configuration cannot be changed once it has been used to instantiate a merger.
 */
public class MergerConfig extends ProcessorConfigObject {
	protected boolean	union			= false;
	protected int		maxOnBits		= 0;

	public MergerConfig() {
		
	}

	public MergerConfig(int maxOnBits) {
		this.maxOnBits = maxOnBits;
	}
	
	/**
	 * Returns a copy of this configuration.
	 * 
	 * @return A copy of this configuration
	 */
	public MergerConfig copy() {
		MergerConfig r = new MergerConfig(maxOnBits);
		r.union = union;
		return r;
	}
	
	/**
	 * Indicates the merger should create a union out of the incoming SDRs (default is concatenate).
	 * 
	 * @param union Indicates the merger should create a union
	 */
	public void setUnion(boolean union) {
		if (!initialized) {
			this.union = union;
		}
	}
	
	/**
	 * Sets the maximum number of on bits that the merged SDR may contain.
	 * Zero will disable sub sampling.
	 * 
	 * @param maxOnBits The maximum number of on bits
	 */
	public void setMaxOnBits(int maxOnBits) {
		if (!initialized) {
			this.maxOnBits = maxOnBits;
		}
	}
	
	/**
	 * Returns a description of this configuration.
	 * 
	 * @return A description
	 */
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Merger maximum number of on bits: ");
		r.append("" + maxOnBits);
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("union","" + union));
		json.rootElement.children.add(new JsElem("maxOnBits","" + maxOnBits));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (!initialized && json.rootElement!=null) {
			union = json.rootElement.getChildBoolean("union",union);
			maxOnBits = json.rootElement.getChildInt("maxOnBits",maxOnBits);
		}
	}
}
