package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Str;

public abstract class SDRProcessorConfig {
	public Str getDescription() {
		return new Str(this.getClass().getSimpleName());
	}
}
