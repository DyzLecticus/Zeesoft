package nl.zeesoft.zdk.neural.sp;

import nl.zeesoft.zdk.matrix.Matrix;

public class SpBoostFactors extends Matrix {
	public SpConfig	config	= null;
	
	public SpBoostFactors(Object caller, SpConfig config) {
		this.config = config;
		initialize(config.outputSize);
		setValue(caller, 1F);
	}
}
