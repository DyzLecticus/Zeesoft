package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;

public class NetworkConfig {
	protected List<String>				inputNames			= new ArrayList<String>();
	protected List<ProcessorConfig>		processorConfigs	= new ArrayList<ProcessorConfig>();
	
	public StringBuilder test() {
		StringBuilder r = new StringBuilder();
		
		return r;
	}
	
	public void addInput(String name) {
		if (name.length()>0 && !inputNames.contains(name)) {
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

	public void addLink(String fromName, String toName) {
		addLink(fromName, 0, toName, 0);
	}

	public void addLink(String fromName, int fromOutput, String toName, int toInput) {
		ProcessorConfig pcF = getProcessorConfig(fromName);
		ProcessorConfig pcT = getProcessorConfig(toName);
		if ((pcF!=null || inputNames.contains(fromName)) && pcT!=null) {
			pcT.addLink(fromName,fromOutput,toInput);
		}
	}
	
	public int getNumberOfInputs() {
		return inputNames.size();
	}
	
	public int getNumberOfProcessors() {
		return processorConfigs.size();
	}
	
	protected ProcessorConfig getProcessorConfig(String name) {
		ProcessorConfig r = null;
		for (ProcessorConfig pc: processorConfigs) {
			if (pc.name.equals(name)) {
				r = pc;
				break;
			}
		}
		return r;
	}
	
	protected void addProcessorConfig(ProcessorConfig config) {
		if (config.name.length()>0) {
			ProcessorConfig pc = getProcessorConfig(config.name);
			if (pc==null) {
				processorConfigs.add(config);
			}
		}
	}
}
