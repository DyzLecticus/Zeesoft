package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicFeatureEncoder;

public class HihatEncoder extends BasicFeatureEncoder {
	public HihatEncoder(int onBits) {
		setOnBits(onBits);
		setFeatures(5);
	}
}
