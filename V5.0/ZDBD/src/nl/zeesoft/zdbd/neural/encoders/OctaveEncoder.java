package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicFeatureEncoder;

public class OctaveEncoder extends BasicFeatureEncoder {
	public OctaveEncoder(int onBits) {
		setOnBits(onBits);
		setFeatures(3);
	}
}
