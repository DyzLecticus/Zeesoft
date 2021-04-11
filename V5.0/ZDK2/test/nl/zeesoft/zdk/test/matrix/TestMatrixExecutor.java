package nl.zeesoft.zdk.test.matrix;

import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.ExecutorTask;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.MatrixExecutor;
import nl.zeesoft.zdk.matrix.Size;

public class TestMatrixExecutor {
	private static TestMatrixExecutor	self	= new TestMatrixExecutor();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		MatrixExecutor exec = new MatrixExecutor();
		assert exec.execute(self, 1000) == null;
		
		exec = new MatrixExecutor(new Matrix(), null);
		assert exec.execute(self, 1000) == null;
		
		Matrix matrix = new Matrix();
		matrix.initialize(new Size(2,2,2));
		exec = new MatrixExecutor(matrix, null);
		assert exec.execute(self, 1000) == null;
		
		Executor executor = new Executor();
		exec = new MatrixExecutor(matrix, executor);
		assert exec.execute(self, 1000) == null;
		
		executor.setWorkers(2);
		exec = new MatrixExecutor(matrix, executor) {
			@Override
			protected Function getFunctionForWorker() {
				return new Function() {
					@Override
					protected Object exec() {
						return 1;
					}
				};
			}
		};
		ExecutorTask task = exec.execute(self, 1000);
		List<Object> returnValues = task.getReturnValues();
		assert returnValues.size() == 2;
		assert (int)matrix.data[0][0][0] == 1;
		assert (int)matrix.data[1][0][0] == 1;
		assert (int)matrix.data[0][1][0] == 1;
		assert (int)matrix.data[1][1][0] == 1;
		assert (int)matrix.data[0][0][1] == 1;
		assert (int)matrix.data[1][0][1] == 1;
		assert (int)matrix.data[0][1][1] == 1;
		assert (int)matrix.data[1][1][1] == 1;
		executor.setWorkers(0);
	}
}
