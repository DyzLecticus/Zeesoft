package nl.zeesoft.zdbd.neural;

import nl.zeesoft.zdbd.pattern.DrumAndBassPattern;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.processors.ClassifierConfig;
import nl.zeesoft.zdk.neural.processors.MergerConfig;
import nl.zeesoft.zdk.neural.processors.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.processors.TemporalMemoryConfig;

public class NetworkConfigFactory {
	public static int		SP_ON_BITS				= 82;
	public static int		TM_SIZE_X				= 64;
	public static int		TM_SIZE_Y				= 64;
	public static int		TM_SIZE_Z				= 32;
	public static boolean	QUICK_LEARN				= true;

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
		r.addLink(CONTEXT_INPUT + "InputMerger", 0, "Merger", 0);
		r.addLink(PATTERN_INPUT + "InputMerger", 0, "Merger", 1);

		SpatialPoolerConfig contextPoolerConfig = r.addSpatialPooler("Pooler", POOLER_LAYER);
		contextPoolerConfig.inputSizeX = mergerConfig.sizeX;
		contextPoolerConfig.inputSizeY = mergerConfig.sizeY;
		contextPoolerConfig.outputSizeX = TM_SIZE_X;
		contextPoolerConfig.outputSizeY = TM_SIZE_Y;
		contextPoolerConfig.outputOnBits = SP_ON_BITS;
		r.addLink("Merger", 0, "Pooler", 0);
		
		TemporalMemoryConfig patternMemoryConfig = r.addTemporalMemory("Memory", MEMORY_LAYER);
		patternMemoryConfig.sizeX = TM_SIZE_X;
		patternMemoryConfig.sizeY = TM_SIZE_Y;
		patternMemoryConfig.sizeZ = TM_SIZE_Z;
		if (QUICK_LEARN) {
			patternMemoryConfig.initialPermanence = patternMemoryConfig.permanenceThreshold + 0.1F;
		}
		r.addLink("Pooler", 0, "Memory", 0);
		
		for (int d = 0; d < DrumAndBassPattern.INSTRUMENT_NAMES.length; d++) {
			ClassifierConfig patternClassifierConfig = r.addClassifier(DrumAndBassPattern.INSTRUMENT_NAMES[d] + "Classifier", CLASSIFIER_LAYER);
			patternClassifierConfig.sizeX = TM_SIZE_X * TM_SIZE_Z;
			patternClassifierConfig.sizeY = TM_SIZE_Y;
			patternClassifierConfig.valueKey = DrumAndBassPattern.INSTRUMENT_NAMES[d];
			patternClassifierConfig.logPredictionAccuracy = true;
			patternClassifierConfig.accuracyHistorySize = 256;
			patternClassifierConfig.accuracyTrendSize = 32;
			r.addLink("Memory", 0, DrumAndBassPattern.INSTRUMENT_NAMES[d] + "Classifier", 0);
			r.addLink(PATTERN_INPUT, 0, DrumAndBassPattern.INSTRUMENT_NAMES[d] + "Classifier", 1);
		}
		return r;
	}
}
