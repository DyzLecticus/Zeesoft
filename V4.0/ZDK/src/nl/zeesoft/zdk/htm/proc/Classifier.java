package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A Classifier is used to learn, classify and predict specific values and/or labels based on the temporal memory cell activation pattern.
 * The Classification objects it produces are attached to output DateTimeSDR objects using the keyValues property.
 * One output DateTimeSDR with a Classification is produced for each of the configured prediction steps.
 */
public class Classifier extends ProcessorObject {
	public static final String			CLASSIFICATION_KEY		= "CLASSIFICATION";
	
	protected List<StepsClassifier>		classifiers				= new ArrayList<StepsClassifier>();
	protected int						maxSteps				= 0;
	protected List<SDR>					activationHistory		= new ArrayList<SDR>();
	
	protected DateTimeSDR				inputSDR				= null;
	protected List<DateTimeSDR>			classifierSDRs			= new ArrayList<DateTimeSDR>();
	
	protected int						classifyMaxSteps		= 1;
	
	public Classifier(ClassifierConfig config) {
		super(config);
		for (Integer steps: getConfig().predictSteps) {
			classifiers.add(new StepsClassifier(config,activationHistory,steps));
			if (steps>maxSteps) {
				maxSteps = steps;
			}
		}
	}

	/**
	 * Sets the maximum number of steps for which classifications will be produced.
	 * Use -1 to turn off all classifications.
	 * Use 0 to turn off all predictions.
	 * 
	 * @param classifyMaxSteps The maximum number of classification steps
	 */
	public void setClassifyMaxSteps(int classifyMaxSteps) {
		this.classifyMaxSteps = classifyMaxSteps;
	}
	
	@Override
	public ClassifierConfig getConfig() {
		return (ClassifierConfig) super.getConfig();
	}
	
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
	 * @param inputSDR The input DateTimeSDR used to learn associations between activations and values (and/or labels)
	 * @param learn Indicates the classifier should learn the associations
	 * @return A list of classification SDRs
	 */
	public List<SDR> getSDRsForInput(SDR activationSDR,DateTimeSDR inputSDR,boolean learn) {
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
		classifierSDRs.clear();
		if (input!=null && input.onBits()>0) {
			if (inputSDR!=null) {
				long start = 0;
				if (learn) {
					start = System.nanoTime();
					associateBits(input);
					logStatsValue("associateBits",System.nanoTime() - start);
				}
				if (classifyMaxSteps>=0) {
					start = System.nanoTime();
					r = generateClassifications(input);
					logStatsValue("generateClassifications",System.nanoTime() - start);
				}
			} else {
				r = new DateTimeSDR(input.length());
			}
		} else {
			if (input==null) {
				r = new DateTimeSDR(100);
			} else {
				r = new DateTimeSDR(input.length());
			}
		}
		return r;
	}

	protected void associateBits(SDR input) {
		for (StepsClassifier classifier: classifiers) {
			classifier.associateBits(inputSDR);
		}
	}

	protected DateTimeSDR generateClassifications(SDR input) {
		DateTimeSDR r = null;
		boolean first = true;
		for (StepsClassifier classifier: classifiers) {
			if (classifier.steps<=classifyMaxSteps) {
				DateTimeSDR classificationSDR = classifier.getClassificationSDRForActivationSDR(input,inputSDR);
				if (classificationSDR!=null) {
					if (first) {
						r = classificationSDR;
						first = false;
					} else {
						classifierSDRs.add(classificationSDR);
					}
				}
			}
		}
		return r;
	}
}
