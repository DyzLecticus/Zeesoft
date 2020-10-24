package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Instantiator;

public class ProcessorFactory {
	public static SDRProcessor getNewProcessor(SDRProcessorConfig config, boolean initialize) {
		SDRProcessor r = null;
		String className = config.getClass().getName();
		if (className.endsWith("Config")) {
			className = className.replace("Config","");
			r = (SDRProcessor) Instantiator.getNewClassInstanceForName(className);
			r.initialize();
		}
		return r;
	}
	
	public static SpatialPooler getNewSpatialPooler(SpatialPoolerConfig config) {
		return (SpatialPooler) getNewProcessor(config, true);
	}
	
	public static TemporalMemory getNewTemporalMemory(TemporalMemoryConfig config) {
		return (TemporalMemory) getNewProcessor(config, true);
	}
	
	public static Classifier getNewClassifier(ClassifierConfig config) {
		return (Classifier) getNewProcessor(config, true);
	}
	
	public static ProcessorManager getNewProcessorManager(String name, SDRProcessorConfig config, boolean resetConnections) {
		SDRProcessor processor = getNewProcessor(config, true);
		if (processor instanceof CellGridProcessor && resetConnections) {
			CellGridProcessor cgp = (CellGridProcessor) processor;
			cgp.resetConnections();
		}
		return new ProcessorManager(name,processor);
	}
	
	public static ProcessorManager getNewProcessorManager(String name, SDRProcessorConfig config) {
		return getNewProcessorManager(name, config, false);
	}
}
