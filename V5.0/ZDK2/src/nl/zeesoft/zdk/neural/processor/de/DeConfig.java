package nl.zeesoft.zdk.neural.processor.de;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.encoder.datetime.DateTimeSdrEncoder;
import nl.zeesoft.zdk.neural.processor.ConfigurableIO;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class DeConfig extends DateTimeSdrEncoder implements ConfigurableIO {
	@Override
	public DeConfig copy() {
		DeConfig r = new DeConfig();
		r.copyFrom(this);
		return r;
	}
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = new InputOutputConfig();
		r.addInput("SensorValue", 0);
		r.addOutput("EncodedSensor", new Size(getEncodeLength(),1));
		return r;
	}
}
