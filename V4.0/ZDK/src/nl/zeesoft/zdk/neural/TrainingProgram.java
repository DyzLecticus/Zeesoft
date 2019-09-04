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
	public float		totalAverageError	= 0;
	public float		totalAverageLoss	= 0;

	public TrainingProgram(NeuralNet nn) {
		neuralNet = nn;
	}

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
		totalAverageError = 0;
		totalAverageLoss = 0;
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
		boolean done = !stopOnSuccess || initialResults.success;
		if (done) {
			latestResults = initialResults;
			totalAverageError = initialResults.averageError;
			totalAverageLoss = initialResults.averageLoss;
		} else {
			for (int i = 0; i < epochs; i++) {
				TestSet tSet = getNewTrainingSet();
				neuralNet.train(tSet);
				trainedEpochs++;
				totalAverageError += tSet.averageError;
				totalAverageLoss += tSet.averageLoss;
				latestResults = tSet;
				done = tSet.success;
				if (done && stopOnSuccess) {
					break;
				}
			}
		}
		return done;
	}
	
	/**
	 * Returns false if the latest average loss is above a certain factor of the initial loss.
	 * Used by evolvers to check if the the average loss decreases better than linear; if not, the training is abandoned.
	 *  
	 * @param factor The factor to apply to the initial average loss for comparison
	 * @return False if the latest average loss is above a certain factor of the initial loss, else true
	 */
	public boolean lossCheck(float factor) {
		boolean r = true;
		if (latestResults!=null && latestResults.averageLoss > initialResults.averageLoss * factor) {
			r = false;
		}
		return r;
	}
	
	/**
	 * Returns the calculated training result (lower is better).
	 * 
	 * @return The training result
	 */
	public float getTrainingResult() {
		float r = 0;
		if (initialResults!=null && latestResults!=null) {
			r = initialResults.averageLoss * totalAverageLoss;
		}
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("stopOnSuccess","" + stopOnSuccess));
		json.rootElement.children.add(new JsElem("trainedEpochs","" + trainedEpochs));
		json.rootElement.children.add(new JsElem("totalAverageError","" + totalAverageError));
		json.rootElement.children.add(new JsElem("totalAverageLoss","" + totalAverageLoss));
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
			totalAverageError = json.rootElement.getChildFloat("totalAverageError",totalAverageError);
			totalAverageLoss = json.rootElement.getChildFloat("totalAverageLoss",totalAverageLoss);
			JsElem initElem = json.rootElement.getChildByName("initialResults");
			if (initElem!=null && initElem.children.size()>0) {
				JsFile js = new JsFile();
				js.rootElement = initElem.children.get(0);
				if (js.rootElement!=null) {
					initialResults = new TestSet(js);
					baseTestSet = initialResults.copy();
				}
			}
			JsElem latestElem = json.rootElement.getChildByName("latestResults");
			if (latestElem!=null && latestElem.children.size()>0) {
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
		r.totalAverageError = totalAverageError;
		r.totalAverageLoss = totalAverageLoss;
		r.latestResults = latestResults.copy();
		return r;
	}
	
	protected TestSet getNewTrainingSet() {
		TestSet r = baseTestSet.copy();
		r.randomizeOrder();
		return r;
	}
}
