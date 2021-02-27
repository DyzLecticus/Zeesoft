package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicFeatureEncoder;

public class ShiftEncoder extends BasicFeatureEncoder {
	public ShiftEncoder(int onBits) {
		setOnBits(onBits);
		setFeatures(5);
	}
}
