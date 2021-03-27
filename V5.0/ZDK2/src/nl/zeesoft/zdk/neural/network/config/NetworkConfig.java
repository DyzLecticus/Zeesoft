package nl.zeesoft.zdk.neural.network.config;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.neural.network.config.type.ClassifierConfig;
import nl.zeesoft.zdk.neural.network.config.type.MergerConfig;
import nl.zeesoft.zdk.neural.network.config.type.ScalarEncoderConfig;
import nl.zeesoft.zdk.neural.network.config.type.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.network.config.type.TemporalMemoryConfig;

public class NetworkConfig {
	protected NetworkConfigTester		tester				= new NetworkConfigTester();
	protected List<String>				inputNames			= new ArrayList<String>();
	protected List<ProcessorConfig>		processorConfigs	= new ArrayList<ProcessorConfig>();
	
	public StringBuilder test() {
		return tester.test(this);
	}
	
	public void addInput(String name) {
		if (name.length()>0 && !inputNames.contains(name) && getProcessorConfig(name)==null) {
			inputNames.add(name);
		}
	}
	
	public ScalarEncoderConfig addScalarEncoder(String name) {
		return addScalarEncoder(processorConfigs.size(),name);
	}
	
	public SpatialPoolerConfig addSpatialPooler(String name) {
		return addSpatialPooler(processorConfigs.size(),name);
	}
	
	public TemporalMemoryConfig addTemporalMemory(String name) {
		return addTemporalMemory(processorConfigs.size(),name);
	}
	
	public ClassifierConfig addClassifier(String name) {
		return addClassifier(processorConfigs.size(),name);
	}
	
	public MergerConfig addMerger(String name) {
		return addMerger(processorConfigs.size(),name);
	}
	
	public ScalarEncoderConfig addScalarEncoder(int layer, String name) {
		ScalarEncoderConfig r = new ScalarEncoderConfig(layer,name);
		addProcessorConfig(r);
		return r;
	}
	
	public SpatialPoolerConfig addSpatialPooler(int layer, String name) {
		SpatialPoolerConfig r = new SpatialPoolerConfig(layer,name);
		addProcessorConfig(r);
		return r;
	}
	
	public TemporalMemoryConfig addTemporalMemory(int layer, String name) {
		TemporalMemoryConfig r = new TemporalMemoryConfig(layer,name);
		addProcessorConfig(r);
		return r;
	}
	
	public ClassifierConfig addClassifier(int layer, String name) {
		ClassifierConfig r = new ClassifierConfig(layer,name);
		addProcessorConfig(r);
		return r;
	}
	
	public MergerConfig addMerger(int layer, String name) {
		MergerConfig r = new MergerConfig(layer,name);
		addProcessorConfig(r);
		return r;
	}

	public String addLink(String fromName, String toName) {
		return addLink(fromName, 0, toName, 0);
	}

	public String addLink(String fromName, int fromOutput, String toName, int toInput) {
		String r = "";
		ProcessorConfig pcF = getProcessorConfig(fromName);
		ProcessorConfig pcT = getProcessorConfig(toName);
		if ((pcF!=null || inputNames.contains(fromName)) && pcT!=null) {
			pcT.addLink(fromName,fromOutput,toInput);
		} else {
			if (pcT==null) {
				r = "Link to processor not found: " + fromName;
			} else {
				r = "Link from processor or input not found: ";
			}
		}
		return r;
	}
	
	public List<String> getInputNames() {
		return new ArrayList<String>(inputNames);
	}
	
	public List<ProcessorConfig> getProcessorConfigs() {
		return new ArrayList<ProcessorConfig>(processorConfigs);
	}
	
	public ProcessorConfig getProcessorConfig(String name) {
		ProcessorConfig r = null;
		for (ProcessorConfig pc: processorConfigs) {
			if (pc.name.equals(name)) {
				r = pc;
				break;
			}
		}
		return r;
	}
	
	public List<ProcessorConfig> getProcessorConfigs(String nameContains) {
		List<ProcessorConfig> r = new ArrayList<ProcessorConfig>();
		for (ProcessorConfig pc: processorConfigs) {
			if (pc.name.indexOf(nameContains)>=0) {
				r.add(pc);
			}
		}
		return r;
	}
	
	protected void addProcessorConfig(ProcessorConfig config) {
		if (config.name.length()>0 && !inputNames.contains(config.name)) {
			ProcessorConfig pc = getProcessorConfig(config.name);
			if (pc==null) {
				processorConfigs.add(config);
			}
		}
	}
}
