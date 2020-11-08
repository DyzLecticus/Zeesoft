package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.neural.BasicScalarEncoder;
import nl.zeesoft.zdk.neural.SDREncoder;

public class ScalarEncoder extends AbstractEncoder {
	// Configuration
	protected float					minValue		= 0;
	protected float					maxValue		= 200;
	protected float					resolution		= 1;
	protected boolean				periodic		= false;
	
	@Override
	public void configure(SDRProcessorConfig config) {
		super.configure(config);
		if (config instanceof ScalarEncoderConfig) {
			ScalarEncoderConfig cfg = (ScalarEncoderConfig) config;
			this.minValue = cfg.minValue;
			this.maxValue = cfg.maxValue;
			this.resolution = cfg.resolution;
			this.periodic = cfg.periodic;
		}
	}
	
	@Override
	protected SDREncoder getNewEncoder() {
		BasicScalarEncoder r = new BasicScalarEncoder();
		r.setMinValue(minValue);
		r.setMaxValue(maxValue);
		r.setResolution(resolution);
		r.setPeriodic(periodic);
		return r;
	}
}
