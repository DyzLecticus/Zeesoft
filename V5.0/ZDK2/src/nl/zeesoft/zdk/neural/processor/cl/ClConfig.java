package nl.zeesoft.zdk.neural.processor.cl;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.processor.ConfigurableIO;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class ClConfig implements ConfigurableIO {
	public Size		size			= new Size(48,48,16);
	public boolean	learn			= true;
	public int		maxOnBits		= 256;
	public String	valueName		= "value";
	public int		predictStep		= 1;
	public int		maxCount		= 512;
	
	public ClConfig copy() {
		ClConfig r = new ClConfig();
		r.size = size.copy();
		r.learn = learn;
		r.maxOnBits = maxOnBits;
		r.valueName = valueName;
		r.predictStep = predictStep;
		r.maxCount = maxCount;
		return r;
	}
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = new InputOutputConfig();
		r.addInput("AssociateSDR", size.volume());
		r.addOutput("AssociatedSDR", size);
		return r;
	}
}
