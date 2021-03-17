package nl.zeesoft.zdk.neural.processor.tm;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.model.CellConfig;
import nl.zeesoft.zdk.neural.processor.ConfigurableIO;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class TmConfig extends CellConfig implements ConfigurableIO {
	public boolean	learn	= true;
	
	public TmConfig copy() {
		TmConfig r = new TmConfig();
		r.copyFrom(this);
		r.learn = learn;
		return r;
	}
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = new InputOutputConfig();
		r.addInput("ActiveColumns", size.x * size.y);
		r.addInput("ActiveApicalCells", size.volume());
		r.addOutput("ActiveCells", size);
		r.addOutput("BurstingColumns", new Size(size.x,size.y));
		r.addOutput("PredictiveCells", size);
		r.addOutput("WinnerCells", size);
		return r;
	}
}
