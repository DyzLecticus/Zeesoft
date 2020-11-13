package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;

public class BeatEncoder extends BasicScalarEncoder {
	public BeatEncoder() {
		encodeSizeX = 2;
		encodeSizeY = 4;
		onBits = 1;
		minValue = 0;
		maxValue = 7;
	}
}
