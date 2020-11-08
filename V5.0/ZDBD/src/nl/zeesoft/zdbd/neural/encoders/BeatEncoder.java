package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;

public class BeatEncoder extends BasicScalarEncoder {
	public BeatEncoder() {
		encodeSizeX = 4;
		encodeSizeY = 4;
		onBits = 2;
		minValue = 0;
		maxValue = 7;
	}
}
