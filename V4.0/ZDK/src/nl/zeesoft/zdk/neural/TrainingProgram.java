package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class TrainingProgram implements JsAble {
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
