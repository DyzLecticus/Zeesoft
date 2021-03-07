package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;

public class SpatialPoolerConnections {
	public static Function getResetConnectionsFunction(Object myCaller, SpatialPoolerConfig config) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				Matrix inputConnections = (Matrix) param2;
				Function init = getInitialzeInputConnectionsFunction(config);
				inputConnections.applyFunction(myCaller,init);
				return inputConnections;
			}
		};
		return r;
	}
	
	protected static Function getInitialzeInputConnectionsFunction(SpatialPoolerConfig config) {
		Function function = new Function() {
			@Override
			protected Object exec() {
				float r = -1;
				// TODO determine initial value using randomness and config
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
