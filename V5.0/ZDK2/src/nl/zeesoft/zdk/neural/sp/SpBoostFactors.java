package nl.zeesoft.zdk.neural.sp;

import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.neural.SdrHistory;

public class SpBoostFactors extends Matrix {
	public SpConfig		config				= null;
	public SdrHistory	activationHistory	= null;
	
	public SpBoostFactors(Object caller, SpConfig config, SdrHistory activationHistory) {
		this.config = config;
		this.activationHistory = activationHistory;
		initialize(config.outputSize);
		setValue(caller, 1F);
	}
}
