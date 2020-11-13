package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;

public class BassEncoder extends BasicScalarEncoder {
	public BassEncoder() {
		encodeSizeX = 3;
		encodeSizeY = 6;
		onBits = 1;
		minValue = 0;
		maxValue = 17;
	}
}
