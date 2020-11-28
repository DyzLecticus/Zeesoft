package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicFeatureArrayEncoder;

public class PercussionEncoder extends BasicFeatureArrayEncoder {
	public PercussionEncoder(int onBits) {
		setOnBits(onBits);
		setFeatures(3,3);
	}
}
