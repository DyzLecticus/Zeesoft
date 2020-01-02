package nl.zeesoft.zsda.mod;

import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zsda.grid.GeneratorInitializer;
import nl.zeesoft.zsda.grid.LoggerInitializer;
import nl.zeesoft.zsda.grid.PersistableGenerator;
import nl.zeesoft.zsda.grid.ZGridFactoryInitializer;
import nl.zeesoft.zsda.grid.ZGridInitializer;
import nl.zeesoft.zsda.mod.handler.HtmlZSDAGridConfigHandler;
import nl.zeesoft.zsda.mod.handler.HtmlZSDAGridMonitorHandler;
import nl.zeesoft.zsda.mod.handler.HtmlZSDAIndexHandler;
import nl.zeesoft.zsda.mod.handler.JavaScriptZSDAGridConfigHandler;
import nl.zeesoft.zsda.mod.handler.JavaScriptZSDAGridMonitorHandler;
import nl.zeesoft.zsda.mod.handler.JsonZSDAGridConfigHandler;

public class ModZSDA extends ModObject implements StateListener {
	public static final String			NAME						= "ZSDA";
	public static final String			DESC						= 
		"Zeesoft Streaming Data Analyzer provides a configurable grid of SDR processors to analyze data streams.";

	private ZGridFactoryInitializer		factoryInitializer			= null;
	private ZGridInitializer			gridInitializer				= null;
	private GeneratorInitializer		generatorInitializer		= null;
	private LoggerInitializer			loggerInitializer			= null;
	
	public ModZSDA(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		factoryInitializer = new ZGridFactoryInitializer(config);
		factoryInitializer.addListener(this);
		gridInitializer = new ZGridInitializer(config,factoryInitializer);
		gridInitializer.addListener(this);
		generatorInitializer = new GeneratorInitializer(config,factoryInitializer);
		generatorInitializer.addListener(this);
		loggerInitializer = new LoggerInitializer(config,factoryInitializer);
		loggerInitializer.addListener(this);
	}
	
	@Override
	public void install() {
		factoryInitializer.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZSDAIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		handlers.add(new HtmlZSDAGridConfigHandler(configuration,this));
		handlers.add(new HtmlZSDAGridMonitorHandler(configuration,this));
		handlers.add(new JavaScriptZSDAGridConfigHandler(configuration,this));
		handlers.add(new JavaScriptZSDAGridMonitorHandler(configuration,this));
		handlers.add(new JsonZSDAGridConfigHandler(configuration,this));
		super.initialize();
		factoryInitializer.initialize();
	}
	
	@Override
	public void destroy() {
		PersistableGenerator generator = generatorInitializer.getGenerator();
		if (generator!=null) {
			if (generator.isActive()) {
				generator.stop();
				generator.whileActive();
			}
			generatorInitializer.updatedGenerator();
		}
		
		ZGrid grid = factoryInitializer.getGrid();
		if (grid!=null) {
			if (grid.isActive()) {
				grid.stop();
				grid.whileActive();
			}
			configuration.debug(this,"Saving state ...");
			gridInitializer.updateState();
			grid.destroy();
		}
		
		if (grid!=null) {
			gridInitializer.whileBusy(60);
			configuration.debug(this,"Saved state");
		}
		loggerInitializer.whileBusy(60);
		generatorInitializer.whileBusy(60);
				
		factoryInitializer.destroy();
		gridInitializer.destroy();
		generatorInitializer.destroy();
		loggerInitializer.destroy();
		
		super.destroy();
	}
	
	@Override
	public void stateChanged(Object source, boolean open) {
		if (open) {
			if (source==factoryInitializer) {
				loggerInitializer.initialize();
			} else if (source==loggerInitializer) {
				configuration.debug(this,"Loading state ...");
				gridInitializer.initialize();
			} else if (source==gridInitializer) {
				configuration.debug(this,"Loaded state");
				factoryInitializer.getGrid().start();
				if (selfTest) {
					generatorInitializer.initialize();
				}
			} else if (source==generatorInitializer) {
				generatorInitializer.getGenerator().start();
			}
		}
	}
	
	public ZGridFactoryInitializer getFactoryInitializer() {
		return factoryInitializer;
	}
	
	public ZGridInitializer getGridInitializer() {
		return gridInitializer;
	}
	
	public LoggerInitializer getLoggerInitializer() {
		return loggerInitializer;
	}
	
	public GeneratorInitializer getGeneratorInitializer() {
		return generatorInitializer;
	}
}
