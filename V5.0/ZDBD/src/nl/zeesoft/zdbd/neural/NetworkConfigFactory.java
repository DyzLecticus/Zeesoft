package nl.zeesoft.zdbd.neural;

import nl.zeesoft.zdbd.neural.encoders.EncoderFactory;
import nl.zeesoft.zdbd.pattern.DrumPattern;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.processors.ClassifierConfig;
import nl.zeesoft.zdk.neural.processors.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.processors.TemporalMemoryConfig;

public class NetworkConfigFactory {
	public static NetworkConfig getNetworkConfig(int predictSteps) {
		NetworkConfig r = new NetworkConfig();
		
		int tmSizeX = 32;
		int tmSizeY = 32;
		int tmSizeZ = 8;
		
		r.inputNames.add("Context");
		
		SpatialPoolerConfig contextPoolerConfig = r.addSpatialPooler("ContextPooler", 0);
		contextPoolerConfig.inputSizeX = Rythm.sizeX();
		contextPoolerConfig.inputSizeY = Rythm.sizeY();
		contextPoolerConfig.outputSizeX = tmSizeX;
		contextPoolerConfig.outputSizeY = tmSizeY;
		contextPoolerConfig.outputOnBits = 20;
		r.addLink("Context", 0, "ContextPooler", 0);

		for (int d = 0; d < DrumPattern.DRUM_NAMES.length; d++) {
			r.inputNames.add(DrumPattern.DRUM_NAMES[d]);
			
			SpatialPoolerConfig drumPoolerConfig = r.addSpatialPooler(DrumPattern.DRUM_NAMES[d] + "Pooler", 0);
			drumPoolerConfig.inputSizeX = EncoderFactory.drumEncoder.getEncodeSizeX();
			drumPoolerConfig.inputSizeY = EncoderFactory.drumEncoder.getEncodeSizeY();
			drumPoolerConfig.outputSizeX = tmSizeX;
			drumPoolerConfig.outputSizeY = tmSizeY;
			drumPoolerConfig.outputOnBits = 20;
			r.addLink(DrumPattern.DRUM_NAMES[d], 0, DrumPattern.DRUM_NAMES[d] + "Pooler", 0);
			
			TemporalMemoryConfig drumMemoryConfig = r.addTemporalMemory(DrumPattern.DRUM_NAMES[d] + "Memory", 1);
			drumMemoryConfig.sizeX = tmSizeX;
			drumMemoryConfig.sizeY = tmSizeY;
			drumMemoryConfig.sizeZ = tmSizeZ;
			r.addLink(DrumPattern.DRUM_NAMES[d] + "Pooler", 0, DrumPattern.DRUM_NAMES[d] + "Memory", 0);
			r.addLink("ContextPooler", 0, DrumPattern.DRUM_NAMES[d] + "Memory", 1);
			
			ClassifierConfig drumClassifierConfig = r.addClassifier(DrumPattern.DRUM_NAMES[d] + "Classifier", 2);
			drumClassifierConfig.sizeX = tmSizeX * tmSizeZ;
			drumClassifierConfig.sizeY = tmSizeY;
			drumClassifierConfig.valueKey = DrumPattern.DRUM_NAMES[d];
			for (int s = 1; s <= predictSteps; s++) {
				drumClassifierConfig.predictSteps.add(s);
			}
			r.addLink(DrumPattern.DRUM_NAMES[d] + "Memory", 0, DrumPattern.DRUM_NAMES[d] + "Classifier", 0);
			r.addLink(DrumPattern.DRUM_NAMES[d], 0, DrumPattern.DRUM_NAMES[d] + "Classifier", 1);
		}
		return r;
	}
}
