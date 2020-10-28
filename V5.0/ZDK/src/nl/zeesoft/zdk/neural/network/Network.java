package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.processors.Processor;
import nl.zeesoft.zdk.neural.processors.ProcessorFactory;
import nl.zeesoft.zdk.thread.Lock;

public class Network {
	private Lock				lock			= new Lock();
	
	private NetworkConfig		config			= new NetworkConfig();
	
	private List<Processor>		processors		= new ArrayList<Processor>();
	private NetworkIO			previousIO		= null;
	
	// TODO: Locking
	// TODO: Reset state
	// TODO: Multi thread initialize
	// TODO: Multi threaded save and load
	
	public Str configure(NetworkConfig config) {
		Str err = config.testConfiguration();
		if (err.length()>0) {
			Logger.err(this, err);
		} else {
			lock.lock(this);
			this.config.copyFrom(config);
			processors.clear();
			lock.unlock(this);
		}
		return err;
	}
	
	public Str initialize(boolean resetConnections) {
		Str err = config.testConfiguration();
		if (err.length()>0) {
			Logger.err(this, err);
		} else {
			lock.lock(this);
			for (NetworkProcessorConfig cfg: config.processorConfigs) {
				processors.add(
					ProcessorFactory.getNewProcessor(
						cfg.name, cfg.processorConfig, cfg.threads, resetConnections
					)
				);
			}
			lock.unlock(this);
		}
		return err;
	}
	
	public Str processIO(NetworkIO networkIO) {
		Str err = new Str();
		List<String> valueNames = networkIO.getValueNames();
		for (String name: config.inputNames) {
			if (!valueNames.contains(name)) {
				if (err.length()>0) {
					err.sb().append("\n");
				}
				err.sb().append("Missing value for input name: ");
				err.sb().append(name);
			}
		}
		if (err.length()==0) {
			SortedMap<Integer,List<String>> namesByLayer = getLayerProcessors();
			for (Entry<Integer,List<String>> entry: namesByLayer.entrySet()) {
				// TODO: Process IO
				
			}
		}
		return err;
	}

	protected SortedMap<Integer,List<String>> getLayerProcessors() {
		SortedMap<Integer,List<String>> r = new TreeMap<Integer,List<String>>();
		for (NetworkProcessorConfig cfg: config.processorConfigs) {
			List<String> names = r.get(cfg.layer);
			if (names==null) {
				names = new ArrayList<String>();
				r.put(cfg.layer, names);
			}
			names.add(cfg.name);
		}
		return r;
	}
}
