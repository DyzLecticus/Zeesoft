package nl.zeesoft.zdbd.neural;

import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.processors.ClassifierConfig;
import nl.zeesoft.zdk.neural.processors.Merger;
import nl.zeesoft.zdk.neural.processors.MergerConfig;
import nl.zeesoft.zdk.neural.processors.SpatialPooler;
import nl.zeesoft.zdk.neural.processors.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.processors.TemporalMemory;
import nl.zeesoft.zdk.neural.processors.TemporalMemoryConfig;

public class NetworkConfigFactory {
	private static int		SP_ON_BITS				= 20;
	private static int		TM_SIZE_X				= 32;
	private static int		TM_SIZE_Y				= 32;
	private static int		TM_SIZE_Z				= 16;
	private static boolean	QUICK_LEARN				= true;

	public static int		MERGER_LAYER			= 0;
	public static int		POOLER_LAYER			= 1;
	public static int		MEMORY_LAYER			= 2;
	public static int		CLASSIFIER_LAYER		= 3;
		
	public static String	CONTEXT_INPUT			= "Context";
	public static String	GROUP1_INPUT			= "Group1";
	public static String	GROUP2_INPUT			= "Group2";
	public static String	GROUP3_INPUT			= "Group3";
		
	public static NetworkConfig getNetworkConfig() {
		NetworkConfig r = new NetworkConfig();
		
		r.addInput(CONTEXT_INPUT);
		r.addInput(GROUP1_INPUT);
		r.addInput(GROUP2_INPUT);
		r.addInput(GROUP3_INPUT);
		
		addGroupProcessors(r,1,GROUP1_INPUT);
		addGroupProcessors(r,2,GROUP2_INPUT);
		addGroupProcessors(r,3,GROUP3_INPUT);
		
		for (PatternInstrument inst: InstrumentPattern.INSTRUMENTS) {
			String inputName = "";
			if (inst.group()==1) {
				inputName = GROUP1_INPUT;
			} else if (inst.group()==2) {
				inputName = GROUP2_INPUT;
			} else if (inst.group()==3) {
				inputName = GROUP3_INPUT;
			}
			addClassifier(r, inst.name(), inputName);
		}
		return r;
	}
	
	protected static void addGroupProcessors(NetworkConfig r, int group, String inputName) {
		SDR sdr = new SDR((Rythm.sizeX() * Rythm.sizeY()) + (InstrumentPattern.sizeX(group) * InstrumentPattern.sizeY(group)),1);
		sdr.square();
		
		MergerConfig mergerConfig = r.addMerger(inputName + "Merger",MERGER_LAYER);
		mergerConfig.sizeX = sdr.sizeX();
		mergerConfig.sizeY = sdr.sizeY();
		r.addLink(CONTEXT_INPUT, 0, inputName + "Merger", 0);
		r.addLink(inputName, 0, inputName + "Merger", 1);

		SpatialPoolerConfig poolerConfig = r.addSpatialPooler(inputName + "Pooler", POOLER_LAYER);
		poolerConfig.inputSizeX = mergerConfig.sizeX;
		poolerConfig.inputSizeY = mergerConfig.sizeY;
		poolerConfig.potentialRadius = 0;
		poolerConfig.outputSizeX = TM_SIZE_X;
		poolerConfig.outputSizeY = TM_SIZE_Y;
		poolerConfig.outputOnBits = SP_ON_BITS;
		r.addLink(inputName + "Merger", Merger.MERGED_OUTPUT, inputName + "Pooler", 0);
		
		TemporalMemoryConfig memoryConfig = r.addTemporalMemory(inputName + "Memory", MEMORY_LAYER);
		memoryConfig.sizeX = TM_SIZE_X;
		memoryConfig.sizeY = TM_SIZE_Y;
		memoryConfig.sizeZ = TM_SIZE_Z;
		if (QUICK_LEARN) {
			memoryConfig.initialPermanence = memoryConfig.permanenceThreshold + 0.1F;
		}
		r.addLink(inputName + "Pooler", SpatialPooler.ACTIVE_COLUMNS_OUTPUT, inputName + "Memory", 0);
	}
	
	protected static void addClassifier(NetworkConfig r, String name, String inputName) {
		ClassifierConfig classifierConfig = r.addClassifier(name + "Classifier", CLASSIFIER_LAYER);
		classifierConfig.sizeX = TM_SIZE_X * TM_SIZE_Z;
		classifierConfig.sizeY = TM_SIZE_Y;
		classifierConfig.valueKey = name;
		classifierConfig.logPredictionAccuracy = true;
		classifierConfig.accuracyHistorySize = 256;
		classifierConfig.accuracyTrendSize = 32;
		r.addLink(inputName + "Memory", TemporalMemory.ACTIVE_CELLS_OUTPUT, name + "Classifier", 0);
		r.addLink(inputName, 0, name + "Classifier", 1);
	}
}
