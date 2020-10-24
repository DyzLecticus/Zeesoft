package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.SDRHistory;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class Classifier extends SDRProcessor {
	public static final int			CLASSIFICATION_OUTPUT		= 0;
	
	public static final String		DEFAULT_VALUE_KEY			= "value";
	public static final String		CLASSIFICATION_VALUE_KEY	= "classification";
	
	protected Lock					lock						= new Lock();
	
	// Configuration
	protected int					sizeX						= 768;
	protected int					sizeY						= 48;

	protected String				valueKey					= DEFAULT_VALUE_KEY;
	protected List<Integer>			predictSteps				= new ArrayList<Integer>();
	protected int					maxCount					= 40;
	
	// State
	protected List<ClassifierStep>	classifierSteps				= new ArrayList<ClassifierStep>();
	protected SDRHistory			activationHistory			= null;
	protected Object				value						= null;
	
	@Override
	public void configure(SDRProcessorConfig config) {
		if (config instanceof ClassifierConfig) {
			ClassifierConfig cfg = (ClassifierConfig) config;
			
			predictSteps.clear();
			
			this.sizeX = cfg.sizeX;
			this.sizeY = cfg.sizeY;
			
			this.valueKey = cfg.valueKey;
			this.predictSteps.addAll(cfg.predictSteps);
			this.maxCount = cfg.maxCount;
		}
	}
		
	@Override
	public Str getDescription() {
		Str r = super.getDescription();
		r.sb().append(" (");
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
		
		if (predictSteps.size()==0) {
			predictSteps.add(1);
		}
		
		int maxSteps = 0;
		for (Integer predictStep: predictSteps) {
			if (predictStep>maxSteps) {
				maxSteps = predictStep;
			}
		}
		
		activationHistory = new SDRHistory(sizeX, sizeY, maxSteps + 1);
		for (Integer predictStep: predictSteps) {
			classifierSteps.add(new ClassifierStep(predictStep,valueKey,maxCount,activationHistory));
		}
	}
	
	@Override
	public void setInput(SDR... sdrs) {
		outputs.clear();
		value = null;
		
		if (sdrs.length>0) {
			SDR combinedInput = new SDR(sizeX, sizeY);
			for (int i = 0; i < sdrs.length; i++) {
				if (sdrs[i] instanceof KeyValueSDR) {
					value = ((KeyValueSDR) sdrs[i]).get(valueKey); 
				}
				if (sdrs[i].sizeX()==sizeX && sdrs[i].sizeY()==sizeY) {
					combinedInput.and(sdrs[i]);
				}
			}
			super.setInput(combinedInput);
			activationHistory.addSDR(combinedInput);
		} else {
			Logger.err(this, new Str("At least one input SDR is required"));
		}
		
		outputs.add(new KeyValueSDR());
	}

	@Override
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn) {
		
		CodeRunnerList associateBits = new CodeRunnerList();
		if (learn) {
			for (ClassifierStep classifierStep: classifierSteps) {
				RunCode code = new RunCode() {
					@Override
					protected boolean run() {
						if (value!=null) {
							ClassifierStep step = (ClassifierStep) params[0];
							step.associateBits(value);
						}
						return true;
					}
				};
				code.params[0] = classifierStep;
				associateBits.add(code);
			}
		}

		CodeRunnerList generatePrediction = new CodeRunnerList();
		for (ClassifierStep classifierStep: classifierSteps) {
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					generatePrediction((ClassifierStep) params[0]);
					return true;
				}
			};
			code.params[0] = classifierStep;
			generatePrediction.add(code);
		}
		
		if (learn) {
			runnerChain.add(associateBits);
		}
		runnerChain.add(generatePrediction);
		addIncrementProcessedToProcessorChain(runnerChain);
	}

	protected void generatePrediction(ClassifierStep step) {
		Classification classification = step.generatePrediction(inputs.get(0));
		lock.lock(this);
		((KeyValueSDR)outputs.get(CLASSIFICATION_OUTPUT)).put(CLASSIFICATION_VALUE_KEY + ":" + classification.step, classification);
		lock.unlock(this);
	}
}
