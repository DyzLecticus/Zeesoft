package nl.zeesoft.zdk.neural.processor.se;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.encoder.ScalarSdrEncoder;
import nl.zeesoft.zdk.neural.processor.ConfigurableIO;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class SeConfig extends ScalarSdrEncoder implements ConfigurableIO {
	@Override
	public SeConfig copy() {
		SeConfig r = new SeConfig();
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
