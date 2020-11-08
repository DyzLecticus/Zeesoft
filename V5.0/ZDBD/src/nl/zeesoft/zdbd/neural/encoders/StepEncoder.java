package nl.zeesoft.zdbd.neural.encoders;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;

public class StepEncoder extends BasicScalarEncoder {
	public StepEncoder() {
		encodeSizeX = 8;
		encodeSizeY = 8;
		onBits = 2;
		minValue = 0;
		maxValue = 31;
	}
}
