package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

/**
 * A TrainingProgram can be used to simplify the process of testing and training NeuralNet instances.
 * It requires a NeuralNet instance and a base TestSet instance.
 * It will use a randomized copy of of the TestSet to test and train each epoch.
 * It will also record initial results and keep track of latest results to provide insights into the rate of convergence.
 */
public class TrainingProgram implements JsAble {
	public NeuralNet	neuralNet			= null;
	public TestSet		baseTestSet			= null;
	
	public boolean		stopOnSuccess		= true;
	
	public TestSet		initialResults		= null;
	public int			trainedEpochs		= 0;
	public TestSet		latestResults		= null;

	public TrainingProgram(NeuralNet nn,TestSet baseTs) {
		neuralNet = nn;
		if (baseTs!=null) {
			baseTestSet = baseTs;
			prepare();
		}
	}
	
	/**
	 * Resets the training program.
	 */
	public void prepare() {
		trainedEpochs = 0;
		latestResults = null;
		initialResults = getNewTrainingSet();
		neuralNet.test(initialResults);
	}
	
	/**
	 * Trains the network a specific number of epochs.
	 * 
	 * @param epochs The number of epochs
	 * @return true If the latest training results were successful
	 */
	public boolean train(int epochs) {
		boolean done = false;
		for (int i = 0; i < epochs; i++) {
			TestSet tSet = getNewTrainingSet();
			neuralNet.train(tSet);
			trainedEpochs++;
			latestResults = tSet;
			done = tSet.success;
			if (stopOnSuccess) {
				break;
			}
		}
		return done;
	}
	
	/**
	 * Returns the calculated error change.
	 * 
	 * @return The error change
	 */
	public float getErrorChange() {
		return latestResults.averageError - initialResults.averageError;
	}
	
	/**
	 * Returns the calculated error change rate.
	 * 
	 * @return The error change rate
	 */
	public float getErrorChangeRate() {
		return getErrorChange() / trainedEpochs * -1F;
	}
	
	/**
	 * Returns the calculated loss change.
	 * 
	 * @return The loss change
	 */
	public float getLossChange() {
		return latestResults.averageLoss - initialResults.averageLoss;
	}
	
	/**
	 * Returns the calculated loss change rate.
	 * 
	 * @return The loss change rate
	 */
	public float getLossChangeRate() {
		return getLossChange() / trainedEpochs * -1F;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("stopOnSuccess","" + stopOnSuccess));
		json.rootElement.children.add(new JsElem("trainedEpochs","" + trainedEpochs));
		if (initialResults!=null) {
			JsElem initElem = new JsElem("initialResults",true);
			json.rootElement.children.add(initElem);
			initElem.children.add(initialResults.toJson().rootElement);
		}
		if (latestResults!=null) {
			JsElem latestElem = new JsElem("latestResults",true);
			json.rootElement.children.add(latestElem);
			latestElem.children.add(latestResults.toJson().rootElement);
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			stopOnSuccess = json.rootElement.getChildBoolean("stopOnSuccess",stopOnSuccess);
			trainedEpochs = json.rootElement.getChildInt("trainedEpochs",trainedEpochs);
			JsElem initElem = json.rootElement.getChildByName("initialResults");
			if (initElem!=null) {
				JsFile js = new JsFile();
				js.rootElement = initElem.children.get(0);
				if (js.rootElement!=null) {
					initialResults = new TestSet(js);
				}
			}
			JsElem latestElem = json.rootElement.getChildByName("latestResults");
			if (latestElem!=null) {
				JsFile js = new JsFile();
				js.rootElement = latestElem.children.get(0);
				if (js.rootElement!=null) {
					latestResults = new TestSet(js);
				}
			}
		}
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
