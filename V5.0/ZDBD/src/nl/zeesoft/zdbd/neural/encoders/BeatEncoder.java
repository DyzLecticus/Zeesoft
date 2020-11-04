package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;

public class BeatEncoder extends BasicScalarEncoder {
	public BeatEncoder() {
		encodeSizeX = 8;
		encodeSizeY = 4;
		onBits = 4;
		minValue = 0;
		maxValue = 7;
	}
}
