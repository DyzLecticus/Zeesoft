package nl.zeesoft.zdk.neural.sp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;

public class SpActivations extends Matrix {
	public SpConfig			config			= null;
	public SpConnections	connections		= null;
	public SpBoostFactors	boostFactors	= null;
	
	public SpActivations(Object caller, SpConfig config, SpConnections connections, SpBoostFactors	boostFactors) {
		this.config = config;
		this.connections = connections;
		this.boostFactors = boostFactors;
		initialize(config.outputSize);
	}
	
	public void activate(Object caller, List<Position> activeInputPositions) {
		applyFunction(caller,getActivateFunction(activeInputPositions));
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
		if (limit>0 && r.size() > limit) {
			int remove = r.size() - limit;
			for (int n = 0; n < remove; n++) {
				r.remove(limit);
			}
		}
		return r;
	}

	protected Function getActivateFunction(List<Position> activeInputPositions) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				float activation = 0;
				Position position = (Position) param1;
				for (Position input: activeInputPositions) {
					float permanence = (float)((Matrix)connections.getValue(position)).getValue(input);
					if (permanence>config.permanenceThreshold) {
						activation = activation + 1F; 
					}
				}
				float factor = (float)boostFactors.getValue(position);
				if (factor!=1F) {
					activation = activation * factor;
				}
				return activation;
			}
		};
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
