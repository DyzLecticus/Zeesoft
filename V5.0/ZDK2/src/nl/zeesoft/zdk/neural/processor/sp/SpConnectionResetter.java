package nl.zeesoft.zdk.neural.processor.sp;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;

public class SpConnectionResetter {
	public SpConfig	config	= null;
	
	public SpConnectionResetter(SpConfig config) {
		this.config = config;
	}
	
	protected Function getResetFunction(Object myCaller) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				Matrix permanences = (Matrix) param2;
				permanences.applyFunction(myCaller,getResetPermanencesFunction((Position) param1));
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
					inRange = projected.getDistance((Position) param1) < config.potentialRadius;
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
