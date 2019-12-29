package nl.zeesoft.zsda;

import nl.zeesoft.zdk.htm.grid.ZGridFactory;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderDateTime;
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
		if (getModule(ModZSDA.NAME).selfTest) {
			ZGridEncoderDateTime encoder = (ZGridEncoderDateTime) factory.getEncoder(0);
			encoder.setIncludeMonth(false);
			encoder.setIncludeDayOfWeek(false);
			encoder.setIncludeHourOfDay(false);
			factory.setEncoder(0,encoder);
		}
	}
}
