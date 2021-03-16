package nl.zeesoft.zdk.neural.processor.cl;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrHistory;
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
	protected void processValidIO(ProcessorIO io) {
		Sdr input = io.inputs.get(ASSOCIATE_SDR_INPUT).copy();
		input.subsample(config.maxOnBits);
		io.outputs.add(input.copy());
		activationHistory.push(input);
		
		bits.associateBits(io.inputValue);
		
		io.outputValue = bits.generatePrediction(input);
	}

	@Override
	protected int getMaxInputVolume(int index) {
		return config.size.volume();
	}
	
	@Override
	protected Size getOutputSize(int index) {
		return config.size;
	}
	
	@Override
	protected boolean isInitialized(ProcessorIO io) {
		if (config==null || bits==null) {
			io.error = this.getClass().getSimpleName() + " is not initialized";
		}
		return io.error.length() == 0;
	}

	@Override
	protected String getInputName(int index) {
		return "AssociateSDR";
	}

	@Override
	protected String getOutputName(int index) {
		return "AssociatedSDR";
	}
}
