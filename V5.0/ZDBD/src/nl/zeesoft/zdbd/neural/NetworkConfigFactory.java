package nl.zeesoft.zdbd.neural;

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
	public static String	DRUM_INPUT			= "Drum";
	
	public static NetworkConfig getNetworkConfig(int predictSteps) {
		NetworkConfig r = new NetworkConfig();
		
		int tmSizeX = 48;
		int tmSizeY = 48;
		int tmSizeZ = 32;
		
		r.inputNames.add(CONTEXT_INPUT);
		
		SpatialPoolerConfig contextPoolerConfig = r.addSpatialPooler(CONTEXT_INPUT + "Pooler", POOLER_LAYER);
		contextPoolerConfig.inputSizeX = Rythm.sizeX();
		contextPoolerConfig.inputSizeY = Rythm.sizeY();
		contextPoolerConfig.outputSizeX = tmSizeX;
		contextPoolerConfig.outputSizeY = tmSizeY / 2;
		contextPoolerConfig.outputOnBits = 20;
		r.addLink(CONTEXT_INPUT, 0, CONTEXT_INPUT + "Pooler", 0);

		r.inputNames.add(DRUM_INPUT);
		
		SpatialPoolerConfig drumPoolerConfig = r.addSpatialPooler(DRUM_INPUT + "Pooler", POOLER_LAYER);
		drumPoolerConfig.inputSizeX = DrumPattern.sizeX();
		drumPoolerConfig.inputSizeY = DrumPattern.sizeY();
		drumPoolerConfig.outputSizeX = tmSizeX;
		drumPoolerConfig.outputSizeY = tmSizeY / 2;
		drumPoolerConfig.outputOnBits = 26;
		r.addLink(DRUM_INPUT, 0, DRUM_INPUT + "Pooler", 0);
		
		MergerConfig drumMergerConfig = r.addMerger(DRUM_INPUT + "Merger", MERGER_LAYER);
		drumMergerConfig.sizeX = tmSizeX;
		drumMergerConfig.sizeY = tmSizeY;
		drumMergerConfig.concatenate = true;
		r.addLink(CONTEXT_INPUT + "Pooler", 0, DRUM_INPUT + "Merger", 0);
		r.addLink(DRUM_INPUT + "Pooler", 0, DRUM_INPUT + "Merger", 1);
		
		TemporalMemoryConfig drumMemoryConfig = r.addTemporalMemory(DRUM_INPUT + "Memory", MEMORY_LAYER);
		drumMemoryConfig.sizeX = tmSizeX;
		drumMemoryConfig.sizeY = tmSizeY;
		drumMemoryConfig.sizeZ = tmSizeZ;
		r.addLink(DRUM_INPUT + "Merger", 0, DRUM_INPUT + "Memory", 0);
		
		for (int d = 0; d < DrumPattern.DRUM_NAMES.length; d++) {
			ClassifierConfig drumClassifierConfig = r.addClassifier(DrumPattern.DRUM_NAMES[d] + "Classifier", CLASSIFIER_LAYER);
			drumClassifierConfig.sizeX = tmSizeX * tmSizeZ;
			drumClassifierConfig.sizeY = tmSizeY;
			drumClassifierConfig.valueKey = DrumPattern.DRUM_NAMES[d];
			drumClassifierConfig.logPredictionAccuracy = true;
			drumClassifierConfig.accuracyHistorySize = 256;
			drumClassifierConfig.accuracyTrendSize = 32;
			r.addLink(DRUM_INPUT + "Memory", 0, DrumPattern.DRUM_NAMES[d] + "Classifier", 0);
			r.addLink(DRUM_INPUT, 0, DrumPattern.DRUM_NAMES[d] + "Classifier", 1);
		}
		return r;
	}
}
