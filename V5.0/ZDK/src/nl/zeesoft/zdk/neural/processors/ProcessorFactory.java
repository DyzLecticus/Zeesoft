package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Instantiator;

public class ProcessorFactory {
	public static final int THREADS	= 4; 
	
	public static SDRProcessor getNewSDRProcessor(SDRProcessorConfig config, boolean initialize) {
		SDRProcessor r = null;
		String className = config.getClass().getName();
		if (className.endsWith("Config")) {
			className = className.replace("Config","");
			Object obj = Instantiator.getNewClassInstanceForName(className);
			if (obj!=null && obj instanceof SDRProcessor) {
				r = (SDRProcessor) Instantiator.getNewClassInstanceForName(className);
				if (initialize) {
					r.initialize();
				}
			}
		}
		return r;
	}
	
	public static Encoder getNewEncoder(EncoderConfig config) {
		return (Encoder) getNewSDRProcessor(config, true);
	}
	
	public static SpatialPooler getNewSpatialPooler(SpatialPoolerConfig config) {
		return (SpatialPooler) getNewSDRProcessor(config, true);
	}
	
	public static TemporalMemory getNewTemporalMemory(TemporalMemoryConfig config) {
		return (TemporalMemory) getNewSDRProcessor(config, true);
	}
	
	public static Merger getNewMerger(MergerConfig config) {
		return (Merger) getNewSDRProcessor(config, true);
	}
	
	public static Classifier getNewClassifier(ClassifierConfig config) {
		return (Classifier) getNewSDRProcessor(config, true);
	}
	
	public static Processor getNewProcessor(String name, SDRProcessorConfig config) {
		return getNewProcessor(name, config, THREADS);
	}
	
	public static Processor getNewProcessor(String name, SDRProcessorConfig config, int threads) {
		return getNewProcessor(name, config, threads, false);
	}
	
	public static Processor getNewProcessor(String name, SDRProcessorConfig config, boolean resetConnections) {
		return getNewProcessor(name, config, THREADS, resetConnections);
	}
	
	public static Processor getNewProcessor(String name, SDRProcessorConfig config, int threads, boolean resetConnections) {
		SDRProcessor processor = getNewSDRProcessor(config, true);
		if (processor instanceof CellGridProcessor && resetConnections) {
			CellGridProcessor cgp = (CellGridProcessor) processor;
			cgp.resetConnections();
		}
		return new Processor(name,processor,threads);
	}
}
