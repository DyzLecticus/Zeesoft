package nl.zeesoft.zdk.neural.processor.sp;

import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellConfig;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.model.Segment;
import nl.zeesoft.zdk.neural.model.Synapse;

public class SpCells {
	public SpConfig			config		= null;
	public SpConnections	connections	= null;
	
	public SpCells(SpConfig config, SpConnections connections) {
		this.config = config;
		this.connections = connections;
	}
	
	public Cells toCells(Object caller) {
		CellConfig cellConfig = new CellConfig();
		cellConfig.size = config.outputSize;
		cellConfig.permanenceThreshold = config.permanenceThreshold;
		cellConfig.permanenceDecrement = config.permanenceDecrement;
		cellConfig.permanenceIncrement = config.permanenceIncrement;
		Cells cells = new Cells(caller, cellConfig);
		cells.applyFunction(caller, this.getCreateProximalSegmentsFunction(caller));
		return cells;
	}
	
	protected Function getCreateProximalSegmentsFunction(Object myCaller) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				Matrix permanences = (Matrix) connections.getValue((Position)param1);
				Cell cell = (Cell) param2;
				Segment segment = new Segment();
				permanences.applyFunction(myCaller, getCreateSynapsesFunction(segment));
				if (segment.synapses.size()>0) {
					cell.proximalSegments.add(segment);
				}
				return param2;
			}
		};
		return r;
	}
	
	protected Function getCreateSynapsesFunction(Segment segment) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				if (param2!=null) {
					float permanence = (float) param2;
					if (permanence >= 0) {
						Synapse synapse = new Synapse();
						synapse.connectTo = (Position) param1;
						synapse.permanence = permanence;
						segment.synapses.add(synapse);
					}
				}
				return param2;
			}
		};
		return r;
	}
}
