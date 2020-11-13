package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;

public class StepEncoder extends BasicScalarEncoder {
	public StepEncoder() {
		encodeSizeX = 4;
		encodeSizeY = 8;
		onBits = 1;
		minValue = 0;
		maxValue = 31;
	}
}
