package nl.zeesoft.zdk.neural.processor.cl;

import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrHistory;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class Classifier extends Processor {
	public static final int		ASSOCIATE_SDR_INPUT		= 0;
	
	public static final int		ASSOCIATED_SDR_OUTPUT	= 0;
	
	public ClConfig				config					= null;
	public SdrHistory			activationHistory		= new SdrHistory();
	public ClBits				bits					= null;

	public void initialize(ClConfig config) {
		this.config = config.copy();
		
		activationHistory.initialize(config.size.volume());
		activationHistory.capacity = config.predictStep + 1;
		
		bits = new ClBits(this, this.config, activationHistory);
	}
	
	@Override
	public void reset() {
		activationHistory.sdrs.clear();
		bits.reset();
	}
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = super.getInputOutputConfig();
		if (config!=null) {
			r = config.getInputOutputConfig();
		}
		return r;
	}

	@Override
	protected void processValidIO(ProcessorIO io) {
		Sdr input = io.inputs.get(ASSOCIATE_SDR_INPUT).copy();
		input.subsample(config.maxOnBits);
		activationHistory.push(input);
		if (config.learn) {
			bits.associateBits(io.inputValue);
		}
		io.outputValue = bits.generatePrediction(input);
		io.outputs.add(input.copy());
	}
	
	@Override
	protected boolean isInitialized(ProcessorIO io) {
		if (config==null || bits==null) {
			io.error = this.getClass().getSimpleName() + " is not initialized";
		}
		return io.error.length() == 0;
	}
}