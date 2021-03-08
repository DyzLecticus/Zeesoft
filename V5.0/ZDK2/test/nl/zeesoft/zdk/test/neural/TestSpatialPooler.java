package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.neural.SpatialPooler;
import nl.zeesoft.zdk.neural.SpatialPoolerConfig;

public class TestSpatialPooler {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		SpatialPoolerConfig config = new SpatialPoolerConfig();
		SpatialPooler sp = new SpatialPooler();
		
		sp.initialize(config);
		
		assert sp.connections.volume() == 100;
		assert sp.connections.data[0][0][0] instanceof Matrix;
		Matrix permanences = (Matrix) sp.connections.data[0][0][0];
		assert permanences.volume() == 16;
		assert permanences.data[0][0][0] == null;
		
		assert sp.activations.volume() == 100;
		assert sp.activations.data[0][0][0] instanceof Float;
		assert (float)sp.activations.data[0][0][0] == 0F;

		assert sp.activationHistory.length == 100;
		assert sp.activationHistory.capacity == 1000;

		assert sp.boostFactors.volume() == 100;
		assert sp.boostFactors.data[0][0][0] instanceof Float;
		assert (float)sp.boostFactors.data[0][0][0] == 1F;

		sp.resetConnections();
		assert permanences.data[0][0][0] != null;
		assert permanences.data[0][0][0] instanceof Float;
		assert (float)permanences.data[0][0][0] >= -1;
		assert (float)permanences.data[0][0][0] <= 1;
		assert (float)permanences.data[3][3][0] == -1;
		
		sp.initialize(config);
		sp.config.potentialRadius = 16;
		sp.resetConnections();
		assert (float)permanences.data[0][0][0] >= -1;
		assert (float)permanences.data[0][0][0] <= 1;
		
		sp.initialize(config);
		sp.config.potentialRadius = 0;
		sp.resetConnections();
		assert (float)permanences.data[0][0][0] >= -1;
		assert (float)permanences.data[0][0][0] <= 1;
	}
}
