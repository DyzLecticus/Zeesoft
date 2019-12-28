package nl.zeesoft.zsda;

import nl.zeesoft.zdk.htm.grid.ZGridFactory;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderValue;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsda.mod.ModZSDA;

public class ZSDAConfig extends Config {
	@Override
	protected void addModules() {
		super.addModules();
		getZODB().selfTest = false;
		getZODB().maxLenObj = 99999999;
		addModule(new ModZSDA(this));
	}
	
	public void initializeFactory(ZGridFactory factory) {
		factory.initializeDefaultGrid();
		ZGridEncoderValue enc = (ZGridEncoderValue) factory.getEncoder(1);
		enc.setMaxValue(50);
	}
}
