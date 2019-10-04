package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class Classifier extends ProcessorObject {
	protected ClassifierConfig					config					= null;
	
	protected List<StepsClassifier>				classifiers				= new ArrayList<StepsClassifier>();
	
	protected List<DateTimeSDR>					classifierSDRs			= new ArrayList<DateTimeSDR>();
	
	protected DateTimeSDR						inputSDR				= null;
	
	public Classifier(ClassifierConfig config) {
		this.config = config;
		config.initialized = true;
		for (Integer steps: config.predictSteps) {
			classifiers.add(new StepsClassifier(config,steps));
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
		classifierSDRs.clear();
		List<SDR> r = super.getSDRsForInput(input, context, learn);
		for (DateTimeSDR sdr: classifierSDRs) {
			r.add(sdr);
		}
		return r;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input, boolean learn) {
		DateTimeSDR r = null;
		long start = 0;
		
		if (inputSDR!=null) {
			start = System.currentTimeMillis();
			r = generateClassifications(input);
			logStatsValue("generateClassifications",System.currentTimeMillis() - start);
		} else {
			r = new DateTimeSDR(input.length());
		}
		
		return r;
	}
	
	protected DateTimeSDR generateClassifications(SDR input) {
		DateTimeSDR r = null;
		int i = 0;
		for (StepsClassifier classifier: classifiers) {
			DateTimeSDR classificationSDR = classifier.getClassificationSDRForActivationSDR(input,inputSDR);
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
