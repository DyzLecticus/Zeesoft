package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Test extends Prediction implements JsAble {
	public float[] 			expectations	= null;

	public float[] 			errors			= null;
	
	protected Test(JsFile json) {
		fromJson(json);
	}
	
	protected Test(int inputNeurons,int outputNeurons) {
		super(inputNeurons,outputNeurons);
		initialize(inputNeurons,outputNeurons);
	}
	
	protected Test(NeuralNet nn) {
		super(nn);
		initialize(nn.inputNeurons,nn.outputNeurons);
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("inputs",inputs));
		json.rootElement.children.add(new JsElem("outputs",outputs));
		json.rootElement.children.add(new JsElem("expectations",expectations));
		json.rootElement.children.add(new JsElem("errors",errors));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			inputs = json.rootElement.getChildFloatArray("inputs",inputs);
			outputs = json.rootElement.getChildFloatArray("outputs",outputs);
			expectations = json.rootElement.getChildFloatArray("expectations",expectations);
			errors = json.rootElement.getChildFloatArray("errors",errors);
		}
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
	}
		
	@Override
	protected void finalize(NeuralNet nn) {
		super.finalize(nn);
		for (int i = 0; i < errors.length; i++) {
			errors[i] = expectations[i] - outputs[i];
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
