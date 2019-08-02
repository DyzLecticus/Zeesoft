package nl.zeesoft.zdk.neural;

public class TrainingProgram {
	public NeuralNet	neuralNet			= null;
	public TestSet		baseTestSet			= null;
	
	public boolean		stopSuccess			= true;
	
	public int			trainedEpochs		= 0;
	public int			successes			= 0;
	public TestSet		initialResults		= null;
	public TestSet		latestResults		= null;
	
	public TrainingProgram(NeuralNet nn,TestSet baseTs) {
		neuralNet = nn;
		baseTestSet = baseTs;
		prepare();
	}
	
	public void prepare() {
		trainedEpochs = 0;
		successes = 0;
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
			if (stopSuccess && tSet.success) {
				done = true;
				break;
			}
		}
		return done;
	}
		
	protected TestSet getNewTrainingSet() {
		TestSet r = baseTestSet.copy();
		r.randomizeOrder();
		return r;
	}
}
