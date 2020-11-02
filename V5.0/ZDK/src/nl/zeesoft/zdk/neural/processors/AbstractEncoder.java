package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.BasicScalarEncoder;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.SDREncoder;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.RunCode;

public abstract class AbstractEncoder extends SDRProcessor {
	public static final int			SDR_OUTPUT		= 0;
	public static final int			VALUE_OUTPUT	= 1;
	
	// Configuration
	protected int					sizeX			= 16;
	protected int					sizeY			= 16;
	protected int					onBits			= 16;
	
	// State
	protected Object				value			= null;
	protected SDREncoder			encoder			= null;
	
	@Override
	public void configure(SDRProcessorConfig config) {
		if (config instanceof AbstractEncoderConfig) {
			AbstractEncoderConfig cfg = (AbstractEncoderConfig) config;
			this.sizeX = cfg.sizeX;
			this.sizeY = cfg.sizeY;
			this.onBits = cfg.onBits;
		}
	}

	@Override
	public void initialize(CodeRunnerList runnerList) {
		if (sizeX < 2) {
			sizeX = 2;
		}
		if (sizeY < 2) {
			sizeY = 2;
		}
		encoder = getNewEncoder();
		encoder.setEncodeDimensions(sizeX, sizeY);
		encoder.setOnBits(onBits);
	}
	
	public void setValue(Object value) {
		KeyValueSDR input = new KeyValueSDR();
		input.setValue(value);
		setInput(input);
	}
	
	@Override
	public void setInput(SDR... sdrs) {
		outputs.clear();
		if (sdrs.length>0) {
			value = null;
			if (sdrs[0] instanceof KeyValueSDR) {
				value = ((KeyValueSDR)sdrs[0]).getValue();
			}
			if (value!=null) {
				super.setInput(sdrs);
			} else {
				Logger.err(this, new Str("A KeyValueSDR with a value to encode is required"));
			}
		} else {
			Logger.err(this, new Str("At least one input SDR is required"));
		}
		outputs.add(new SDR(sizeX, sizeY));
		outputs.add(new KeyValueSDR());
	}

	@Override
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn, int threads) {
		CodeRunnerList encodeValue = new CodeRunnerList(
			new RunCode() {
				@Override
				protected boolean run() {
					if (value!=null) {
						SDR output1 = encoder.getEncodedValue(value);
						KeyValueSDR output2 = new KeyValueSDR();
						output2.setValue(value);
						outputs.clear();
						outputs.add(output1);
						outputs.add(output2);
					}
					return true;
				}
			}
		);
		runnerChain.add(encodeValue);
		addIncrementProcessedToProcessorChain(runnerChain);
	}
	
	protected SDREncoder getNewEncoder() {
		return new BasicScalarEncoder();
	}
}
