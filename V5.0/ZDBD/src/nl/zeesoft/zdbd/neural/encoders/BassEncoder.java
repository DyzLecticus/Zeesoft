package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;

public class BassEncoder extends BasicScalarEncoder {
	public BassEncoder() {
		encodeSizeX = 8;
		encodeSizeY = 9;
		onBits = 4;
		minValue = 0;
		maxValue = 17;
	}
}
