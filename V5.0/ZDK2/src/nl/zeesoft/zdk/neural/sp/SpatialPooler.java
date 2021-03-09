package nl.zeesoft.zdk.neural.sp;

import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.Processor;
import nl.zeesoft.zdk.neural.ProcessorIO;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrHistory;

public class SpatialPooler implements Processor {
	// Configuration
	public SpConfig			config						= null;
	
	// State
	public SpConnections	connections					= null;
	public SpBoostFactors	boostFactors				= null;
	public SpActivations	activations					= null;
	public SdrHistory		activationHistory			= new SdrHistory();
	public float			averageGlobalActivation		= 0.0F;

	public void initialize(SpConfig config) {
		this.config = config.copy();
		
		connections = new SpConnections(this, this.config);
		boostFactors = new SpBoostFactors(this, this.config);
		activations = new SpActivations(this, this.config, connections, boostFactors);
		
		activationHistory.initialize(config.outputSize.volume());
		activationHistory.capacity = config.activationHistorySize;
	}
	
	public void resetConnections() {
		connections.reset(this);
	}

	@Override
	public void processIO(ProcessorIO io) {
		if (io.inputs.size()==0) {
			io.error = this.getClass().getSimpleName() + " requires at least one input SDR";
		} else {
			Sdr input = io.inputs.get(0);
			if (input.length > config.inputSize.volume()) {
				io.error = this.getClass().getSimpleName() + " input SDR may not be longer than " + config.inputSize.volume();
			} else {
				List<Position> activeInputPositions	= input.getActivePositions(config.inputSize);
				activations.activate(this, activeInputPositions);
			}
		}
	}
}
