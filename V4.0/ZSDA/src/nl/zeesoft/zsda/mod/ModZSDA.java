package nl.zeesoft.zsda.mod;

import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zsda.grid.PersistableProcessorState;
import nl.zeesoft.zsda.grid.ZGridFactoryInitializer;
import nl.zeesoft.zsda.grid.ZGridInitializer;
import nl.zeesoft.zsda.mod.handler.HtmlZSDAGridConfigHandler;
import nl.zeesoft.zsda.mod.handler.HtmlZSDAIndexHandler;
import nl.zeesoft.zsda.mod.handler.JavaScriptZSDAGridConfigHandler;
import nl.zeesoft.zsda.mod.handler.JsonZSDAGridConfigHandler;

public class ModZSDA extends ModObject implements StateListener {
	public static final String			NAME						= "ZSDA";
	public static final String			DESC						= 
		"Zeesoft Streaming Data Analyzer provides a configurable grid of SDR processors to analyze data streams.";

	private ZGridFactoryInitializer		factoryInitializer			= null;
	private ZGridInitializer			gridInitializer				= null;
	
	public ModZSDA(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		factoryInitializer = new ZGridFactoryInitializer(config);
		factoryInitializer.addListener(this);
		gridInitializer = new ZGridInitializer(config,factoryInitializer);
		gridInitializer.addListener(this);
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
		handlers.add(new JavaScriptZSDAGridConfigHandler(configuration,this));
		handlers.add(new JsonZSDAGridConfigHandler(configuration,this));
		super.initialize();
		factoryInitializer.initialize();
	}
	
	@Override
	public void destroy() {
		ZGrid grid = factoryInitializer.getGrid();
		if (grid!=null) {
			grid.stop();
			grid.whileActive();
		}
		
		/*
		SortedMap<String,ZStringBuilder> stateData = grid.getColumnStateData();
		for (Entry<String,ZStringBuilder> entry: stateData.entrySet()) {
			PersistableProcessorState state = gridInitializer.getProcessorState(entry.getKey());
			state.setStateData(entry.getValue());
			gridInitializer.updateProcessorState(entry.getKey());
		}
		*/
		
		grid.destroy();
		
		factoryInitializer.destroy();
		gridInitializer.destroy();
		
		super.destroy();
	}
	
	@Override
	public void stateChanged(Object source, boolean open) {
		if (source==factoryInitializer && open) {
			gridInitializer.initialize();
		} else if (source==gridInitializer && open) {
			//factoryInitializer.getGrid().start();
		}
	}
	
	public ZGridFactoryInitializer getFactoryInitializer() {
		return factoryInitializer;
	}
}
