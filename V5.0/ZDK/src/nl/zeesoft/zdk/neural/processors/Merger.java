package nl.zeesoft.zdk.neural.processors;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.RunCode;

public class Merger extends SDRProcessor {
	public static final int			MERGED_OUTPUT	= 0;
	
	// Configuration
	protected int					sizeX			= 768;
	protected int					sizeY			= 48;
	protected int					maxOnBits		= 184;
	protected float					distortion		= 0.0F;
	
	@Override
	public void configure(SDRProcessorConfig config) {
		if (config instanceof MergerConfig) {
			MergerConfig cfg = (MergerConfig) config;
			
			this.sizeX = cfg.sizeX;
			this.sizeY = cfg.sizeY;
			this.maxOnBits = cfg.maxOnBits;
			this.distortion = cfg.distortion;
		}
	}
		
	@Override
	public Str getDescription() {
		Str r = super.getDescription();
		r.sb().append(" (?*? > ");
		r.sb().append(sizeX);
		r.sb().append("*");
		r.sb().append(sizeY);
		r.sb().append(")");
		return r;
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
	public void setInput(SDR... sdrs) {
		outputs.clear();
		
		if (sdrs.length>0) {
			super.setInput(sdrs);
		} else {
			Logger.err(this, new Str("At least one input SDR is required"));
		}
		
		outputs.add(new SDR(sizeX, sizeY));
	}

	@Override
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn) {
		CodeRunnerList mergeInputs = new CodeRunnerList(
			new RunCode() {
				@Override
				protected boolean run() {
					for (SDR input: inputs) {
						outputs.get(MERGED_OUTPUT).or(input);
					}
					if (maxOnBits>0) {
						outputs.get(MERGED_OUTPUT).subsample(maxOnBits);
					}
					outputs.get(MERGED_OUTPUT).distort(distortion);
					return true;
				}
			}
		);
		runnerChain.add(mergeInputs);
		addIncrementProcessedToProcessorChain(runnerChain);
	}
}
