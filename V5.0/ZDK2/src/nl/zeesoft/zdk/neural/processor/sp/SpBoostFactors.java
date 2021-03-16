package nl.zeesoft.zdk.neural.processor.sp;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;
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

	public void update(Object caller, int processed) {
		if (processed % config.boostFactorPeriod == 0) {
			float averageGlobalActivation = activationHistory.getTotalAverage();
			applyFunction(caller, getUpdateBoostFactorsRunnerList(averageGlobalActivation));
		}
	}
	
	protected Function getUpdateBoostFactorsRunnerList(float averageGlobalActivation) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				int index = config.outputSize.getIndexForPosition((Position) param1);
				float average = activationHistory.getAverage(index);
				return getBoostFactor(average, averageGlobalActivation, config.boostStrength);
			}
		};
		return r;
	}
	
	public static float getBoostFactor(float average, float averageGlobalActivation, float boostStrength) {
		float r = 0;
		if (average!=averageGlobalActivation && boostStrength>1) {
			r = (float) Math.exp((float)boostStrength * - 1 * (average - averageGlobalActivation));
		} else {
			r = 1;
		}
		return r;
		
	}
}
