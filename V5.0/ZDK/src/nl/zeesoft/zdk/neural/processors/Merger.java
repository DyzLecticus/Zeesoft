package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.RunCode;

public class Merger extends SDRProcessor {
	public static final int			MERGED_OUTPUT	= 0;
	
	// Configuration
	protected int					sizeX			= 768;
	protected int					sizeY			= 48;
	protected boolean				concatenate		= false;
	protected int					maxOnBits		= 256;
	protected float					distortion		= 0.0F;
	
	@Override
	public void configure(SDRProcessorConfig config) {
		if (config instanceof MergerConfig) {
			MergerConfig cfg = (MergerConfig) config;
			this.sizeX = cfg.sizeX;
			this.sizeY = cfg.sizeY;
			this.maxOnBits = cfg.maxOnBits;
			this.distortion = cfg.distortion;
			this.concatenate = cfg.concatenate;
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
	}
	
	@Override
	public Str setInput(SDR... sdrs) {
		Str err = new Str();
		outputs.clear();
		if (sdrs.length>0) {
			super.setInput(sdrs);
		} else {
			err.sb().append("At least one input SDR is required");
		}
		outputs.add(new SDR(sizeX, sizeY));
		return err;
	}

	@Override
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn, int threads) {
		CodeRunnerList mergeInputs = new CodeRunnerList(
			new RunCode() {
				@Override
				protected boolean run() {
					boolean keyValue = false;
					
					int offset = 0;
					for (SDR input: inputs) {
						if (input.onBits()>0) {
							if (concatenate) {
								outputs.get(MERGED_OUTPUT).concat(input,offset);
								offset += input.length();
							} else {
								outputs.get(MERGED_OUTPUT).or(input);
							}
						}
						if (input instanceof KeyValueSDR) {
							keyValue = true;
						}
					}
					if (maxOnBits>0) {
						outputs.get(MERGED_OUTPUT).subsample(maxOnBits);
					}
					outputs.get(MERGED_OUTPUT).distort(distortion);
					
					if (keyValue) {
						KeyValueSDR output = new KeyValueSDR(outputs.get(MERGED_OUTPUT));
						for (SDR input: inputs) {
							if (input instanceof KeyValueSDR) {
								KeyValueSDR kvSdr = (KeyValueSDR) input;
								for (String key: kvSdr.getValueKeys()) {
									output.put(key, kvSdr.get(key));
								}
							}
						}
						outputs.set(MERGED_OUTPUT, output);
					}
					return true;
				}
			}
		);
		runnerChain.add(mergeInputs);
		addIncrementProcessedToProcessorChain(runnerChain);
	}
}
