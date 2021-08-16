package nl.zeesoft.zdk.neural.processor.cl;

import nl.zeesoft.zdk.json.Finalizable;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrHistory;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.LearningProcessor;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class Classifier extends LearningProcessor implements Finalizable {
	public static final int		ASSOCIATE_SDR_INPUT		= 0;
	public static final int		ASSOCIATE_VALUE_INPUT	= 1;
	
	public static final int		ASSOCIATED_SDR_OUTPUT	= 0;
	
	public ClConfig				config					= null;
	public SdrHistory			activationHistory		= new SdrHistory();
	public ClBits				bits					= null;

	public int					processed				= 0;

	@Override
	public void finalizeObject() {
		bits.setConfig(config, activationHistory);
	}

	public void initialize(ClConfig config) {
		this.config = config.copy();
		
		activationHistory.initialize(config.size.volume());
		activationHistory.capacity = config.predictStep + 1;
		
		bits = new ClBits(this.config, activationHistory);
	}

	@Override
	public void reset() {
		activationHistory.sdrs.clear();
		if (bits!=null) {
			bits.reset();
		}
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
		activationHistory.push(input.copy());
		if (learn) {
			bits.associateBits(io.inputValue, processed);
		}
		io.outputValue = bits.generatePrediction(input, io.inputValue, processed);
		io.outputs.add(input.copy());
		processed++;
	}
	
	@Override
	protected boolean isInitialized(ProcessorIO io) {
		if (config==null || bits==null) {
			io.error = this.getClass().getSimpleName() + " is not initialized";
		}
		return io.error.length() == 0;
	}
}
