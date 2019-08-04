package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.functions.ZLossFunction;
import nl.zeesoft.zdk.functions.ZRandomize;

public class TestSet {
	public int				inputNeurons	= 1;
	public int				outputNeurons	= 1;
	
	public ZLossFunction	lossFunction	= StaticFunctions.MEAN_SQUARED_ERROR;
	public List<Test> 		tests	 		= new ArrayList<Test>();
	public float			errorTolerance	= 0.1F;
	
	public float			averageError	= 0;
	public float			averageLoss		= 0;
	public boolean			success			= true;
	
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
			averageLoss += t.loss;
		}
		if (total>0) {
			averageError = averageError / total;
			averageLoss = averageLoss / tests.size();
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
	
	public TestSet copy() {
		TestSet r = getNewTestSet(inputNeurons,outputNeurons);
		for (Test t: tests) {
			r.tests.add((Test) t.copy());
		}
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
