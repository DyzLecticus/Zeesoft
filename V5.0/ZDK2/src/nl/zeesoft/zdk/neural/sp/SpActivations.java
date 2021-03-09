package nl.zeesoft.zdk.neural.sp;

import java.util.List;

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
}
