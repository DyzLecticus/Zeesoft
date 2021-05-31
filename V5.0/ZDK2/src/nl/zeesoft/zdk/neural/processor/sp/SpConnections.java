package nl.zeesoft.zdk.neural.processor.sp;

import java.util.List;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;

public class SpConnections extends Matrix {
	public SpConfig					config		= null;
	public SpConnectionResetter		resetter	= null;
	
	protected SpConnections(Object caller, Matrix matrix) {
		initialize(matrix.size);
		copyDataFrom(caller, matrix);
	}
	
	public SpConnections(Object caller, SpConfig config) {
		this.config = config;
		resetter = new SpConnectionResetter(config);
		initialize(config.outputSize);
		applyFunction(caller,getInitializeFunction());
	}
	
	public void reset(Object caller) {
		applyFunction(caller, resetter.getResetFunction(caller));
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
