package nl.zeesoft.zsda;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGridFactory;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderDateTime;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zsda.mod.ModZSDA;

public class ZSDAConfig extends Config {
	@Override
	protected void addModules() {
		super.addModules();
		getZODB().selfTest = false;
		getZODB().maxLenObj = 99999999;
		addModule(new ModZSDA(this));
	}

	@Override
	public Database getNewDatabase() {
		Database r = super.getNewDatabase();
		r.getConfiguration().indexConfig.addIndex(ModZSDA.NAME + "/Logs/","dateTime",true,false);
		return r;
	}

	public void initializeFactory(ZGridFactory factory) {
		factory.initializeDefaultGrid();
		if (getModule(ModZSDA.NAME).selfTest) {
			ZGridEncoderDateTime encoder = (ZGridEncoderDateTime) factory.getEncoder(0);
			encoder.setBitsPerEncoder(6);
			encoder.setScale(2);
			encoder.setIncludeMonth(false);
			encoder.setIncludeDayOfWeek(false);
			encoder.setIncludeHourOfDay(false);
			factory.setEncoder(0,encoder);
			ZStringBuilder err = encoder.testScalarOverlap();
			if (err.length()>0) {
				error(this,err.toString());
			}
			PoolerConfig poolerConfig = new PoolerConfig(encoder.length(),1024,21);
			poolerConfig.setProximalRadius(2);
			factory.setProcessor(1,0,poolerConfig);
		}
	}
}
