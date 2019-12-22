package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.json.JsAble;

public abstract class ProcessorConfigObject implements JsAble {
	protected boolean		initialized			= false;

	/**
	 * Returns a copy of this configuration.
	 * 
	 * @return A copy of this configuration
	 */
	public abstract ProcessorConfigObject copy();
}
