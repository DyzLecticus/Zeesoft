package nl.zeesoft.zdk.neural.processor.sp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.MatrixExecutor;
import nl.zeesoft.zdk.matrix.Position;

public class SpActivations extends Matrix {
	public SpConfig			config			= null;
	public SpConnections	connections		= null;
	public SpBoostFactors	boostFactors	= null;
	public Executor			executor		= null;
	
	protected SpActivations(Object caller, Matrix matrix) {
		initialize(matrix.size);
		copyDataFrom(caller, matrix);
	}
	
	public SpActivations(
		Object caller,
		SpConfig config,
		SpConnections connections,
		SpBoostFactors boostFactors,
		Executor executor
		) {
		this.config = config;
		this.connections = connections;
		this.boostFactors = boostFactors;
		this.executor = executor;
		initialize(config.outputSize);
	}
	
	public void activate(Object caller, List<Position> activeInputPositions) {
		MatrixExecutor exec = new MatrixExecutor(this, executor) {
			@Override
			protected Function getFunctionForWorker() {
				return getActivateFunction(activeInputPositions);
			}
		};
		exec.execute(caller, 1000);
	}

	public List<Position> getWinners(Object caller, int limit) {
		List<Position> r = new ArrayList<Position>();
		SortedMap<Float,List<Position>> sorted = getPositionsByValue(caller);
		for (Entry<Float,List<Position>> entry: sorted.entrySet()) {
			List<Position> columns = entry.getValue();
			int size = columns.size();
			for (int i = 0; i < size; i++) {
				Position add = columns.remove(Rand.getRandomInt(0,columns.size()-1));
				r.add(0,add);
			}
		}
		Util.applySizeLimitToList(r, limit);
		return r;
	}

	protected Function getActivateFunction(List<Position> activeInputPositions) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				Position position = (Position) param1;
				float activation = activateColumnPosition(position, activeInputPositions);
				float factor = (float)boostFactors.getValue(position);
				if (factor!=1F) {
					activation = activation * factor;
				}
				return activation;
			}
		};
		return r;
	}
	
	protected float activateColumnPosition(Position position, List<Position> activeInputPositions) {
		float r = 0;
		for (Position input: activeInputPositions) {
			float permanence = (float)((Matrix)connections.getValue(position)).getValue(input);
			if (permanence>config.permanenceThreshold) {
				r = r + 1F; 
			}
		}
		return r;
	}
	
	protected SortedMap<Float,List<Position>> getPositionsByValue(Object caller) {
		SortedMap<Float,List<Position>> r = new TreeMap<Float,List<Position>>();
		Function function = new Function() {
			@Override
			protected Object exec() {
				float val = (float) param2;
				List<Position> positions = r.get((float) param2);
				if (positions==null) {
					positions = new ArrayList<Position>();
					r.put(val,positions);
				}
				positions.add((Position) param1);
				return param2;
			}
		};
		applyFunction(caller, function);
		return r;
	}
}
