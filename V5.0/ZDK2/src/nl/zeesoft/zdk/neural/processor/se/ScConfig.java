package nl.zeesoft.zdk.neural.processor.se;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.ScalarSdrEncoder;
import nl.zeesoft.zdk.neural.processor.ConfigurableIO;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class ScConfig extends ScalarSdrEncoder implements ConfigurableIO {
	@Override
	public ScConfig copy() {
		ScConfig r = new ScConfig();
		r.copyFrom(this);
		return r;
	}
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = new InputOutputConfig();
		r.addInput("SensorValue", (int)maxValue);
		r.addOutput("EncodedSensor", new Size(encodeLength,1));
		return r;
	}
}
