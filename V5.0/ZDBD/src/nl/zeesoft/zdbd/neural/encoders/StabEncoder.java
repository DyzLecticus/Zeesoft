package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicFeatureEncoder;

public class StabEncoder extends BasicFeatureEncoder {
	public StabEncoder(int onBits) {
		setOnBits(onBits);
		setFeatures(17);
	}
}
