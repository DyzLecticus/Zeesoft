package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.neural.SpatialPooler;
import nl.zeesoft.zdk.neural.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.SpatialPoolerConnections;

public class TestSpatialPooler {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		assert new SpatialPoolerConnections() != null;
		
		SpatialPoolerConfig config = new SpatialPoolerConfig();
		SpatialPooler sp = new SpatialPooler();
		
		sp.initialize(config);
		
		assert sp.connections.volume() == 100;
		assert sp.connections.data[0][0][0] instanceof Matrix;
		Matrix permanences = (Matrix) sp.connections.data[0][0][0];
		assert permanences.volume() == 16;
		assert permanences.data[0][0][0] == null;
		
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
