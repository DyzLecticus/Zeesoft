package nl.zeesoft.zdk.neural.sp;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;

public class SpConnections extends Matrix {
	public SpConfig	config	= null;
	
	public SpConnections(Object caller, SpConfig config) {
		this.config = config;
		initialize(config.outputSize);
		applyFunction(caller,getInitializeFunction());
	}
	
	public void reset(Object caller) {
		applyFunction(caller, getResetFunction(caller, config));
	}

	protected Function getInitializeFunction() {
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
	
	protected Function getResetFunction(Object myCaller, SpConfig config) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				Position position = (Position) param1;
				Matrix permanences = (Matrix) param2;
				permanences.applyFunction(myCaller,getResetPermanencesFunction(position));
				return permanences;
			}
		};
		return r;
	}
	
	protected Function getResetPermanencesFunction(Position position) {
		Function function = new Function() {
			@Override
			protected Object exec() {
				float r = -1;
				boolean inRange = true;
				if (config.potentialRadius > 0 &&
					config.potentialRadius < config.inputSize.volume()
					) {
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
