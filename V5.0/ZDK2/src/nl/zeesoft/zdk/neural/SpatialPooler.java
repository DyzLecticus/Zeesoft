package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;

public class SpatialPooler {
	// Configuration
	public SpatialPoolerConfig			config						= null;
	
	// State
	public List<Position>				activeInputPositions		= new ArrayList<Position>();
	public SpatialPoolerConnections		connections					= new SpatialPoolerConnections();
	public Matrix						activations					= new Matrix();
	public SdrHistory					activationHistory			= new SdrHistory();
	public float						averageGlobalActivation		= 0.0F;
	public Matrix						boostFactors				= new Matrix();

	public void initialize(SpatialPoolerConfig config) {
		this.config = config.copy();
		connections.initialize(config.outputSize);
		connections.initialize(this, config);
		
		activations.initialize(config.outputSize);
		activations.setValue(this, 0F);
		
		activationHistory.initialize(config.outputSize.volume());
		activationHistory.capacity = config.activationHistorySize;
		
		boostFactors.initialize(config.outputSize);
		boostFactors.setValue(this, 1F);
	}
	
	public void resetConnections() {
		connections.reset(this, config);
	}
}
