package nl.zeesoft.zdk.neural.sp;

import java.util.List;

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
		applyFunction(caller, getResetFunction(caller));
	}
	
	public boolean isInitialized() {
		return size!=null && data[0][0][0]!=null && ((Matrix)data[0][0][0]).data[0][0][0]!=null;
	}
	
	public void adjustPermanences(Object caller, List<Position> activeInputPositions, List<Position> winners) {
		for (Position winner: winners) {
			Matrix permanences = (Matrix) getValue(winner);
			permanences.applyFunction(caller, getAdjustPermanencesFunction(activeInputPositions));
		}
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
	
	protected Function getAdjustPermanencesFunction(List<Position> activeInputPositions) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				float permanence = (float) param2;
				boolean isActiveInput = activeInputPositions.contains((Position) param1);
				return getAdjustedPermanence(
					permanence, isActiveInput, config.permanenceIncrement, config.permanenceDecrement
				);
			}
		};
		return r;
	}
	
	public static float getAdjustedPermanence(float permanence, boolean isActiveInput, float increment, float decrement) {
		float r = permanence;
		if (r>=0) {
			if (isActiveInput) {
				r += increment;
				if (r > 1F) {
					r = 1F;
				}
			} else {
				r -= decrement;
				if (r < 0F) {
					r = 0F;
				}
			}
		}
		return r;
	}
}
