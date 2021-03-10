package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.neural.ProcessorIO;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.sp.SpConfig;
import nl.zeesoft.zdk.neural.sp.SpatialPooler;

public class TestSpatialPooler {
	private static TestSpatialPooler	self	= new TestSpatialPooler();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		SpConfig config = new SpConfig();
		SpatialPooler sp = new SpatialPooler();
		
		sp.initialize(config);

		assert sp.activationHistory.length == 100;
		assert sp.activationHistory.capacity == 1000;
		
		assert sp.connections.config == sp.config;
		assert sp.connections.volume() == 100;
		assert sp.connections.data[0][0][0] instanceof Matrix;
		Matrix permanences = (Matrix) sp.connections.data[0][0][0];
		assert permanences.volume() == 16;
		assert permanences.data[0][0][0] == null;

		assert sp.boostFactors.config == sp.config;
		assert sp.boostFactors.volume() == 100;
		assert sp.boostFactors.data[0][0][0] instanceof Float;
		assert (float)sp.boostFactors.data[0][0][0] == 1F;
		
		assert sp.activations.config == sp.config;
		assert sp.activations.volume() == 100;
		assert sp.activations.data[0][0][0] == null;

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

		ProcessorIO io = new ProcessorIO();
		sp.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("SpatialPooler requires at least one input SDR");
		
		io = new ProcessorIO();
		io.inputs.add(new Sdr(17));
		sp.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("SpatialPooler input SDR may not be longer than 16");
		
		Sdr input = new Sdr(16);
		input.setBit(0, true);
		input.setBit(1, true);
		input.setBit(2, true);
		input.setBit(3, true);
		
		io = new ProcessorIO();
		io.inputs.add(input);
	
		Matrix before = sp.connections.copy(self);
		assert before.equals(sp.connections);
		
		sp.boostFactors.data[0][0][0] = 1.00001F;
		sp.processIO(io);
		assert sp.activations.data[0][0][0] instanceof Float;
		assert (float)sp.activations.data[0][0][0] >= 0;
		assert io.outputs.size()>0;
		Sdr output = io.outputs.get(0);
		assert output.length == 100;
		assert output.onBits.size() == 2;
		
		assert !sp.connections.equals(before);
	}
}
