package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;

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
				Position position = (Position) param1;
				Matrix permanences = (Matrix) param2;
				permanences.applyFunction(myCaller,getResetPermanencesFunction(position,config));
				return permanences;
			}
		};
		return r;
	}
	
	protected static Function getResetPermanencesFunction(Position position,SpatialPoolerConfig config) {
		Function function = new Function() {
			@Override
			protected Object exec() {
				float r = -1;
				boolean inRange = true;
				if (config.potentialRadius> 0 && config.potentialRadius < config.inputSize.volume()) {
					Position projected = config.outputSize.projectPositionOn(position, config.inputSize);
					Position other = (Position) param1;
					inRange = projected.getDistance(other) < config.potentialRadius;
				}
				if (inRange && Rand.getRandomFloat(0, 1) < config.potentialConnections) {
					r = Rand.getRandomFloat(0, 1);
				}
				return r;
			}
		};
		return function;
	}
}
