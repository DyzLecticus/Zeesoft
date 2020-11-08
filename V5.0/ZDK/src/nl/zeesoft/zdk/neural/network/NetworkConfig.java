package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.processors.ClassifierConfig;
import nl.zeesoft.zdk.neural.processors.MergerConfig;
import nl.zeesoft.zdk.neural.processors.SDRProcessorConfig;
import nl.zeesoft.zdk.neural.processors.ScalarEncoderConfig;
import nl.zeesoft.zdk.neural.processors.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.processors.TemporalMemoryConfig;

public class NetworkConfig {
	public int								initializeTimeoutMs	= 3000;
	public int								resetStateTimeoutMs	= 1000;
	public int								saveTimeoutMs		= 30000;
	public int								loadTimeoutMs		= 30000;
	
	public List<String>						inputNames			= new ArrayList<String>();
	public List<NetworkProcessorConfig>		processorConfigs	= new ArrayList<NetworkProcessorConfig>();
	public List<NetworkLink>				links				= new ArrayList<NetworkLink>();

	public NetworkConfig() {
		
	}

	public NetworkConfig(NetworkConfig config) {
		copyFrom(config);
	}
	
	public void copyFrom(NetworkConfig config) {
		inputNames.clear();
		processorConfigs.clear();
		links.clear();
		
		this.initializeTimeoutMs = config.initializeTimeoutMs;
		this.resetStateTimeoutMs = config.resetStateTimeoutMs;
		this.saveTimeoutMs = config.saveTimeoutMs;
		this.loadTimeoutMs = config.loadTimeoutMs;
		
		inputNames.addAll(config.inputNames);
		for (NetworkProcessorConfig cfg: config.processorConfigs) {
			processorConfigs.add(new NetworkProcessorConfig(cfg));
		}
		for (NetworkLink link: config.links) {
			links.add(new NetworkLink(link));
		}
	}
	
	public Str getDescription() {
		Str r = new Str();
		r.sb().append("Inputs: ");
		for (String name: inputNames) {
			r.sb().append("\n");
			r.sb().append("<- ");
			r.sb().append(name);
		}
		SortedMap<Integer,List<NetworkProcessorConfig>> configs = getLayerProcessorConfigs();
		for (Entry<Integer,List<NetworkProcessorConfig>> entry: configs.entrySet()) {
			if (r.length()>0) {
				r.sb().append("\n");
			}
			r.sb().append("Layer: ");
			r.sb().append(entry.getKey());
			for (NetworkProcessorConfig cfg: entry.getValue()) {
				Str cfgDesc = cfg.processorConfig.getDescription();
				cfgDesc.replace("\n", "\n  ");
				cfgDesc.sb().insert(0, ": ");
				cfgDesc.sb().insert(0, cfg.name);
				cfgDesc.sb().insert(0, "  ");
				
				// Merge inputs
				List<Str> split = cfgDesc.split("\n");
				int i = 1;
				List<NetworkLink> links = this.getNetworkLinksTo(cfg.name);
				for (NetworkLink link: links) {
					Str linkStr = new Str("  <- ");
					linkStr.sb().append(link.toIndex);
					linkStr.sb().append(" = ");
					linkStr.sb().append(link.fromName);
					if (!inputNames.contains(link.fromName)) {
						linkStr.sb().append("/");
						linkStr.sb().append(link.fromIndex);
					}
					split.add(i, linkStr);
					i++;
				}
				cfgDesc.merge(split,"\n");
				
				r.sb().append("\n");
				r.sb().append(cfgDesc);
			}
		}
		return r;
	}
	
	public void addInput(String name) {
		if (!inputNames.contains(name)) {
			inputNames.add(name);
		}
	}
	
	public ScalarEncoderConfig addScalarEncoder(String name) {
		return addScalarEncoder(name, processorConfigs.size());
	}

	public ScalarEncoderConfig addScalarEncoder(String name, int layer) {
		ScalarEncoderConfig config = new ScalarEncoderConfig();
		addProcessorConfig(name, config, layer);
		return config;
	}
	
	public SpatialPoolerConfig addSpatialPooler(String name) {
		return addSpatialPooler(name, processorConfigs.size());
	}

	public SpatialPoolerConfig addSpatialPooler(String name, int layer) {
		SpatialPoolerConfig config = new SpatialPoolerConfig();
		addProcessorConfig(name, config, layer);
		return config;
	}
	
	public TemporalMemoryConfig addTemporalMemory(String name) {
		return addTemporalMemory(name, processorConfigs.size());
	}

	public TemporalMemoryConfig addTemporalMemory(String name, int layer) {
		TemporalMemoryConfig config = new TemporalMemoryConfig();
		addProcessorConfig(name, config, layer);
		return config;
	}
	
	public ClassifierConfig addClassifier(String name) {
		return addClassifier(name, processorConfigs.size());
	}

	public ClassifierConfig addClassifier(String name, int layer) {
		ClassifierConfig config = new ClassifierConfig();
		addProcessorConfig(name, config, layer);
		return config;
	}

	public MergerConfig addMerger(String name) {
		return addMerger(name, processorConfigs.size());
	}

	public MergerConfig addMerger(String name, int layer) {
		MergerConfig config = new MergerConfig();
		addProcessorConfig(name, config, layer);
		return config;
	}
	
	public void addProcessorConfig(String name, SDRProcessorConfig config, int layer) {
		processorConfigs.add(new NetworkProcessorConfig(layer, name, config));
	}
	
	public void addLink(String fromName, int fromIndex, String toName, int toIndex) {
		links.add(new NetworkLink(fromName, fromIndex, toName, toIndex));
	}
	
	public Str testConfiguration() {
		Str r = new Str();
		int i = 0;
		for (String name: inputNames) {
			if (inputNames.lastIndexOf(name)>i) {
				if (r.sb().length()>0) {
					r.sb().append("\n");
				}
				r.sb().append("Input name must be unique: ");
				r.sb().append(name);
			}
			i++;
		}
		for (NetworkProcessorConfig config: processorConfigs) {
			if (inputNames.contains(config.name)) {
				if (r.sb().length()>0) {
					r.sb().append("\n");
				}
				r.sb().append("Processor name may not match an input name: " + config.name);
			}
			for (NetworkProcessorConfig compare: processorConfigs) {
				if (compare!=config && compare.name.equals(config.name)) {
					if (r.sb().length()>0) {
						r.sb().append("\n");
					}
					r.sb().append("Processor name must be unique: ");
					r.sb().append(config.name);
				}
			}
		}
		for (NetworkLink link: links) {
			for (NetworkLink compare: links) {
				if (compare!=link && compare.toName.equals(link.toName) && compare.toIndex==link.toIndex) {
					if (r.sb().length()>0) {
						r.sb().append("\n");
					}
					r.sb().append("Link target must be unique: ");
					r.sb().append(link.toName);
					r.sb().append("/");
					r.sb().append(link.toIndex);
				}
			}
		}
		for (NetworkLink link: links) {
			checkNameExists(link.fromName, r);
			checkNameExists(link.toName, r);
			
			SDRProcessorConfig fromConfig = getSDRProcessorConfig(link.fromName);
			SDRProcessorConfig toConfig = getSDRProcessorConfig(link.fromName);
						
			int fromSizeX = 0;
			int fromSizeY = 0;
			int toSizeX = 0;
			int toSizeY = 0;

			if (link.fromIndex == 0 && fromConfig instanceof ScalarEncoderConfig) {
				ScalarEncoderConfig cfg = (ScalarEncoderConfig) fromConfig;
				fromSizeX = cfg.sizeX;
				fromSizeY = cfg.sizeY;
			}

			if (link.fromIndex == 0 && fromConfig instanceof SpatialPoolerConfig) {
				SpatialPoolerConfig cfg = (SpatialPoolerConfig) fromConfig;
				fromSizeX = cfg.outputSizeX;
				fromSizeY = cfg.outputSizeY;
			}
			if (link.toIndex == 0 && toConfig instanceof SpatialPoolerConfig) {
				SpatialPoolerConfig cfg = (SpatialPoolerConfig) toConfig;
				toSizeX = cfg.inputSizeX;
				toSizeY = cfg.inputSizeY;
			}
			
			if (link.fromIndex == 0 && fromConfig instanceof TemporalMemoryConfig) {
				TemporalMemoryConfig cfg = (TemporalMemoryConfig) fromConfig;
				fromSizeX = cfg.sizeX * cfg.sizeZ;
				fromSizeY = cfg.sizeY;
			}
			if (link.toIndex == 0 && toConfig instanceof TemporalMemoryConfig) {
				TemporalMemoryConfig cfg = (TemporalMemoryConfig) toConfig;
				toSizeX = cfg.sizeX;
				toSizeY = cfg.sizeY;
			}
			
			if (toConfig instanceof ClassifierConfig) {
				ClassifierConfig cfg = (ClassifierConfig) toConfig;
				toSizeX = cfg.sizeX;
				toSizeY = cfg.sizeY;
			}
			
			if (link.fromIndex == 0 && fromConfig instanceof MergerConfig) {
				MergerConfig cfg = (MergerConfig) fromConfig;
				fromSizeX = cfg.sizeX;
				fromSizeY = cfg.sizeY;
			}

			if (fromSizeX>0 && fromSizeY>0 && toSizeX>0 && toSizeY>0 && 
				(fromSizeX!=toSizeX || fromSizeY!=toSizeY)
				) {
				if (r.sb().length()>0) {
					r.sb().append("\n");
				}
				r.sb().append("Processor output dimensions do not match required input dimensions: ");
				r.sb().append(link.fromName);
				r.sb().append("/");
				r.sb().append(link.fromIndex);
				r.sb().append(" -> ");
				r.sb().append(link.toName);
				r.sb().append("/");
				r.sb().append(link.toIndex);
			}
		}
		return r;
	}
	
	public NetworkProcessorConfig getNetworkProcessorConfig(String name) {
		NetworkProcessorConfig r = null;
		for (NetworkProcessorConfig config: processorConfigs) {
			if (config.name.equals(name)) {
				r = config;
				break;
			}
		}
		return r;
	}

	public SDRProcessorConfig getSDRProcessorConfig(String name) {
		SDRProcessorConfig r = null;
		NetworkProcessorConfig config = getNetworkProcessorConfig(name);
		if (config!=null && config.processorConfig==null) {
			r = config.processorConfig;
		}
		return r;
	}

	public SpatialPoolerConfig getSpatialPoolerConfig(String name) {
		SpatialPoolerConfig r = null;
		SDRProcessorConfig config = getSDRProcessorConfig(name);
		if (config!=null && config instanceof SpatialPoolerConfig) {
			r = (SpatialPoolerConfig) config;
		}
		return r;
	}

	public TemporalMemoryConfig getTemporalMemoryConfig(String name) {
		TemporalMemoryConfig r = null;
		SDRProcessorConfig config = getSDRProcessorConfig(name);
		if (config!=null && config instanceof TemporalMemoryConfig) {
			r = (TemporalMemoryConfig) config;
		}
		return r;
	}

	public ClassifierConfig getClassifierConfig(String name) {
		ClassifierConfig r = null;
		SDRProcessorConfig config = getSDRProcessorConfig(name);
		if (config!=null && config instanceof ClassifierConfig) {
			r = (ClassifierConfig) config;
		}
		return r;
	}

	public MergerConfig getMergerConfig(String name) {
		MergerConfig r = null;
		SDRProcessorConfig config = getSDRProcessorConfig(name);
		if (config!=null && config instanceof MergerConfig) {
			r = (MergerConfig) config;
		}
		return r;
	}

	public List<NetworkLink> getNetworkLinksFrom(String fromName) {
		List<NetworkLink> r = new ArrayList<NetworkLink>();
		for (NetworkLink link: links) {
			if (link.fromName.equals(fromName)) {
				r.add(link);
			}
		}
		return r;
	}

	public List<NetworkLink> getNetworkLinksTo(String toName) {
		List<NetworkLink> r = new ArrayList<NetworkLink>();
		for (NetworkLink link: links) {
			if (link.toName.equals(toName)) {
				r.add(link);
			}
		}
		return r;
	}
	
	private void checkNameExists(String name, Str err) {
		if (!nameExists(name)) {
			if (err.sb().length()>0) {
				err.sb().append("\n");
			}
			err.sb().append("Input or processor not found with name: ");
			err.sb().append(name);
		}
	}
	
	private boolean nameExists(String name) {
		boolean r = false;
		if (inputNames.contains(name) || getNetworkProcessorConfig(name)!=null) {
			r = true;
		}
		return r;
	}
	
	public SortedMap<Integer,List<NetworkProcessorConfig>> getLayerProcessorConfigs() {
		SortedMap<Integer,List<NetworkProcessorConfig>> r = new TreeMap<Integer,List<NetworkProcessorConfig>>();
		for (NetworkProcessorConfig cfg: processorConfigs) {
			List<NetworkProcessorConfig> configs = r.get(cfg.layer);
			if (configs==null) {
				configs = new ArrayList<NetworkProcessorConfig>();
				r.put(cfg.layer, configs);
			}
			configs.add(cfg);
		}
		return r;
	}
}
