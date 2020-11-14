package nl.zeesoft.zdbd.neural;

import nl.zeesoft.zdbd.pattern.DrumAndBassPattern;
import nl.zeesoft.zdbd.pattern.Rythm;
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
	public static int		SP_ON_BITS				= 50;
	public static int		TM_SIZE_X				= 50;
	public static int		TM_SIZE_Y				= 50;
	public static int		TM_SIZE_Z				= 32;
	public static boolean	QUICK_LEARN				= true;
	public static boolean	CLASSIFY_RYTHM			= false;

	public static int		INPUT_MERGER_LAYER		= 0;
	public static int		MAIN_MERGER_LAYER		= 1;
	public static int		POOLER_LAYER			= 2;
	public static int		MEMORY_LAYER			= 3;
	public static int		CLASSIFIER_LAYER		= 4;
		
	public static String	CONTEXT_INPUT			= "Context";
	public static String	PATTERN_INPUT			= "Pattern";
		
	public static NetworkConfig getNetworkConfig() {
		NetworkConfig r = new NetworkConfig();
		
		r.inputNames.add(CONTEXT_INPUT);
		r.inputNames.add(PATTERN_INPUT);
		
		MergerConfig inputMergerConfig = r.addMerger(CONTEXT_INPUT + "InputMerger",INPUT_MERGER_LAYER);
		inputMergerConfig.sizeX = Rythm.sizeX();
		inputMergerConfig.sizeY = Rythm.sizeY();
		r.addLink(CONTEXT_INPUT, 0, CONTEXT_INPUT + "InputMerger", 0);
		
		inputMergerConfig = r.addMerger(PATTERN_INPUT + "InputMerger",INPUT_MERGER_LAYER);
		inputMergerConfig.sizeX = DrumAndBassPattern.sizeX();
		inputMergerConfig.sizeY = DrumAndBassPattern.sizeY();
		r.addLink(PATTERN_INPUT, 0, PATTERN_INPUT + "InputMerger", 0);

		SDR sdr = new SDR((Rythm.sizeX() * Rythm.sizeY()) + (DrumAndBassPattern.sizeX() * DrumAndBassPattern.sizeY()),1);
		sdr.square();
		
		MergerConfig mergerConfig = r.addMerger("Merger",MAIN_MERGER_LAYER);
		mergerConfig.sizeX = sdr.sizeX();
		mergerConfig.sizeY = sdr.sizeY();
		mergerConfig.concatenate = true;
		r.addLink(CONTEXT_INPUT + "InputMerger", Merger.MERGED_OUTPUT, "Merger", 0);
		r.addLink(PATTERN_INPUT + "InputMerger", Merger.MERGED_OUTPUT, "Merger", 1);

		SpatialPoolerConfig poolerConfig = r.addSpatialPooler("Pooler", POOLER_LAYER);
		poolerConfig.inputSizeX = mergerConfig.sizeX;
		poolerConfig.inputSizeY = mergerConfig.sizeY;
		poolerConfig.potentialRadius = 0;
		poolerConfig.outputSizeX = TM_SIZE_X;
		poolerConfig.outputSizeY = TM_SIZE_Y;
		poolerConfig.outputOnBits = SP_ON_BITS;
		r.addLink("Merger", Merger.MERGED_OUTPUT, "Pooler", 0);
		
		TemporalMemoryConfig patternMemoryConfig = r.addTemporalMemory("Memory", MEMORY_LAYER);
		patternMemoryConfig.sizeX = TM_SIZE_X;
		patternMemoryConfig.sizeY = TM_SIZE_Y;
		patternMemoryConfig.sizeZ = TM_SIZE_Z;
		if (QUICK_LEARN) {
			patternMemoryConfig.initialPermanence = patternMemoryConfig.permanenceThreshold + 0.1F;
		}
		r.addLink("Pooler", SpatialPooler.ACTIVE_COLUMNS_OUTPUT, "Memory", 0);
		
		for (int i = 0; i < DrumAndBassPattern.INSTRUMENT_NAMES.length; i++) {
			addClassifier(r, DrumAndBassPattern.INSTRUMENT_NAMES[i], PATTERN_INPUT);
		}
		if (CLASSIFY_RYTHM) {
			for (int e = 0; e < Rythm.ELEMENT_NAMES.length; e++) {
				addClassifier(r, Rythm.ELEMENT_NAMES[e], CONTEXT_INPUT);
			}
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
		r.addLink("Memory", TemporalMemory.ACTIVE_CELLS_OUTPUT, name + "Classifier", 0);
		r.addLink(inputName, 0, name + "Classifier", 1);
	}
}
