package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;

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
	
	public static final String		CLASSIFICATION_VALUE_KEY	= "classification";
	public static final String		ACCURACY_VALUE_KEY			= "accuracy";
	public static final String		ACCURACY_TREND_VALUE_KEY	= "accuracyTrend";
	
	protected Lock					lock						= new Lock();
	
	// Configuration
	protected int					sizeX						= 768;
	protected int					sizeY						= 48;
	protected int					maxOnBits					= 256;

	protected String				valueKey					= KeyValueSDR.DEFAULT_VALUE_KEY;
	protected List<Integer>			predictSteps				= new ArrayList<Integer>();
	protected int					maxCount					= 40;
	
	protected boolean				logPredictionAccuracy		= false;
	protected int					accuracyHistorySize			= 100;
	protected int					accuracyTrendSize			= 10;
	
	// State
	protected List<ClassifierStep>	classifierSteps				= new ArrayList<ClassifierStep>();
	protected SDRHistory			activationHistory			= null;
	protected Object				value						= null;

	protected Object				predictedValue				= null;
	protected List<Float>			accuracyHistory				= new ArrayList<Float>();
	
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
			
			this.logPredictionAccuracy = cfg.logPredictionAccuracy;
			this.accuracyHistorySize = cfg.accuracyHistorySize;
			
			if (maxCount < 8) {
				maxCount = 8;
			}
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
		
		if (predictSteps.size()==0) {
			predictSteps.add(1);
		}
		
		if (logPredictionAccuracy && !predictSteps.contains(1)) {
			logPredictionAccuracy = false;
		}
		if (logPredictionAccuracy) {
			if (accuracyHistorySize<10) {
				accuracyHistorySize = 10;
			}
			if (accuracyTrendSize<0) {
				accuracyTrendSize = 1;
			} else if (accuracyTrendSize > accuracyHistorySize) {
				accuracyTrendSize = accuracyHistorySize / 2;
			}
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
	public Str setInput(SDR... sdrs) {
		Str err = new Str();
		outputs.clear();
		value = null;
		
		if (sdrs.length>0) {
			super.setInput(sdrs);
		} else {
			err.sb().append("At least one input SDR is required");
		}
		
		outputs.add(new KeyValueSDR());
		return err;
	}
	
	@Override
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn, int threads) {
		runnerChain.add(getProcessInputsRunnerList());
		if (logPredictionAccuracy) {
			runnerChain.add(getLogPredictionAccuracyRunnerList());
		}
		if (learn) {
			runnerChain.add(getAssociateBitsRunnerList(threads));
		}
		runnerChain.add(getGeneratePredictionsRunnerList(threads));
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
	
	protected CodeRunnerList getAssociateBitsRunnerList(int threads) {
		CodeRunnerList r = new CodeRunnerList();
		if (threads <= 0) {
			threads = 1;
		}
		if (threads>classifierSteps.size()) {
			threads = classifierSteps.size();
		}
		for (int t = 0; t < threads; t++) {
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					if (value!=null) {
						@SuppressWarnings("unchecked")
						List<ClassifierStep> steps = (List<ClassifierStep>) params[0];
						for (ClassifierStep step: steps) {
							step.associateBits(value);
						}
					}
					return true;
				}
			};
			List<ClassifierStep> steps = new ArrayList<ClassifierStep>();
			int s = 0;
			for (ClassifierStep step: classifierSteps) {
				if (s % threads==t) {
					steps.add(step);
				}
				s++;
			}
			code.params[0] = steps;
			r.add(code);
		}
		return r;
	}

	protected CodeRunnerList getLogPredictionAccuracyRunnerList() {
		CodeRunnerList r = new CodeRunnerList(new RunCode() {
			@Override
			protected boolean run() {
				if (value!=null && predictedValue!=null) {
					float accuracy = 0F;
					if (value==predictedValue || value.equals(predictedValue)) {
						accuracy = 1F;
					}
					accuracyHistory.add(0,accuracy);
					if (accuracyHistory.size()>accuracyHistorySize) {
						int remove = accuracyHistory.size() - accuracyHistorySize; 
						for (int i = 0; i < remove; i++) {
							accuracyHistory.remove(accuracyHistorySize);
						}
					}
					float total = 0F;
					float totalTrend = 0F;
					float totalTrendNum = 0F;
					int i = 0;
					for (Float acc: accuracyHistory) {
						total += acc;
						if (i < accuracyTrendSize) {
							totalTrend += acc;
							totalTrendNum += 1F;
						}
						i++;
					}
					float avg = total / (float) accuracyHistory.size();
					float avgTrend = totalTrend / totalTrendNum;
					((KeyValueSDR)outputs.get(CLASSIFICATION_OUTPUT)).put(ACCURACY_VALUE_KEY, avg);
					((KeyValueSDR)outputs.get(CLASSIFICATION_OUTPUT)).put(ACCURACY_TREND_VALUE_KEY, avgTrend);
				}
				return true;
			}
		});
		return r;
	}

	protected CodeRunnerList getGeneratePredictionsRunnerList(int threads) {
		CodeRunnerList r = new CodeRunnerList();
		if (threads <= 0) {
			threads = 1;
		}
		if (threads>classifierSteps.size()) {
			threads = classifierSteps.size();
		}
		for (int t = 0; t < threads; t++) {
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					@SuppressWarnings("unchecked")
					List<ClassifierStep> steps = (List<ClassifierStep>) params[0];
					for (ClassifierStep step: steps) {
						generatePrediction(step);
					}
					return true;
				}
			};
			List<ClassifierStep> steps = new ArrayList<ClassifierStep>();
			int s = 0;
			for (ClassifierStep step: classifierSteps) {
				if (s % threads==t) {
					steps.add(step);
				}
				s++;
			}
			code.params[0] = steps;
			r.add(code);
		}
		return r;
	}

	protected void generatePrediction(ClassifierStep step) {
		Classification classification = step.generatePrediction(inputs.get(0));
		lock.lock(this);
		((KeyValueSDR)outputs.get(CLASSIFICATION_OUTPUT)).put(CLASSIFICATION_VALUE_KEY + ":" + classification.step, classification);
		predictedValue = null;
		if (logPredictionAccuracy && classification.step==1 && classification.getMostCountedValues().size()==1) {
			predictedValue = classification.getMostCountedValues().get(0);
		}
		lock.unlock(this);
	}
}
