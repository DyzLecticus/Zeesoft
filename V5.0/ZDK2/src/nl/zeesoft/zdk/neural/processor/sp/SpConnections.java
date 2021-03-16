package nl.zeesoft.zdk.neural.processor.sp;

import java.util.List;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellConfig;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.model.Segment;
import nl.zeesoft.zdk.neural.model.Synapse;

public class SpConnections extends Matrix {
	public SpConfig	config	= null;
	
	public SpConnections(Object caller, SpConfig config) {
		this.config = config;
		initialize(config.outputSize);
		applyFunction(caller,getInitializeFunction());
	}
	
	public Cells toCells(Object caller) {
		CellConfig cellConfig = new CellConfig();
		cellConfig.size = config.outputSize;
		cellConfig.permanenceThreshold = config.permanenceThreshold;
		cellConfig.permanenceDecrement = config.permanenceDecrement;
		cellConfig.permanenceIncrement = config.permanenceIncrement;
		Cells cells = new Cells(caller, cellConfig);
		cells.applyFunction(caller, this.getCreateProximalSegmentsFunction(caller));
		return cells;
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
	
	protected Function getCreateProximalSegmentsFunction(Object myCaller) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				Matrix permanences = (Matrix) getValue((Position)param1);
				Cell cell = (Cell) param2;
				Segment segment = new Segment();
				permanences.applyFunction(myCaller, getCreateSynapsesFunction(segment));
				if (segment.synapses.size()>0) {
					cell.proximalSegments.add(segment);
				}
				return param2;
			}
		};
		return r;
	}
	
	protected Function getCreateSynapsesFunction(Segment segment) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				if (param2!=null) {
					float permanence = (float) param2;
					if (permanence >= 0) {
						Synapse synapse = new Synapse();
						synapse.connectTo = (Position) param1;
						synapse.permanence = permanence;
						segment.synapses.add(synapse);
					}
				}
				return param2;
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
