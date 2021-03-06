package nl.zeesoft.zdk.neural.sp;

import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.Processor;
import nl.zeesoft.zdk.neural.ProcessorIO;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrHistory;

public class SpatialPooler implements Processor {
	public SpConfig			config						= null;
	public SdrHistory		activationHistory			= new SdrHistory();
	public SpConnections	connections					= null;
	public SpBoostFactors	boostFactors				= null;
	public SpActivations	activations					= null;
	public int				processed					= 0;

	public void initialize(SpConfig config) {
		this.config = config.copy();
		
		activationHistory.initialize(config.outputSize.volume());
		activationHistory.capacity = config.activationHistorySize;
		
		connections = new SpConnections(this, this.config);
		boostFactors = new SpBoostFactors(this, this.config, activationHistory);
		activations = new SpActivations(this, this.config, connections, boostFactors);
	}
	
	public void resetConnections() {
		connections.reset(this);
	}

	@Override
	public void processIO(ProcessorIO io) {
		if (isInitialized(io) && isValidIO(io)) {
			List<Position> activeInputPositions = io.inputs.get(0).getOnPositions(config.inputSize);
			activations.activate(this, activeInputPositions);
			List<Position> winners = activations.getWinners(this, config.outputOnBits);
			setOutput(io, winners);
			if (config.learn) {
				connections.adjustPermanences(this, activeInputPositions, winners);
			}
			activationHistory.push(io.outputs.get(0).copy());
			processed++;
			boostFactors.update(this, processed);
		}
	}
	
	protected boolean isInitialized(ProcessorIO io) {
		if (config==null) {
			io.error = this.getClass().getSimpleName() + " is not initialized";
		} else if (connections==null || !connections.isInitialized()) {
			io.error = this.getClass().getSimpleName() + " connections are not initialized";
		}
		return io.error.length()==0;
	}
	
	protected boolean isValidIO(ProcessorIO io) {
		if (io.inputs.size()==0) {
			io.error = this.getClass().getSimpleName() + " requires at least one input SDR";
		} else {
			Sdr input = io.inputs.get(0);
			if (input.length > config.inputSize.volume()) {
				io.error = this.getClass().getSimpleName() + " input SDR may not be longer than " + config.inputSize.volume();
			}
		}
		return io.error.length()==0;
	}
	
	protected void setOutput(ProcessorIO io, List<Position> winners) {
		Sdr output = new Sdr(config.outputSize.volume());
		output.setOnPositions(config.outputSize, winners);
		io.outputs.add(output);
	}
}
