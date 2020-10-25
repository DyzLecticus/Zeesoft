package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;

/**
 * This class can be used to configure a Classifier before initialization.
 * 
 * Please note; This implementation will 'forget' old classifications by default (see maxCount)
 * 
 * Configurable properties;
 * - sizeX, sizeY; Merged output SDR dimensions (input SDRs that do not match are ignored for merge) 
 * - maxOnBits; Optional maximum number of on bits in the merged output (uses sub sampling) 
 * - valueKey; Value key to look for in the input KeyValueSDRs
 * - predictSteps; List of steps to classify/predict (step 0 = classify, step > 0 = predict)
 * - maxCount; Optional maximum count of a step bit value count
 *     If a step bit reaches this maximum, all bit value counts of the step are divided by two.
 *     When when a value has only a single count it will be removed.
 *     When a step bit has no value counts it will be removed.  
 */
public class ClassifierConfig extends SDRProcessorConfig{
	public int				sizeX			= 768;
	public int				sizeY			= 48;
	public int				maxOnBits		= 256;

	public String			valueKey		= Classifier.DEFAULT_VALUE_KEY;
	public List<Integer>	predictSteps	= new ArrayList<Integer>();
	public int				maxCount		= 40;
}
