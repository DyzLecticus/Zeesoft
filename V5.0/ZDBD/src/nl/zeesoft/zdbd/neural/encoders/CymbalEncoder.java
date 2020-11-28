package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicFeatureArrayEncoder;

public class CymbalEncoder extends BasicFeatureArrayEncoder {
	public CymbalEncoder(int onBits) {
		setOnBits(onBits);
		setFeatures(3,3);
	}
}
