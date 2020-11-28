package nl.zeesoft.zdbd.neural;

import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdbd.pattern.inst.PatternInstrument;
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
	private static int		SP_ON_BITS				= 32;
	private static int		TM_SIZE_X				= 40;
	private static int		TM_SIZE_Y				= 40;
	private static int		TM_SIZE_Z				= 32;
	private static boolean	QUICK_LEARN				= true;

	public static int		MERGER_LAYER			= 0;
	public static int		POOLER_LAYER			= 1;
	public static int		MEMORY_LAYER			= 2;
	public static int		CLASSIFIER_LAYER		= 3;
		
	public static String	RYTHM_INPUT				= "Rythm";
	public static String	GROUP1_INPUT			= "Group1";
	public static String	GROUP2_INPUT			= "Group2";
		
	public static NetworkConfig getNetworkConfig() {
		NetworkConfig r = new NetworkConfig();
		
		r.addInput(RYTHM_INPUT);
		r.addInput(GROUP1_INPUT);
		r.addInput(GROUP2_INPUT);
		
		SDR sdr = new SDR((Rythm.sizeX() * Rythm.sizeY()) + (InstrumentPattern.sizeX(1) * InstrumentPattern.sizeY(1)),1);
		sdr.square();
		
		MergerConfig group1MergerConfig = r.addMerger(GROUP1_INPUT + "Merger",MERGER_LAYER);
		group1MergerConfig.sizeX = sdr.sizeX();
		group1MergerConfig.sizeY = sdr.sizeY();
		r.addLink(RYTHM_INPUT, 0, GROUP1_INPUT + "Merger", 0);
		r.addLink(GROUP1_INPUT, 0, GROUP1_INPUT + "Merger", 1);
		
		sdr = new SDR((Rythm.sizeX() * Rythm.sizeY()) + (InstrumentPattern.sizeX(2) * InstrumentPattern.sizeY(2)),1);
		sdr.square();
		
		MergerConfig group2MergerConfig = r.addMerger(GROUP2_INPUT + "Merger",MERGER_LAYER);
		group2MergerConfig.sizeX = sdr.sizeX();
		group2MergerConfig.sizeY = sdr.sizeY();
		r.addLink(RYTHM_INPUT, 0, GROUP2_INPUT + "Merger", 0);
		r.addLink(GROUP2_INPUT, 0, GROUP2_INPUT + "Merger", 1);

		SpatialPoolerConfig group1PoolerConfig = r.addSpatialPooler(GROUP1_INPUT + "Pooler", POOLER_LAYER);
		group1PoolerConfig.inputSizeX = group1MergerConfig.sizeX;
		group1PoolerConfig.inputSizeY = group1MergerConfig.sizeY;
		group1PoolerConfig.potentialRadius = 0;
		group1PoolerConfig.outputSizeX = TM_SIZE_X;
		group1PoolerConfig.outputSizeY = TM_SIZE_Y;
		group1PoolerConfig.outputOnBits = SP_ON_BITS;
		r.addLink(GROUP1_INPUT + "Merger", Merger.MERGED_OUTPUT, GROUP1_INPUT + "Pooler", 0);
		

		SpatialPoolerConfig group2PoolerConfig = r.addSpatialPooler(GROUP2_INPUT + "Pooler", POOLER_LAYER);
		group2PoolerConfig.inputSizeX = group2MergerConfig.sizeX;
		group2PoolerConfig.inputSizeY = group2MergerConfig.sizeY;
		group2PoolerConfig.potentialRadius = 0;
		group2PoolerConfig.outputSizeX = TM_SIZE_X;
		group2PoolerConfig.outputSizeY = TM_SIZE_Y;
		group2PoolerConfig.outputOnBits = SP_ON_BITS;
		r.addLink(GROUP2_INPUT + "Merger", Merger.MERGED_OUTPUT, GROUP2_INPUT + "Pooler", 0);
		
		TemporalMemoryConfig group1MemoryConfig = r.addTemporalMemory(GROUP1_INPUT + "Memory", MEMORY_LAYER);
		group1MemoryConfig.sizeX = TM_SIZE_X;
		group1MemoryConfig.sizeY = TM_SIZE_Y;
		group1MemoryConfig.sizeZ = TM_SIZE_Z;
		if (QUICK_LEARN) {
			group1MemoryConfig.initialPermanence = group1MemoryConfig.permanenceThreshold + 0.1F;
		}
		r.addLink(GROUP1_INPUT + "Pooler", SpatialPooler.ACTIVE_COLUMNS_OUTPUT, GROUP1_INPUT + "Memory", 0);
		
		TemporalMemoryConfig group2MemoryConfig = r.addTemporalMemory(GROUP2_INPUT + "Memory", MEMORY_LAYER);
		group2MemoryConfig.sizeX = TM_SIZE_X;
		group2MemoryConfig.sizeY = TM_SIZE_Y;
		group2MemoryConfig.sizeZ = TM_SIZE_Z;
		if (QUICK_LEARN) {
			group2MemoryConfig.initialPermanence = group2MemoryConfig.permanenceThreshold + 0.1F;
		}
		r.addLink(GROUP2_INPUT + "Pooler", SpatialPooler.ACTIVE_COLUMNS_OUTPUT, GROUP2_INPUT + "Memory", 0);
		
		for (PatternInstrument inst: InstrumentPattern.INSTRUMENTS) {
			String inputName = "";
			if (inst.group()==1) {
				inputName = GROUP1_INPUT;
			} else if (inst.group()==2) {
				inputName = GROUP2_INPUT;
			}
			addClassifier(r, inst.name(), inputName);
		}
		return r;
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
