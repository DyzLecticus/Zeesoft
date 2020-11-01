package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;
import nl.zeesoft.zdk.thread.CodeRunnerList;

public class ScalarEncoder extends AbstractEncoder {
	// Configuration
	protected float					minValue		= 0;
	protected float					maxValue		= 200;
	protected boolean				periodic		= false;
	
	@Override
	public void configure(SDRProcessorConfig config) {
		super.configure(config);
		if (config instanceof ScalarEncoderConfig) {
			ScalarEncoderConfig cfg = (ScalarEncoderConfig) config;
			this.minValue = cfg.minValue;
			this.maxValue = cfg.maxValue;
			this.periodic = cfg.periodic;
		}
	}

	@Override
	public void initialize(CodeRunnerList runnerList) {
		super.initialize(runnerList);
		BasicScalarEncoder enc = (BasicScalarEncoder) encoder;
		enc.setMinValue(minValue);
		enc.setMaxValue(maxValue);
		enc.setPeriodic(periodic);
	}
}
