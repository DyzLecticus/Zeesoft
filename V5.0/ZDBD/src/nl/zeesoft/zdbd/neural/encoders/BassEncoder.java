package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicFeatureEncoder;

public class BassEncoder extends BasicFeatureEncoder {
	public BassEncoder(int onBits) {
		setOnBits(onBits);
		setFeatures(17);
	}
}
