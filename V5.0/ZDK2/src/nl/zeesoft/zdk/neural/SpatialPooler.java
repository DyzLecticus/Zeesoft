package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;

public class SpatialPooler {
	// Configuration
	public SpatialPoolerConfig	config						= null;
	
	// State
	public List<Position>		activeInputPositions		= new ArrayList<Position>();
	public Matrix				connections					= new Matrix();
	public Matrix				activations					= new Matrix();
	public SdrHistory			activationHistory			= new SdrHistory();
	public float				averageGlobalActivation		= 0.0F;
	public Matrix				boostFactors				= new Matrix();

	public void initialize(SpatialPoolerConfig config) {
		this.config = config.copy();
		connections.initialize(config.outputSize);
		Function function = new Function() {
			@Override
			protected Object exec() {
				Matrix matrix = new Matrix();
				matrix.initialize(config.inputSize);
				return matrix;
			}
		};
		connections.applyFunction(this,function);
		activations.initialize(config.outputSize);
		activationHistory.initialize(config.outputSize.x * config.outputSize.y);
		activationHistory.capacity = config.activationHistorySize;
		boostFactors.initialize(config.outputSize);
	}
	
	public void resetConnections(Object caller) {
		connections.applyFunction(caller, getResetConnectionsFunction(caller));
	}
	
	public Function getResetConnectionsFunction(Object caller) {
		return SpatialPoolerConnections.getResetConnectionsFunction(caller, config);
	}
}
