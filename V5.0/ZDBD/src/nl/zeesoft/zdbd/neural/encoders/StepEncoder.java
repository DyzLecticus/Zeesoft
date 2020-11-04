package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;

public class StepEncoder extends BasicScalarEncoder {
	public StepEncoder() {
		encodeSizeX = 16;
		encodeSizeY = 8;
		onBits = 4;
		minValue = 0;
		maxValue = 31;
	}
}
