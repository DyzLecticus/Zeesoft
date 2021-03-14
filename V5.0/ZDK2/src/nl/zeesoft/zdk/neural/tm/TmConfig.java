package nl.zeesoft.zdk.neural.tm;

import nl.zeesoft.zdk.neural.model.CellConfig;

public class TmConfig extends CellConfig {
	public boolean	learn			= true;
	
	public TmConfig copy() {
		TmConfig r = new TmConfig();
		r.copyFrom(this);
		r.learn = learn;
		return r;
	}
}
