package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A Classifier is used to learn, classify and predict specific values and/or labels based on the temporal memory cell activation pattern.
 * The Classification objects it produces are attached to output DateTimeSDR objects using the keyValues property.
 * One output DateTimeSDR with Classification is produced for each of the configured prediction steps.
 */
public class Classifier extends ProcessorObject {
	public static final String			CLASSIFICATION_KEY		= "CLASSIFICATION";
	
	protected List<StepsClassifier>		classifiers				= new ArrayList<StepsClassifier>();
	protected int						maxSteps				= 0;
	protected List<SDR>					activationHistory		= new ArrayList<SDR>();
	
	protected DateTimeSDR				inputSDR				= null;
	protected List<DateTimeSDR>			classifierSDRs			= new ArrayList<DateTimeSDR>();
	
	public Classifier(ClassifierConfig config) {
		super(config);
		for (Integer steps: getConfig().predictSteps) {
			classifiers.add(new StepsClassifier(config,activationHistory,steps));
			if (steps>maxSteps) {
				maxSteps = steps;
			}
		}
	}
	
	@Override
	public ClassifierConfig getConfig() {
		return (ClassifierConfig) super.getConfig();
	}
	
	/**
	 * Returns a description of this classifier.
	 * 
	 * @return A description of this classifier
	 */
	@Override
	public ZStringBuilder getDescription() {
		return getConfig().getDescription();
	}
	
	@Override
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		for (StepsClassifier classifier: classifiers) {
			if (r.length()>0) {
				r.append("@");
			}
			r.append(classifier.toStringBuilder());
		}
		return r;
	}

	@Override
	public void fromStringBuilder(ZStringBuilder str) {
		List<ZStringBuilder> elems = str.split("@");
		for (ZStringBuilder elem: elems) {
			StepsClassifier classifier = new StepsClassifier(getConfig(),activationHistory,0);
			classifier.fromStringBuilder(elem);
			classifiers.add(classifier);
		}
	}
	
	@Override
	public void destroy() {
		for (StepsClassifier classifier: classifiers) {
			for (StepsClassifierBit bit: classifier.bits.values()) {
				bit.labelCounts.clear();
				bit.valueCounts.clear();
				bit.config = null;
			}
			classifier.bits.clear();
			classifier.config = null;
		}
		classifiers.clear();
		activationHistory.clear();
		classifierSDRs.clear();
	}
	
	/**
	 * Returns a list classification SDRs for a certain cell activation SDR.
	 * The ability to create classifications depends on the learned associations between input SDRs and corresponding cell activation SDRs.
	 * 
	 * @param activationSDR The temporal memory cell activation SDR
	 * @param inputSDR The input SDR used to learn associations between activations and values (and/or labels)
	 * @param learn Indicates the classifier should learn the associations
	 * @return A list of classification SDRs
	 */
	public List<SDR> getSDRsForInput(SDR activationSDR,SDR inputSDR,boolean learn) {
		List<SDR> context = new ArrayList<SDR>();
		context.add(inputSDR);
		return getSDRsForInput(activationSDR, context, learn);
	}
	
	@Override
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		inputSDR = null;
		if (context.size()>0 && context.get(0) instanceof DateTimeSDR) {
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
