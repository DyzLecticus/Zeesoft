package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicFeatureEncoder;

public class NoteEncoder extends BasicFeatureEncoder {
	public NoteEncoder(int onBits) {
		setOnBits(onBits);
		setFeatures(12);
	}
}
