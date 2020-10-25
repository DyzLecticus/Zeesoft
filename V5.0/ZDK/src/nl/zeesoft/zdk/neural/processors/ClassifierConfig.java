package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;

public class ClassifierConfig extends SDRProcessorConfig{
	public int				sizeX			= 768;
	public int				sizeY			= 48;
	public int				maxOnBits		= 256;

	public String			valueKey		= Classifier.DEFAULT_VALUE_KEY;
	public List<Integer>	predictSteps	= new ArrayList<Integer>();
	public int				maxCount		= 40;
}
