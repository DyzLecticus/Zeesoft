package nl.zeesoft.zdk.neural.processor.mr;

import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.Processor;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class Merger extends Processor {
	public static final int		SDR_INPUT_1				= 0;
	public static final int		SDR_INPUT_2				= 1;
	public static final int		SDR_INPUT_3				= 2;
	public static final int		SDR_INPUT_4				= 3;
	public static final int		SDR_INPUT_5				= 4;
	public static final int		SDR_INPUT_6				= 5;
	public static final int		SDR_INPUT_7				= 6;
	public static final int		SDR_INPUT_8				= 7;
	public static final int		SDR_INPUT_9				= 8;
	
	public static final int		MERGED_SDR_OUTPUT		= 0;
	
	public MrConfig				config					= null;

	public void initialize(MrConfig config) {
		this.config = config.copy();
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
		int length = getOutputSize(MERGED_SDR_OUTPUT).volume();
		Sdr output = new Sdr(length);
		if (config.concatenate) {
			output.concat(io.inputs);
		} else {
			for (Sdr input: io.inputs) {
				output.or(input);
			}
		}
		output.subsample(config.maxOnBits);
		output.distort(config.distortion);
		io.outputs.add(output);
	}
	
	@Override
	protected boolean isInitialized(ProcessorIO io) {
		if (config==null) {
			io.error = this.getClass().getSimpleName() + " is not initialized";
		}
		return io.error.length() == 0;
	}
}
