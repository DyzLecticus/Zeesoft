package nl.zeesoft.zdk.matrix;

import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.ExecutorTask;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;

public class MatrixExecutor {
	public Matrix			matrix		= null;
	public Executor			executor	= null;

	public MatrixExecutor() {
		
	}

	public MatrixExecutor(Matrix matrix, Executor executor) {
		this.matrix = matrix;
		this.executor = executor;
	}
	
	public ExecutorTask execute(Object caller, int timeoutMs) {
		ExecutorTask r = null;
		if (matrix!=null && matrix.size!=null) {
			if (executor == null || executor.getWorkers()==0) {
				matrix.applyFunction(caller, getFunctionForWorker());
			} else {
				r = applyFunction(caller, timeoutMs);
			}
		}
		return r;
	}
	
	protected Function getFunctionForWorker() {
		// Override to implement
		return new Function();
	}
	
	protected ExecutorTask applyFunction(Object caller, int timeoutMs) {
		FunctionList fl = new FunctionList();
		for (int worker = 0; worker < executor.getWorkers(); worker++) {
			fl.addFunction(getWorkerFunction(worker, getFunctionForWorker()));
		}
		FunctionListList fll = new FunctionListList();
		fll.addFunctionList(fl);
		return executor.execute(caller, fll, timeoutMs);
	}
	
	protected Function getWorkerFunction(int worker, Function exec) {
		Function r = new Function() {
			@Override
			protected Object exec() {
				applyFunction((int) param1, caller, (Function) param2);
				return true;
			}					
		};
		r.param1 = worker;
		r.param2 = exec;
		return r;
	}
	
	protected void applyFunction(int worker, Object caller, Function exec) {
		for (int i = 0; i < matrix.volume(); i++) {
			if (i % executor.getWorkers() == worker) {
				Position pos = matrix.size.getPositionForIndex(i);
				exec.param1 = pos;
				exec.param2 = matrix.getValue(pos);
				Object returnValue = exec.execute(caller);
				matrix.setValue(pos, returnValue);
			}
		}
	}
}
