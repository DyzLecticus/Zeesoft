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
	protected int					maxOnBits					= 256;

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
			this.maxOnBits = cfg.maxOnBits;
			
			this.valueKey = cfg.valueKey;
			this.predictSteps.addAll(cfg.predictSteps);
			this.maxCount = cfg.maxCount;
			
			if (maxCount<8) {
				maxCount = 8;
			}
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
			super.setInput(sdrs);
		} else {
			Logger.err(this, new Str("At least one input SDR is required"));
		}
		
		outputs.add(new KeyValueSDR());
	}
	
	@Override
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn, int threads) {
		runnerChain.add(getProcessInputsRunnerList());
		if (learn) {
			runnerChain.add(getAssociateBitsRunnerList());
		}
		runnerChain.add(getGeneratePredictionsRunnerList());
		addIncrementProcessedToProcessorChain(runnerChain);
	}

	@Override
	public Str toStr() {
		Str r = super.toStr();
		r.sb().append(processed);
		r.sb().append(OBJECT_SEPARATOR);
		int i = 0;
		for (ClassifierStep step: classifierSteps) {
			if (i>0) {
				r.sb().append("\n");
			}
			r.sb().append(step.toStr().sb());
			i++;
		}
		r.sb().append(OBJECT_SEPARATOR);
		r.sb().append(activationHistory.toStr());
		return r;
	}

	@Override
	public void fromStr(Str str) {
		List<Str> objects = str.split(OBJECT_SEPARATOR);
		if (objects.size()>=3) {
			processed = Integer.parseInt(objects.get(0).toString());
			
			classifierSteps.clear();
			List<Str> steps = objects.get(1).split("\n");
			for (Str step: steps) {
				ClassifierStep cs = new ClassifierStep(0,valueKey,maxCount,activationHistory);
				cs.fromStr(step);
				classifierSteps.add(cs);
			}
			
			activationHistory.clear();
			activationHistory.fromStr(objects.get(2));
		}
	}

	protected CodeRunnerList getProcessInputsRunnerList() {
		CodeRunnerList r = new CodeRunnerList(new RunCode() {
			@Override
			protected boolean run() {
				SDR combinedInput = new SDR(sizeX, sizeY);
				for (SDR input: inputs) {
					if (input instanceof KeyValueSDR) {
						value = ((KeyValueSDR) input).get(valueKey); 
					}
					if (input.sizeX()==sizeX && input.sizeY()==sizeY) {
						combinedInput.or(input);
					}
				}
				if (maxOnBits>0) {
					combinedInput.subsample(maxOnBits);
				}
				activationHistory.addSDR(combinedInput);
				return true;
			}
		});
		return r;
	}

	protected CodeRunnerList getAssociateBitsRunnerList() {
		CodeRunnerList r = new CodeRunnerList();
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
			r.add(code);
		}
		return r;
	}

	protected CodeRunnerList getGeneratePredictionsRunnerList() {
		CodeRunnerList r = new CodeRunnerList();
		for (ClassifierStep classifierStep: classifierSteps) {
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					generatePrediction((ClassifierStep) params[0]);
					return true;
				}
			};
			code.params[0] = classifierStep;
			r.add(code);
		}
		return r;
	}

	protected void generatePrediction(ClassifierStep step) {
		Classification classification = step.generatePrediction(inputs.get(0));
		lock.lock(this);
		((KeyValueSDR)outputs.get(CLASSIFICATION_OUTPUT)).put(CLASSIFICATION_VALUE_KEY + ":" + classification.step, classification);
		lock.unlock(this);
	}
}
