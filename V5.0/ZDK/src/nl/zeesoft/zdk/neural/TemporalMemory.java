package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.grid.ColumnFunction;
import nl.zeesoft.zdk.grid.Grid;
import nl.zeesoft.zdk.grid.GridColumn;
import nl.zeesoft.zdk.grid.Position;
import nl.zeesoft.zdk.grid.SDR;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.RunCode;

public class TemporalMemory extends SDRProcessor {
	public int					sizeX					= 48;
	public int					sizeY					= 48;
	public int					sizeZ					= 16;

	public int					maxSegmentsPerCell		= 256;
	public int					maxSynapsesPerSegment	= 256;
	
	public float				permanenceThreshold		= 0.1F;
	public float				permanenceIncrement		= 0.05F;
	public float				permanenceDecrement		= 0.008F;

	public int					activationThreshold		= 13;
	public int					matchingThreshold		= 10;
	
	protected List<Position>	activeCellPositions		= new ArrayList<Position>();
	protected List<Position>	predictiveCellPositions	= new ArrayList<Position>();
	protected SDR				burstingColumns			= null;
	protected Grid				distalSegments			= null;

	@Override
	public void initialize(CodeRunnerList runnerList) {
		if (sizeX < 4) {
			sizeX = 4;
		}
		if (sizeY < 4) {
			sizeY = 4;
		}
		if (sizeZ < 4) {
			sizeZ = 4;
		}
		
		input = new SDR();
		input.initialize(sizeX, sizeY);
		
		output = new SDR();
		output.initialize(sizeX, sizeY * sizeZ);
		
		activeCellPositions.clear();
		predictiveCellPositions.clear();
		
		burstingColumns = new SDR();
		burstingColumns.initialize(sizeX, sizeY);
		
		distalSegments = new Grid();
		distalSegments.initialize(sizeX, sizeY, sizeZ);
		
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				return new ArrayList<DistalSegment>();
			}
		};
		distalSegments.applyFunction(function, runnerList);
	}
	
	@Override
	public void randomizeConnections(CodeRunnerList runnerList) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				@SuppressWarnings("unchecked")
				List<DistalSegment> segments = (ArrayList<DistalSegment>) value;
				if (segments.size()>0) {
					for (DistalSegment segment: segments) {
						segment.synapses.clear();
					}
					segments.clear();
				}
				return value;
			}
		};
		distalSegments.applyFunction(function, runnerList);
	}
	
	@Override
	public void setInput(SDR sdr) {
		super.setInput(sdr);
		burstingColumns.setValue(false);
		activeCellPositions.clear();
	}
	
	@Override
	public void buildProcessorChain(CodeRunnerChain runnerChain, boolean learn) {
		CodeRunnerList activateColumns = new CodeRunnerList();
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object[] applyFunction(GridColumn column, Object[] values) {
				boolean active = false;
				for (GridColumn inputColumn: activeInputColumns) {
					if (column.posX()==inputColumn.posX() && column.posY()==inputColumn.posY()) {
						active = true;
						break;
					}
				}
				if (active) {
					boolean predicted = false;
					for (Position pos: predictiveCellPositions) {
						if (pos.x==column.posX() && pos.y==column.posY()) {
							activeCellPositions.add(pos);
							predicted = true;
						}
					}
					if (!predicted) {
						activeCellPositions.addAll(Position.getColumnPositions(column.posX(), column.posY(), sizeZ));
						values[0] = true;
					}
					// TODO:
					// Determine winners for learning?
				}
				return values;
			}
		};
		burstingColumns.applyFunction(function, activateColumns);
		
		// TODO: Learning
		
		
		CodeRunnerList clearPrediction = new CodeRunnerList();
		clearPrediction.add(new RunCode() {
			@Override
			protected boolean run() {
				predictiveCellPositions.clear();
				return true;
			}
		});
		
		CodeRunnerList predictActiveCells = new CodeRunnerList();
		function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, int posZ, Object value) {
				@SuppressWarnings("unchecked")
				List<DistalSegment> segments = (ArrayList<DistalSegment>) value;
				if (segments.size()>0) {
					boolean predict = false;
					for (DistalSegment segment: segments) {
						segment.calculateActiveSynapses(activeCellPositions, permanenceThreshold);
						if (segment.activeSynapses>activationThreshold) {
							predict = true;
						}
					}
					if (predict) {
						predictiveCellPositions.add(new Position(column.posX(),column.posY(),posZ));
					}
				}
				return value;
			}
		};
		distalSegments.applyFunction(function, predictActiveCells);
		
		runnerChain.add(activateColumns);
		runnerChain.add(clearPrediction);
		runnerChain.add(predictActiveCells);
	}
	
	public void debugColumnActivation() {
		System.out.println("Burst SDR: " + burstingColumns.toStr());
		System.out.println("activeCellPositions: " + activeCellPositions.size());
	}
}
