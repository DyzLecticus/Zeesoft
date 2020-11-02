package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;

/**
 * This class can be used to configure a Classifier before initialization.
 */
public class ClassifierConfig extends SDRProcessorConfig{
	public static String 	DOCUMENTATION 	=
		"Please note; This implementation will 'forget' old classifications by default (see maxCount)  \n" +
		"  \n" +
		"Configurable properties;  \n" + 
		" * *sizeX*, *sizeY*; Merged output SDR dimensions (input SDRs that do not match are ignored for merge).  \n" + 
		" * *maxOnBits*; Optional maximum number of on bits in the merged input (uses sub sampling).  \n" +
		" * *valueKey*; Value key to look for in the input KeyValueSDRs.  \n" +
		" * *predictSteps*; List of steps to classify/predict;  \n" +
		"   Step 0 will classify the current input.  \n" +
		"   Steps greater than 0 will predict future input.  \n" +
		"   By default the next step will be predicted (predictSteps equals [1]).  \n" + 
		" * *maxCount*; Maximum count of a step bit value count (a minimum of 8 is enforced);  \n" +
		"   If a step bit reaches this maximum, all bit value counts of the step are divided by two.  \n" +
		"   When when a value has only a single count it will be removed.  \n" +
		"   When a step bit has no value counts it will be removed.  \n" +
		"";
	
	public int				sizeX			= 768;
	public int				sizeY			= 48;
	public int				maxOnBits		= 256;

	public String			valueKey		= KeyValueSDR.DEFAULT_VALUE_KEY;
	public List<Integer>	predictSteps	= new ArrayList<Integer>();
	public int				maxCount		= 40;

	@Override
	public Str getDescription() {
		Str r = super.getDescription();
		
		r.sb().append(": ");
		r.sb().append(sizeX);
		r.sb().append("*");
		r.sb().append(sizeY);

		r.sb().append("\n");
		r.sb().append("-> 0 = Classifications: 1*1");
		return r;
	}
}
