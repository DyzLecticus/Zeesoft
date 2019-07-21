package nl.zeesoft.zbe.brain;

public class TrainingProgram {
	private Brain 			baseBrain			= null;
	private	TestCycleSet	baseTestCycleSet	= null;
	private int				trainCycles			= 1000;
	
	private int				trainedCycles		= 0;

	public TrainingProgram(Brain baseBrain, TestCycleSet baseTestCycleSet) {
		initialize(baseBrain,baseTestCycleSet);
	}
	
	public TrainingProgram(Brain baseBrain, TestCycleSet baseTestCycleSet,int trainCycles) {
		initialize(baseBrain,baseTestCycleSet);
		this.trainCycles = trainCycles;
	}
	
	public Brain runProgram() {
		Brain r = baseBrain.copy();
		trainedCycles = 0;
		TestCycleSet bestResults = baseTestCycleSet.copy();
		r.runTestCycleSet(bestResults);
		if (!bestResults.isSuccess()) {
			float learningRate = bestResults.averageError;
			for (int c = 0; c < trainCycles; c++) {
				trainedCycles++;
				Brain variation = r.copy(true,learningRate);
				TestCycleSet tcs = baseTestCycleSet.copy();
				variation.runTestCycleSet(tcs);
				if (tcs.isSuccess()) {
					r = variation;
					break;
				} else if (tcs.successes > bestResults.successes) {
					bestResults = tcs;
					learningRate = tcs.averageError;
					r = variation;
				}
			}
		}
		return r;
	}
	
	public int getTrainedCycles() {
		return trainedCycles;
	}
	
	private void initialize(Brain baseBrain, TestCycleSet baseTestCycleSet) {
		this.baseBrain = baseBrain;
		this.baseTestCycleSet = baseTestCycleSet;
	}
}
