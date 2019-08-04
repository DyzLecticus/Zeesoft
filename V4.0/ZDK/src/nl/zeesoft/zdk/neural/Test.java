package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.functions.ZLossFunction;

public class Test extends Prediction {
	public float[] 			expectations	= null;

	public float[] 			errors			= null;
	public float			loss			= 0;
	
	protected Test(int inputNeurons,int outputNeurons) {
		super(inputNeurons,outputNeurons);
		initialize(inputNeurons,outputNeurons);
	}
	
	protected Test(NeuralNet nn) {
		super(nn);
		initialize(nn.inputNeurons,nn.outputNeurons);
	}
	
	@Override
	public Prediction copy() {
		Test r = new Test(inputs.length,outputs.length);
		copyTo(r);
		return r;
	}
	
	@Override
	protected void prepare(NeuralNet nn) {
		super.prepare(nn);
		initializeValues(errors);
		loss = 0;
	}
		
	@Override
	protected void finalize(NeuralNet nn,ZLossFunction lossFunction) {
		super.finalize(nn,lossFunction);
		for (int i = 0; i < errors.length; i++) {
			errors[i] = expectations[i] - outputs[i];
		}
		if (lossFunction!=null) {
			loss = lossFunction.calculateLoss(outputs,expectations);
		}
	}

	@Override
	protected void copyTo(Prediction p) {
		super.copyTo(p);
		if (p instanceof Test) {
			Test t = (Test) p;
			t.expectations = new float[expectations.length];
			copyValues(t.expectations,expectations);
			t.errors = new float[errors.length];
			copyValues(t.errors,errors);
			t.loss = loss;
		}
	}
	
	@Override
	protected void initialize(int inputNeurons, int outputNeurons) {
		super.initialize(inputNeurons, outputNeurons);
		expectations = new float[outputNeurons];
		initializeValues(expectations);
		errors = new float[outputNeurons];
		initializeValues(errors);
	}
}
