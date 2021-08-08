package nl.zeesoft.zdk.neural.processor.cl;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.processor.ConfigurableIO;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class ClConfig implements ConfigurableIO {
	public Size		size			= Cells.getDefaultSize();
	public int		maxOnBits		= Cells.getDefaultOnBits() * Cells.getDefaultSize().z;
	public int		predictStep		= 1;
	public int		maxCount		= 512;
	
	public ClConfig copy() {
		ClConfig r = new ClConfig();
		r.size = size.copy();
		r.maxOnBits = maxOnBits;
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
