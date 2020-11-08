package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;

public class PatternEncoder extends BasicScalarEncoder {
	public PatternEncoder() {
		encodeSizeX = 2;
		encodeSizeY = 4;
		onBits = 2;
		minValue = 0;
		maxValue = 3;
	}
}
