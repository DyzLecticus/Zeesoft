package nl.zeesoft.zdk.neural.processor.cl;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.processor.ConfigurableIO;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class ClConfig implements ConfigurableIO {
	public Size		size						= Cells.getDefaultSize();
	public int		maxOnBits					= Cells.getDefaultOnBits() * Cells.getDefaultSize().z;
	public int		predictStep					= 1;
	public float	alpha						= 0.001F;
	public int		initialCount				= 5;
	public int		avgPredictionTop			= 5;
	public float	avgPredictionStdDevFactor	= 2.0F;
	
	public ClConfig copy() {
		ClConfig r = new ClConfig();
		r.size = size.copy();
		r.maxOnBits = maxOnBits;
		r.predictStep = predictStep;
		r.alpha = alpha;
		r.initialCount = initialCount;
		r.avgPredictionTop = avgPredictionTop;
		r.avgPredictionStdDevFactor = avgPredictionStdDevFactor;
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
