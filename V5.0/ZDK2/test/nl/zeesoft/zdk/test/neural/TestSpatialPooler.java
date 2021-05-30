package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.model.CellStats;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.sp.SpBoostFactors;
import nl.zeesoft.zdk.neural.processor.sp.SpConfig;
import nl.zeesoft.zdk.neural.processor.sp.SpConnections;
import nl.zeesoft.zdk.neural.processor.sp.SpatialPooler;

public class TestSpatialPooler {
	private static TestSpatialPooler	self	= new TestSpatialPooler();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		ProcessorIO io = new ProcessorIO(new Sdr(10));
		MockProcessor processor = new MockProcessor();
		InputOutputConfig ioConfig = processor.getInputOutputConfig();
		assert ioConfig.inputs.size() == 1;
		assert ioConfig.inputs.get(0).name.equals("InputName");
		assert ioConfig.outputs.size() == 1;
		assert ioConfig.outputs.get(0).name.equals("OutputName");
		processor.processIO(io);
		processor.reset();
		assert processor.isLearn();
		processor.setLearn(false);
		assert !processor.isLearn();
		assert io.outputs.size() == 1;
		assert processor.toString().length() == 55;
		
		SpConfig config = new SpConfig();
		config.inputSize = new Size(4,4);
		config.outputSize = new Size(10,10);
		config.potentialRadius = 2;
		config.outputOnBits = 2;
		config.boostFactorPeriod = 2;
		
		ioConfig = config.getInputOutputConfig();
		assert ioConfig.inputs.size() == 1;
		assert ioConfig.inputs.get(SpatialPooler.ENCODED_SENSOR_INPUT).name.equals("EncodedSensor");
		assert ioConfig.outputs.size() == 1;
		assert ioConfig.outputs.get(SpatialPooler.ACTIVE_COLUMNS_OUTPUT).name.equals("ActiveColumns");
		assert ioConfig.toString().length() == 42;
		
		SpatialPooler sp = new SpatialPooler();
		assert sp.toString().length() == 14;
		assert sp.getCells() == null;
		sp.reset();
		
		io = new ProcessorIO();
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

		assert SpBoostFactors.getBoostFactor(2.0F, 2.0F, 1.5F)==1;
		assert SpBoostFactors.getBoostFactor(2.0F, 3.0F, 1.0F)==1;
		assert SpBoostFactors.getBoostFactor(2.0F, 3.0F, 1.5F)>1;
		assert SpBoostFactors.getBoostFactor(4.0F, 3.0F, 1.5F)<1;
		
		io = new ProcessorIO();
		sp.processIO(io);
		assert io.error.length() > 0;
		assert io.error.equals("SpatialPooler connections are not initialized");

		sp.reset();
		assert permanences.data[0][0][0] != null;
		assert permanences.data[0][0][0] instanceof Float;
		assert (float)permanences.data[0][0][0] >= -1;
		assert (float)permanences.data[0][0][0] <= 1;
		assert (float)permanences.data[3][3][0] == -1;
		
		sp.initialize(config);
		sp.config.potentialRadius = 16;
		CellStats stats = new CellStats(sp);
		assert stats.cells == 100;
		assert stats.proximalStats.segments == 0;
		
		sp.reset();
		assert (float)permanences.data[0][0][0] >= -1;
		assert (float)permanences.data[0][0][0] <= 1;
		
		sp.initialize(config);
		sp.config.potentialRadius = 0;
		sp.reset();
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
		assert io.error.equals("SpatialPooler input SDR 1 may not be longer than 16");
		
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
		assert io.error.length()==0;
		assert sp.activations.data[0][0][0] instanceof Float;
		assert (float)sp.activations.data[0][0][0] >= 0;
		assert io.outputs.size()>0;
		Sdr output = io.outputs.get(0);
		assert output.length == 100;
		assert output.onBits.size() == 2;
		assert !sp.connections.equals(connectionsBefore);
		assert sp.processed == 1;
		assert (float)sp.boostFactors.data[0][0][0] == 1.00001F;
		
		input = new Sdr(16);
		input.setBit(4, true);
		input.setBit(5, true);
		input.setBit(6, true);
		input.setBit(7, true);
		
		connectionsBefore = sp.connections.copy(self);
		Matrix activationsBefore = sp.activations.copy(self);
		sp.setLearn(false);
		io = new ProcessorIO(input);
		sp.processIO(io);
		assert sp.connections.equals(connectionsBefore);
		assert !sp.activations.equals(activationsBefore);
		assert sp.processed == 2;
		assert (float)sp.boostFactors.data[0][0][0] != 1.00001F;
		
		stats = new CellStats(sp);
		assert stats.cells == 100;
		assert stats.proximalStats.segments == 100;
		assert stats.proximalStats.synapses > 1000;
		assert stats.proximalStats.activeSynapses > 1000;
		
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
