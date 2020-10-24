package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Instantiator;

public class ProcessorFactory {
	public static SDRProcessor getNewSDRProcessor(SDRProcessorConfig config, boolean initialize) {
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
		return (SpatialPooler) getNewSDRProcessor(config, true);
	}
	
	public static TemporalMemory getNewTemporalMemory(TemporalMemoryConfig config) {
		return (TemporalMemory) getNewSDRProcessor(config, true);
	}
	
	public static Classifier getNewClassifier(ClassifierConfig config) {
		return (Classifier) getNewSDRProcessor(config, true);
	}
	
	public static Processor getNewProcessor(String name, SDRProcessorConfig config, boolean resetConnections) {
		SDRProcessor processor = getNewSDRProcessor(config, true);
		if (processor instanceof CellGridProcessor && resetConnections) {
			CellGridProcessor cgp = (CellGridProcessor) processor;
			cgp.resetConnections();
		}
		return new Processor(name,processor);
	}
	
	public static Processor getNewProcessor(String name, SDRProcessorConfig config) {
		return getNewProcessor(name, config, false);
	}
}
