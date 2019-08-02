package nl.zeesoft.zdk.neural;

public class TrainingProgram {
	public NeuralNet		neuralNet			= null;
	public TrainingSet		baseTrainingSet		= null;
	
	public boolean			stopOnSuccess		= true;
	
	public int				trainedEpochs		= 0;
	public TrainingSet		initialResults		= null;
	public TrainingSet		latestResults		= null;
	
	public TrainingProgram(NeuralNet nn,TrainingSet baseTs) {
		neuralNet = nn;
		baseTrainingSet = baseTs;
		prepare();
	}
	
	public void prepare() {
		trainedEpochs = 0;
		latestResults = null;
		initialResults = getNewTrainingSet();
		neuralNet.test(initialResults);
	}
	
	public boolean train(int epochs) {
		boolean done = false;
		for (int i = 0; i < epochs; i++) {
			TrainingSet tSet = getNewTrainingSet();
			neuralNet.train(tSet);
			latestResults = tSet;
			trainedEpochs++;
			if (stopOnSuccess && tSet.success) {
				done = true;
			}
		}
		return done;
	}
	
	protected TrainingSet getNewTrainingSet() {
		TrainingSet r = baseTrainingSet.copy();
		r.randomizeOrder();
		return r;
	}
}
