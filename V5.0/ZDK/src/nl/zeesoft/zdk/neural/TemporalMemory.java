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

public class TemporalMemory extends SDRProcessor {
	public int					sizeX				= 48;
	public int					sizeY				= 48;
	public int					sizeZ				= 16;

	public int					maxSegmentsPerCell	= 256;
	
	protected Grid				activeCells			= null;
	protected List<Position>	activeCellPositions	= null;
	protected Grid				predictiveCells		= null;
	protected SDR				burstingColumns		= null;
	protected Grid				distalSegments		= null;

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
		
		activeCells = new Grid();
		activeCells.initialize(sizeX, sizeY, sizeZ, false);
		
		predictiveCells = new Grid();
		predictiveCells.initialize(sizeX, sizeY, sizeZ, false);
		
		distalSegments = new Grid();
		distalSegments.initialize(sizeX, sizeY, sizeZ);
		
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, Object value) {
				return new ArrayList<DistalSegment>();
			}
		};
		distalSegments.applyFunction(function, runnerList);
	}
	
	@Override
	public void randomizeConnections(CodeRunnerList runnerList) {
		ColumnFunction function = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, Object value) {
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
				boolean active = true;
				for (GridColumn inputColumn: activeInputColumns) {
					if (column.posX()==inputColumn.posX() && column.posY()==inputColumn.posY()) {
						active = true;
						break;
					}
				}
				if (active) {
					// TODO:
					// For each cell
					// Check if predictive cell in column
					// If no predictive cells; select random 
				}
				// For each cell in column, check predictive, if true, activate
				// If no predictive cells; burst column
				return values;
			}
		};
		burstingColumns.applyFunction(function, activateColumns);
		
		runnerChain.add(activateColumns);
	}
}
