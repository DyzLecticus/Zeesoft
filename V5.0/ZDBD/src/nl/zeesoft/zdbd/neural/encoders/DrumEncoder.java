package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;

public class DrumEncoder extends BasicScalarEncoder {
	public DrumEncoder() {
		encodeSizeX = 2;
		encodeSizeY = 3;
		onBits = 2;
		minValue = 0;
		maxValue = 2;
	}
}
