package nl.zeesoft.zdk.neural;

public class TrainingProgram {
	public NeuralNet	neuralNet			= null;
	public TestSet		baseTestSet			= null;
	
	public boolean		stopOnSuccess		= true;
	
	public TestSet		initialResults		= null;
	public int			trainedEpochs		= 0;
	public TestSet		latestResults		= null;
	
	public TrainingProgram(NeuralNet nn,TestSet baseTs) {
		neuralNet = nn;
		baseTestSet = baseTs;
		prepare();
	}
	
	public void prepare() {
		trainedEpochs = 0;
		latestResults = null;
		initialResults = getNewTrainingSet();
		neuralNet.test(initialResults);
	}
	
	public boolean train(int repeats) {
		boolean done = false;
		for (int i = 0; i < repeats; i++) {
			TestSet tSet = getNewTrainingSet();
			neuralNet.train(tSet);
			trainedEpochs++;
			latestResults = tSet;
			if (stopOnSuccess && tSet.success) {
				done = true;
				break;
			}
		}
		return done;
	}
	
	public float getErrorChange() {
		return latestResults.averageError - initialResults.averageError;
	}
	
	public float getErrorChangeRate() {
		return getErrorChange() / trainedEpochs * -1F;
	}
	
	public float getLossChange() {
		return latestResults.averageLoss - initialResults.averageLoss;
	}

	public float getLossChangeRate() {
		return getLossChange() / trainedEpochs * -1F;
	}
	
	public TrainingProgram copy() {
		TrainingProgram r = new TrainingProgram(neuralNet.copy(),baseTestSet.copy());
		r.stopOnSuccess = stopOnSuccess;
		r.initialResults = initialResults.copy();
		r.trainedEpochs = trainedEpochs;
		r.latestResults = latestResults.copy();
		return r;
	}
	
	protected TestSet getNewTrainingSet() {
		TestSet r = baseTestSet.copy();
		r.randomizeOrder();
		return r;
	}
}
