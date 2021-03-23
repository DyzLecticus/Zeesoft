package nl.zeesoft.zdk.neural.processor.sp;

import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.SdrHistory;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.processor.CellsProcessor;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.LearningProcessor;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class SpatialPooler extends LearningProcessor implements CellsProcessor {
	public static final int		ENCODED_SENSOR_INPUT	= 0;
	
	public static final int		ACTIVE_COLUMNS_OUTPUT	= 0;
	
	public SpConfig				config					= null;
	public SdrHistory			activationHistory		= new SdrHistory();
	public SpConnections		connections				= null;
	public SpBoostFactors		boostFactors			= null;
	public SpActivations		activations				= null;
	public int					processed				= 0;

	public void initialize(SpConfig config) {
		this.config = config.copy();
		
		activationHistory.initialize(config.outputSize.volume());
		activationHistory.capacity = config.activationHistorySize;
		
		connections = new SpConnections(this, this.config);
		boostFactors = new SpBoostFactors(this, this.config, activationHistory);
		activations = new SpActivations(this, this.config, connections, boostFactors);
	}
	
	@Override
	public void reset() {
		if (connections!=null) {
			connections.reset(this);
		}
	}
	
	@Override
	public InputOutputConfig getInputOutputConfig() {
		InputOutputConfig r = super.getInputOutputConfig();
		if (config!=null) {
			r = config.getInputOutputConfig();
		}
		return r;
	}

	@Override
	public Cells getCells() {
		Cells r = null;
		if (connections!=null) {
			SpCells cells = new SpCells(config, connections);
			r = cells.toCells(this);
		}
		return r;
	}

	@Override
	protected void processValidIO(ProcessorIO io) {
		List<Position> activeInputPositions = io.inputs.get(0).getOnPositions(config.inputSize);
		activations.activate(this, activeInputPositions);
		List<Position> winners = activations.getWinners(this, config.outputOnBits);
		addOutput(io, winners);
		if (learn) {
			connections.adjustPermanences(this, activeInputPositions, winners);
		}
		activationHistory.push(io.outputs.get(0).copy());
		processed++;
		boostFactors.update(this, processed);
	}
	
	@Override
	protected boolean isInitialized(ProcessorIO io) {
		if (config==null) {
			io.error = this.getClass().getSimpleName() + " is not initialized";
		} else if (connections==null || !connections.isInitialized()) {
			io.error = this.getClass().getSimpleName() + " connections are not initialized";
		}
		return io.error.length() == 0;
	}
}
