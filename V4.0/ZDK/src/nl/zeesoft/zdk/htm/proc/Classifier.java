package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class Classifier extends ProcessorObject {
	public static final String			CLASSIFICATION_KEY		= "classification";
	
	protected ClassifierConfig			config					= null;
	
	protected List<StepsClassifier>		classifiers				= new ArrayList<StepsClassifier>();
	protected int						maxSteps				= 0;
	protected List<SDR>					activationHistory		= new ArrayList<SDR>();
	
	protected DateTimeSDR				inputSDR				= null;
	protected List<DateTimeSDR>			classifierSDRs			= new ArrayList<DateTimeSDR>();
	
	public Classifier(ClassifierConfig config) {
		this.config = config;
		config.initialized = true;
		for (Integer steps: config.predictSteps) {
			classifiers.add(new StepsClassifier(config,activationHistory,steps));
			if (steps>maxSteps) {
				maxSteps = steps;
			}
		}
	}
	
	@Override
	public ZStringBuilder toStringBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromStringBuilder(ZStringBuilder str) {
		// TODO Auto-generated method stub
		
	}
	
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		inputSDR = null;
		if (context.get(0) instanceof DateTimeSDR) {
			inputSDR = (DateTimeSDR) context.get(0);
		}
		
		activationHistory.add(input);
		
		List<SDR> r = super.getSDRsForInput(input, context, learn);
		for (DateTimeSDR sdr: classifierSDRs) {
			r.add(sdr);
		}
		
		while (activationHistory.size()>maxSteps + 1) {
			activationHistory.remove(0);
		}
		return r;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input, boolean learn) {
		DateTimeSDR r = null;
		long start = 0;
		
		classifierSDRs.clear();
		if (inputSDR!=null) {
			start = System.nanoTime();
			r = generateClassifications(input,learn);
			logStatsValue("generateClassifications",System.nanoTime() - start);
		} else {
			r = new DateTimeSDR(input.length());
		}
		
		return r;
	}
	
	protected DateTimeSDR generateClassifications(SDR input,boolean learn) {
		DateTimeSDR r = null;
		int i = 0;
		for (StepsClassifier classifier: classifiers) {
			DateTimeSDR classificationSDR = classifier.getClassificationSDRForActivationSDR(input,inputSDR,learn);
			if (i==0) {
				r = classificationSDR;
			} else {
				classifierSDRs.add(classificationSDR);
			}
			i++;
		}
		return r;
	}
}
