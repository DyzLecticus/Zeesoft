package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.neural.SpatialPooler;
import nl.zeesoft.zdk.neural.SpatialPoolerConfig;
import nl.zeesoft.zdk.neural.SpatialPoolerConnections;

public class TestSpatialPooler {
	private static TestSpatialPooler self = new TestSpatialPooler();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		assert new SpatialPoolerConnections() != null;
		
		SpatialPoolerConfig config = new SpatialPoolerConfig();
		SpatialPooler sp = new SpatialPooler();
		
		sp.initialize(config);
		
		assert sp.connections.size() == 100;
		assert sp.connections.data[0][0][0] instanceof Matrix;
		Matrix inputConnections = (Matrix) sp.connections.data[0][0][0];
		assert inputConnections.size() == 9;
		assert inputConnections.data[0][0][0] == null;
		
		sp.resetConnections(self);
		assert inputConnections.data[0][0][0] != null;
		assert inputConnections.data[0][0][0] instanceof Float;
		assert (float)inputConnections.data[0][0][0] >= -1;
		assert (float)inputConnections.data[0][0][0] <= 1;
	}
}
