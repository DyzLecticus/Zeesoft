package nl.zeesoft.zdk.neural.processor.mr;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.processor.ConfigurableIO;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class MrConfig implements ConfigurableIO {
	public Size		size			= Cells.getDefaultSize();

	public boolean	concatenate		= false;
	public int		maxOnBits		= 256;
	public float	distortion		= 0F;
	
	public MrConfig copy() {
		MrConfig r = new MrConfig();
		r.size = size.copy();
		r.concatenate = concatenate;
		r.maxOnBits = maxOnBits;
		r.distortion = distortion;
		return r;
	}
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = new InputOutputConfig();
		for (int i = 0; i < 9; i++) {
			r.addInput("MergeSDR" + (i + 1), size.volume());
		}
		r.addOutput("MergedSDR", size);
		return r;
	}
}
