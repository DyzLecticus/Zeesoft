package nl.zeesoft.zdk.neural.processor.tm;

import java.util.List;

import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.ExecutorTask;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.MatrixExecutor;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;

public class TmColumns extends Matrix {
	public TmConfig	config		= null;
	public TmCells	cells		= null;
	
	public Executor	executor	= null;
	
	protected TmColumns(Object caller, Matrix matrix) {
		initialize(matrix.size);
		copyDataFrom(caller, matrix);
	}

	public TmColumns(Object caller, TmConfig config, TmCells cells, Executor executor) {
		this.config = config;
		this.cells = cells;
		this.executor = executor;
		initialize(new Size(config.size.x,config.size.y));
	}
	
	public ExecutorTask activate(Object caller, List<Position> activeColumnPositions, int timeoutMs) {
		MatrixExecutor exec = new MatrixExecutor(this, executor) {
			@Override
			protected Function getFunctionForWorker() {
				return getActivateFunction(activeColumnPositions);
			}
		};
		return exec.execute(caller, timeoutMs);
	}
	
	public ExecutorTask adapt(Object caller, int timeoutMs) {
		MatrixExecutor exec = new MatrixExecutor(this, executor) {
			@Override
			protected Function getFunctionForWorker() {
				return getAdaptFunction();
			}
		};
		return exec.execute(caller, timeoutMs);
	}
	
	protected Function getActivateFunction(List<Position> activeColumnPositions) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				boolean burst = false;
				Position column = (Position) param1;
				if (activeColumnPositions.contains(column)) {
					burst = cells.activateColumn(column);
				}
				return burst;
			}
		};
		return r;
	}

	protected Function getAdaptFunction() {
		Function r = new Function() {
			@Override
			protected Object exec() {
				cells.adaptColumn((Position) param1, (boolean) param2);
				return param2;
			}
		};
		return r;
	}
}
