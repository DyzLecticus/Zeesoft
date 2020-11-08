package nl.zeesoft.zdbd.neural;

import nl.zeesoft.zdbd.pattern.DrumAndBassPattern;
import nl.zeesoft.zdbd.pattern.DrumPattern;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.processors.ClassifierConfig;
import nl.zeesoft.zdk.neural.processors.MergerConfig;
import nl.zeesoft.zdk.neural.processors.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.processors.TemporalMemoryConfig;

public class NetworkConfigFactory {
	public static int		POOLER_LAYER		= 0;
	public static int		MERGER_LAYER		= 1;
	public static int		MEMORY_LAYER		= 2;
	public static int		CLASSIFIER_LAYER	= 3;
	
	public static String	CONTEXT_INPUT		= "Context";
	public static String	PATTERN_INPUT		= "Pattern";
	
	public static NetworkConfig getNetworkConfig(int predictSteps) {
		NetworkConfig r = new NetworkConfig();
		
		int tmSizeX = 48;
		int tmSizeY = 48;
		int tmSizeZ = 32;
		
		r.inputNames.add(CONTEXT_INPUT);
		r.inputNames.add(PATTERN_INPUT);
				
		SpatialPoolerConfig contextPoolerConfig = r.addSpatialPooler(CONTEXT_INPUT + "Pooler", POOLER_LAYER);
		contextPoolerConfig.inputSizeX = Rythm.sizeX();
		contextPoolerConfig.inputSizeY = Rythm.sizeY();
		contextPoolerConfig.outputSizeX = tmSizeX;
		contextPoolerConfig.outputSizeY = tmSizeY / 2;
		contextPoolerConfig.outputOnBits = 20;
		r.addLink(CONTEXT_INPUT, 0, CONTEXT_INPUT + "Pooler", 0);

		SpatialPoolerConfig patternPoolerConfig = r.addSpatialPooler(PATTERN_INPUT + "Pooler", POOLER_LAYER);
		patternPoolerConfig.inputSizeX = DrumPattern.sizeX();
		patternPoolerConfig.inputSizeY = DrumPattern.sizeY();
		patternPoolerConfig.outputSizeX = tmSizeX;
		patternPoolerConfig.outputSizeY = tmSizeY / 2;
		patternPoolerConfig.outputOnBits = 26;
		r.addLink(PATTERN_INPUT, 0, PATTERN_INPUT + "Pooler", 0);
		
		MergerConfig patternMergerConfig = r.addMerger(PATTERN_INPUT + "Merger", MERGER_LAYER);
		patternMergerConfig.sizeX = tmSizeX;
		patternMergerConfig.sizeY = tmSizeY;
		patternMergerConfig.concatenate = true;
		r.addLink(CONTEXT_INPUT + "Pooler", 0, PATTERN_INPUT + "Merger", 0);
		r.addLink(PATTERN_INPUT + "Pooler", 0, PATTERN_INPUT + "Merger", 1);
		
		TemporalMemoryConfig patternMemoryConfig = r.addTemporalMemory(PATTERN_INPUT + "Memory", MEMORY_LAYER);
		patternMemoryConfig.sizeX = tmSizeX;
		patternMemoryConfig.sizeY = tmSizeY;
		patternMemoryConfig.sizeZ = tmSizeZ;
		r.addLink(PATTERN_INPUT + "Merger", 0, PATTERN_INPUT + "Memory", 0);
		
		for (int d = 0; d < DrumPattern.DRUM_NAMES.length; d++) {
			ClassifierConfig patternClassifierConfig = r.addClassifier(DrumPattern.DRUM_NAMES[d] + "Classifier", CLASSIFIER_LAYER);
			patternClassifierConfig.sizeX = tmSizeX * tmSizeZ;
			patternClassifierConfig.sizeY = tmSizeY;
			patternClassifierConfig.valueKey = DrumPattern.DRUM_NAMES[d];
			patternClassifierConfig.logPredictionAccuracy = true;
			patternClassifierConfig.accuracyHistorySize = 256;
			patternClassifierConfig.accuracyTrendSize = 32;
			r.addLink(PATTERN_INPUT + "Memory", 0, DrumPattern.DRUM_NAMES[d] + "Classifier", 0);
			r.addLink(PATTERN_INPUT, 0, DrumPattern.DRUM_NAMES[d] + "Classifier", 1);
		}
		return r;
	}
	
	public static NetworkConfig getNetworkConfig() {
		NetworkConfig r = new NetworkConfig();
		
		int spOnBits = 50;
		int tmSizeX = 50;
		int tmSizeY = 50;
		int tmSizeZ = 32;
		
		r.inputNames.add(CONTEXT_INPUT);
		r.inputNames.add(PATTERN_INPUT);
		
		SpatialPoolerConfig contextPoolerConfig = r.addSpatialPooler(CONTEXT_INPUT + "Pooler", POOLER_LAYER);
		contextPoolerConfig.inputSizeX = Rythm.sizeX();
		contextPoolerConfig.inputSizeY = Rythm.sizeY();
		contextPoolerConfig.outputSizeX = tmSizeX;
		contextPoolerConfig.outputSizeY = (tmSizeY / 10) * 3;
		contextPoolerConfig.outputOnBits = (spOnBits / 10) * 3;
		r.addLink(CONTEXT_INPUT, 0, CONTEXT_INPUT + "Pooler", 0);

		SpatialPoolerConfig patternPoolerConfig = r.addSpatialPooler(PATTERN_INPUT + "Pooler", POOLER_LAYER);
		patternPoolerConfig.inputSizeX = DrumAndBassPattern.sizeX();
		patternPoolerConfig.inputSizeY = DrumAndBassPattern.sizeY();
		patternPoolerConfig.outputSizeX = tmSizeX;
		patternPoolerConfig.outputSizeY = tmSizeY - contextPoolerConfig.outputSizeY;
		patternPoolerConfig.outputOnBits = spOnBits - contextPoolerConfig.outputOnBits;
		
		r.addLink(PATTERN_INPUT, 0, PATTERN_INPUT + "Pooler", 0);
		
		MergerConfig patternMergerConfig = r.addMerger(PATTERN_INPUT + "Merger", MERGER_LAYER);
		patternMergerConfig.sizeX = tmSizeX;
		patternMergerConfig.sizeY = tmSizeY;
		patternMergerConfig.concatenate = true;
		r.addLink(CONTEXT_INPUT + "Pooler", 0, PATTERN_INPUT + "Merger", 0);
		r.addLink(PATTERN_INPUT + "Pooler", 0, PATTERN_INPUT + "Merger", 1);
		
		TemporalMemoryConfig patternMemoryConfig = r.addTemporalMemory(PATTERN_INPUT + "Memory", MEMORY_LAYER);
		patternMemoryConfig.sizeX = tmSizeX;
		patternMemoryConfig.sizeY = tmSizeY;
		patternMemoryConfig.sizeZ = tmSizeZ;
		r.addLink(PATTERN_INPUT + "Merger", 0, PATTERN_INPUT + "Memory", 0);
		
		for (int d = 0; d < DrumAndBassPattern.INSTRUMENT_NAMES.length; d++) {
			ClassifierConfig patternClassifierConfig = r.addClassifier(DrumAndBassPattern.INSTRUMENT_NAMES[d] + "Classifier", CLASSIFIER_LAYER);
			patternClassifierConfig.sizeX = tmSizeX * tmSizeZ;
			patternClassifierConfig.sizeY = tmSizeY;
			patternClassifierConfig.valueKey = DrumAndBassPattern.INSTRUMENT_NAMES[d];
			patternClassifierConfig.logPredictionAccuracy = true;
			patternClassifierConfig.accuracyHistorySize = 256;
			patternClassifierConfig.accuracyTrendSize = 32;
			r.addLink(PATTERN_INPUT + "Memory", 0, DrumAndBassPattern.INSTRUMENT_NAMES[d] + "Classifier", 0);
			r.addLink(PATTERN_INPUT, 0, DrumAndBassPattern.INSTRUMENT_NAMES[d] + "Classifier", 1);
		}
		return r;
	}
}
