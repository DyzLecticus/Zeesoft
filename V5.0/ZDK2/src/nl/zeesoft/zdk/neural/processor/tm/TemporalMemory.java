package nl.zeesoft.zdk.neural.processor.tm;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.json.Finalizable;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.processor.CellsProcessor;
import nl.zeesoft.zdk.neural.processor.ExecutorProcessor;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.LearningProcessor;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;

public class TemporalMemory extends LearningProcessor implements CellsProcessor, ExecutorProcessor, Finalizable {
	public static final int		ACTIVE_COLUMNS_INPUT		= 0;
	public static final int		ACTIVE_APICAL_INPUT			= 1;
	
	public static final int		ACTIVE_CELLS_OUTPUT			= 0;
	public static final int		BURSTING_COLUMNS_OUTPUT		= 1;
	public static final int		PREDICTIVE_CELLS_OUTPUT		= 2;
	public static final int		WINNER_CELLS_OUTPUT			= 3;

	public TmConfig				config						= null;
	public TmColumns			columns						= null;
	public TmCells				cells						= null;
	
	public Executor				executor					= new Executor();

	/*
	@JsonTransient
	public int					processed					= 0;
	@JsonTransient
	public long					time1						= 0;
	@JsonTransient
	public long					time2						= 0;
	@JsonTransient
	public long					time3						= 0;
	@JsonTransient
	public long					time4						= 0;
	*/
	
	@Override
	public void setNumberOfWorkers(int workers) {
		executor.setWorkers(workers);
	}

	@Override
	public void finalizeObject() {
		columns.executor = executor;
		cells.executor = executor;
		configureColumns();
		cells.setConfig(this, config);
	}

	public void initialize(TmConfig config) {
		this.config = config.copy();

		cells = new TmCells(this, this.config, executor);
		columns = new TmColumns(this, this.config, cells, executor);
	}
	
	@Override
	public void reset() {
		if (cells!=null) {
			cells.reset(this);
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
	protected void processValidIO(ProcessorIO io) {
		//long s = System.nanoTime();
		cells.cycleState(getNewActiveApicalCellPositions(io));
		//time1 += System.nanoTime() - s;

		//s = System.nanoTime();
		columns.activate(this, getNewActiveColumnPositions(io), io.timeoutMs);
		//time2 += System.nanoTime() - s;

		if (learn) {
			//s = System.nanoTime();
			columns.adapt(this, io.timeoutMs);
			//time3 += System.nanoTime() - s;
		}
		
		//s = System.nanoTime();
		cells.predictActiveCells(this, io.timeoutMs);
		//time4 += System.nanoTime() - s;
		
		addOutput(io, cells.activeCellPositions);
		addOutput(io, columns.getPositionsForValue(this, true));
		addOutput(io, cells.predictiveCellPositions);
		addOutput(io, cells.winnerCellPositions);
		
		//processed++;
		//if (processed % 100 == 0) {
		//	Console.log(time1 / processed + " " + time2 / processed + " " + time3 / processed + " " + time4 / processed);
		//}
	}

	@Override
	public Cells getCells() {
		return cells;
	}
	
	@Override
	protected boolean isInitialized(ProcessorIO io) {
		if (config==null) {
			io.error = this.getClass().getSimpleName() + " is not initialized";
		} else if (cells==null || !cells.isInitialized()) {
			io.error = this.getClass().getSimpleName() + " cells are not initialized";
		}
		return io.error.length() == 0;
	}
	
	protected List<Position> getNewActiveApicalCellPositions(ProcessorIO io) {
		List<Position> r = new ArrayList<Position>();
		if (io.inputs.size()>ACTIVE_APICAL_INPUT) {
			r = io.inputs.get(ACTIVE_APICAL_INPUT).getOnPositions(cells.size);
		}
		return r;
	}
	
	protected List<Position> getNewActiveColumnPositions(ProcessorIO io) {
		return io.inputs.get(ACTIVE_COLUMNS_INPUT).getOnPositions(columns.size);
	}
	
	protected void configureColumns() {
		columns.config = config;
		columns.cells = cells;
		columns.executor = executor;
	}
}
