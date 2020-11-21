package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicFeatureEncoder;

public class DrumEncoder extends BasicFeatureEncoder {
	public DrumEncoder(int onBits) {
		setOnBits(onBits);
		setFeatures(3);
	}
}
