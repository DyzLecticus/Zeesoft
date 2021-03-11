package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.neural.ProcessorIO;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.sp.SpConfig;
import nl.zeesoft.zdk.neural.sp.SpConnections;
import nl.zeesoft.zdk.neural.sp.SpatialPooler;

public class TestSpatialPooler {
	private static TestSpatialPooler	self	= new TestSpatialPooler();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		SpConfig config = new SpConfig();
		SpatialPooler sp = new SpatialPooler();
		
		ProcessorIO io = new ProcessorIO();
		sp.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("SpatialPooler is not initialized");

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

		io = new ProcessorIO();
		sp.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("SpatialPooler connections are not initialized");

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

		float permanence = SpConnections.getAdjustedPermanence(0.999F,true,0.1F,0.1F);
		assert permanence == 1F;
		permanence = SpConnections.getAdjustedPermanence(0.001F,false,0.1F,0.1F);
		assert permanence == 0F;
		
		io = new ProcessorIO();
		sp.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("SpatialPooler requires at least one input SDR");
		
		io = new ProcessorIO(new Sdr(17));
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
			
		Matrix connectionsBefore = sp.connections.copy(self);
		assert connectionsBefore.equals(sp.connections);
		sp.boostFactors.data[0][0][0] = 1.00001F;
		sp.processIO(io);
		assert sp.activations.data[0][0][0] instanceof Float;
		assert (float)sp.activations.data[0][0][0] >= 0;
		assert io.outputs.size()>0;
		Sdr output = io.outputs.get(0);
		assert output.length == 100;
		assert output.onBits.size() == 2;
		assert !sp.connections.equals(connectionsBefore);
		
		input = new Sdr(16);
		input.setBit(4, true);
		input.setBit(5, true);
		input.setBit(6, true);
		input.setBit(7, true);
		
		connectionsBefore = sp.connections.copy(self);
		Matrix activationsBefore = sp.activations.copy(self);
		sp.config.learn = false;
		io = new ProcessorIO(input);
		sp.processIO(io);
		assert sp.connections.equals(connectionsBefore);
		assert !sp.activations.equals(activationsBefore);
		
		sp.connections.data[0][0][0] = null;
		io = new ProcessorIO(input);
		sp.processIO(io);
		assert io.error.equals("SpatialPooler connections are not initialized");

		sp.connections.size = null;
		io = new ProcessorIO(input);
		sp.processIO(io);
		assert io.error.equals("SpatialPooler connections are not initialized");

		sp.connections = null;
		io = new ProcessorIO(input);
		sp.processIO(io);
		assert io.error.equals("SpatialPooler connections are not initialized");

	}
}
