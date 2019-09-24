package nl.zeesoft.zdk.htm.impl;

import nl.zeesoft.zdk.htm.mdl.Model;

public class TemporalMemory extends Model {
	private TemporalMemoryConfig				memoryConfig					= null;
	
	public TemporalMemory(Model model,TemporalMemoryConfig config) {
		super(model.config);
		model.copyTo(this,false,true);
		this.memoryConfig = config;
		config.initialized = true;
	}
}
