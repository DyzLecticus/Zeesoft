package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicFeatureArrayEncoder;

public class ContextEncoder extends BasicFeatureArrayEncoder {
	public ContextEncoder(int onBits) {
		setOnBits(onBits);
		setFeatures(4,8,8);
	}
}
