package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.processors.ClassifierConfig;
import nl.zeesoft.zdk.neural.processors.EncoderConfig;
import nl.zeesoft.zdk.neural.processors.MergerConfig;
import nl.zeesoft.zdk.neural.processors.SDRProcessorConfig;
import nl.zeesoft.zdk.neural.processors.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.processors.TemporalMemoryConfig;

public class NetworkConfig {
	public List<String>						inputNames			= new ArrayList<String>();
	public List<NetworkProcessorConfig>		processorConfigs	= new ArrayList<NetworkProcessorConfig>();
	public List<NetworkLink>				links				= new ArrayList<NetworkLink>();

	public void addInput(String name) {
		inputNames.add(name);
	}
	
	public EncoderConfig addEncoder(String name) {
		return addEncoder(name, processorConfigs.size());
	}

	public EncoderConfig addEncoder(String name, int layer) {
		EncoderConfig config = new EncoderConfig();
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
		for (NetworkLink link: links) {
			checkNameExists(link.fromName, r);
			checkNameExists(link.toName, r);
			
			SDRProcessorConfig fromConfig = getSDRProcessorConfig(link.fromName);
			SDRProcessorConfig toConfig = getSDRProcessorConfig(link.fromName);
						
			int fromSizeX = 0;
			int fromSizeY = 0;
			int toSizeX = 0;
			int toSizeY = 0;

			if (link.fromIndex == 0 && fromConfig instanceof EncoderConfig) {
				EncoderConfig cfg = (EncoderConfig) fromConfig;
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
				toSizeX = cfg.outputSizeX;
				toSizeY = cfg.outputSizeY;
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
				toSizeX = cfg.sizeX;
				toSizeY = cfg.sizeY;
			}

			if (fromSizeX>0 && fromSizeY>0 && toSizeX>0 && toSizeY>0 && 
				(fromSizeX!=toSizeX || fromSizeY!=toSizeY)
				) {
				if (r.sb().length()>0) {
					r.sb().append("\n");
				}
				r.sb().append("Spatial pooler outnput dimensions do not match temporal memory input dimensions: ");
				r.sb().append(link.fromName);
				r.sb().append(" > ");
				r.sb().append(link.toName);
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
}
