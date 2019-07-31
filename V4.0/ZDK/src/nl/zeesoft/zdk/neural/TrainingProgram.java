package nl.zeesoft.zdk.neural;

public class TrainingProgram {
	public NeuralNet		neuralNet			= null;
	public TrainingSet		baseTrainingSet		= null;
	
	public int				stopOnSuccesses		= 10;
	
	public int				trained				= 0;
	public int				successes			= 0;
	public TrainingSet		initialResults		= null;
	public TrainingSet		finalResults		= null;
	
	public TrainingProgram(NeuralNet nn,TrainingSet baseTs) {
		neuralNet = nn;
		baseTrainingSet = baseTs;
		prepare();
	}
	
	public void prepare() {
		trained = 0;
		successes = 0;
		finalResults = null;
		initialResults = getNewTrainingSet();
		neuralNet.train(initialResults);
		handleResult(initialResults);
	}
	
	public boolean train(int repeats) {
		boolean done = false;
		for (int i = 0; i < repeats; i++) {
			TrainingSet tSet = getNewTrainingSet();
			if (train(tSet)) {
				finalize(tSet);
				done = true;
				break;
			}
		}
		return done;
	}
	
	protected boolean train(TrainingSet tSet) {
		neuralNet.train(tSet);
		return handleResult(tSet);
	}
	
	protected void finalize(TrainingSet tSet) {
		finalResults = tSet;
	}
	
	protected boolean handleResult(TrainingSet tSet) {
		trained++;
		if (tSet.success) {
			successes++;
		}
		return (successes >= stopOnSuccesses);
	}
	
	protected TrainingSet getNewTrainingSet() {
		TrainingSet r = baseTrainingSet.copy();
		r.randomizeOrder();
		return r;
	}
}
