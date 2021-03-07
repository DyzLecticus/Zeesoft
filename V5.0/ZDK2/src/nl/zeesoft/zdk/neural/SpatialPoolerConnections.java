package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;

public class SpatialPoolerConnections extends Matrix {
	public void initialize(Object caller, SpatialPoolerConfig config) {
		applyFunction(caller,getInitializeFunction(config));
	}
	
	public void reset(Object caller, SpatialPoolerConfig config) {
		applyFunction(caller, getResetFunction(caller, config));
	}

	protected static Function getInitializeFunction(SpatialPoolerConfig config) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				Matrix permanences = new Matrix();
				permanences.initialize(config.inputSize);
				return permanences;
			}
		};
		return r;
	}
	
	protected static Function getResetFunction(Object myCaller, SpatialPoolerConfig config) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				Matrix permanences = (Matrix) param2;
				permanences.applyFunction(myCaller,getResetPermanencesFunction(config));
				return permanences;
			}
		};
		return r;
	}
	
	protected static Function getResetPermanencesFunction(SpatialPoolerConfig config) {
		Function function = new Function() {
			@Override
			protected Object exec() {
				float r = -1;
				// TODO determine if target is in range
				boolean inRange = true;
				if (inRange && Rand.getRandomFloat(0, 1) < config.potentialConnections) {
					if (Rand.getRandomInt(0, 1) == 1) {
						r = Rand.getRandomFloat(config.permanenceThreshold, 1);
					} else {
						r = Rand.getRandomFloat(0, config.permanenceThreshold);
					}
				}
				return r;
			}
		};
		return function;
	}
}
