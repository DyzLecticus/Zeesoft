package nl.zeesoft.zdk.neural.processor.sp;

import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.processor.ConfigurableIO;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;

public class SpConfig implements ConfigurableIO {
	public Size		inputSize					= new Size(16,16);
	public Size		outputSize					= Cells.getDefaultSize().surface();
	public int		outputOnBits				= Cells.getDefaultOnBits();

	public float	permanenceThreshold			= 0.1F;
	public float	permanenceIncrement			= 0.05F;
	public float	permanenceDecrement			= 0.008F;
	
	public float	potentialConnections		= 0.85F;
	public float	potentialRadius				= 64;
	
	public int		activationHistorySize		= 1000;
	public int		boostFactorPeriod			= 10;
	public int		boostStrength				= 2;
	
	public SpConfig copy() {
		SpConfig r = new SpConfig();
		r.inputSize = inputSize.copy();
		r.outputSize = outputSize.copy();
		r.outputOnBits = outputOnBits;
		r.permanenceThreshold = permanenceThreshold;
		r.permanenceIncrement = permanenceIncrement;
		r.permanenceDecrement = permanenceDecrement;
		r.potentialConnections = potentialConnections;
		r.potentialRadius = potentialRadius;
		r.activationHistorySize = activationHistorySize;
		r.boostFactorPeriod = boostFactorPeriod;
		r.boostStrength = boostStrength;
		return r;
	}
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = new InputOutputConfig();
		r.addInput("EncodedSensor", inputSize.volume());
		r.addOutput("ActiveColumns", outputSize);
		return r;
	}
}
