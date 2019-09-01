package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.functions.ZLossFunction;
import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class TestSet implements JsAble {
	public int				inputNeurons	= 1;
	public int				outputNeurons	= 1;
	
	public ZLossFunction	lossFunction	= StaticFunctions.MEAN_ABSOLUTE_ERROR;
	public List<Test> 		tests	 		= new ArrayList<Test>();
	public float			errorTolerance	= 0.1F;
	
	public float			averageError	= 0;
	public float			averageLoss		= 0;
	public boolean			success			= true;
	
	public TestSet(JsFile json) {
		fromJson(json);
	}
	
	public TestSet(int inputNeurons, int outputNeurons) {
		if (inputNeurons<1) {
			inputNeurons=1;
		}
		if (outputNeurons<1) {
			outputNeurons=1;
		}
		this.inputNeurons = inputNeurons;
		this.outputNeurons = outputNeurons;
	}
	
	protected TestSet(NeuralNet nn) {
		this.inputNeurons = nn.inputNeurons;
		this.outputNeurons = nn.outputNeurons;
	}
	
	public Test addNewTest() {
		Test r = new Test(inputNeurons,outputNeurons);
		tests.add(r);
		return r;
	}
	
	public boolean isConsistent() {
		boolean r = true;
		for (Test t1: tests) {
			for (Test t2: tests) {
				if (t2!=t1) {
					boolean equalIn = true;
					boolean equalOut = true;
					for (int i = 0; i<t1.inputs.length; i++) {
						if (t1.inputs[i]!=t2.inputs[i]) {
							equalIn = false;
							break;
						}
					}
					if (equalIn) {
						for (int i = 0; i<t1.outputs.length; i++) {
							if (t1.outputs[i]!=t2.outputs[i]) {
								equalOut = false;
								break;
							}
						}
						if (!equalOut) {
							r = false;
							break;
						};
					}
				}
			}
			if (!r) {
				break;
			}
		}
		return r;
	}
	
	public void finalize() {
		averageError = 0;
		averageLoss = 0;
		success = true;
		float total = 0;
		for (Test t: tests) {
			for (int i = 0; i<t.errors.length; i++) {
				float diff = t.errors[i];
				if (diff<0) {
					diff = diff * -1;
				}
				if (diff>errorTolerance) {
					success = false;
				}
				averageError += diff;
				total++;
			}
			if (lossFunction!=null) {
				averageLoss += lossFunction.calculateLoss(t.outputs,t.expectations);
			}
		}
		if (total>0 && averageError>0) {
			averageError = averageError / total;
		}
		if (tests.size()>0 && averageLoss>0) {
			averageLoss = averageLoss / (float) tests.size();
		}
	}
	
	public void randomizeOrder() {
		int l = tests.size();
		if (l>1) {
			for (int i = 0; i < l; i++) {
				Test t = tests.remove(i);
				tests.add(ZRandomize.getRandomInt(0,l - 1),t);
			}
		}
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("inputNeurons","" + inputNeurons));
		json.rootElement.children.add(new JsElem("outputNeurons","" + outputNeurons));
		json.rootElement.children.add(new JsElem("lossFunction","" + lossFunction.getClass().getName(),true));
		json.rootElement.children.add(new JsElem("errorTolerance","" + errorTolerance));
		json.rootElement.children.add(new JsElem("averageError","" + averageError));
		json.rootElement.children.add(new JsElem("averageLoss","" + averageLoss));
		json.rootElement.children.add(new JsElem("success","" + success));
		
		JsElem testsElem = new JsElem("tests",true);
		json.rootElement.children.add(testsElem);
		for (Test t: tests) {
			testsElem.children.add(t.toJson().rootElement);
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			inputNeurons = json.rootElement.getChildInt("inputNeurons",inputNeurons);
			outputNeurons = json.rootElement.getChildInt("outputNeurons",outputNeurons);
			lossFunction = StaticFunctions.getLossFunctionByClassName(json.rootElement.getChildString("lossFunction"));
			errorTolerance = json.rootElement.getChildFloat("errorTolerance",errorTolerance);
			averageError = json.rootElement.getChildFloat("averageError",averageError);
			averageLoss = json.rootElement.getChildFloat("averageLoss",averageLoss);
			success = json.rootElement.getChildBoolean("success",success);
			
			JsElem testsElem = json.rootElement.getChildByName("tests");
			if (testsElem!=null) {
				for (JsElem testElem: testsElem.children) {
					JsFile tJs = new JsFile();
					tJs.rootElement = testElem;
					tests.add(new Test(tJs));
				}
			}
		}
	}
	
	public TestSet copy() {
		TestSet r = getNewTestSet(inputNeurons,outputNeurons);
		for (Test t: tests) {
			r.tests.add((Test) t.copy());
		}
		r.lossFunction = lossFunction;
		r.errorTolerance = errorTolerance;
		r.averageError = averageError;
		r.averageLoss = averageLoss;
		r.success = success;
		return r;
	}
	
	protected TestSet getNewTestSet(int inputNeurons,int outputNeurons) {
		return new TestSet(inputNeurons,outputNeurons);
	}
}
