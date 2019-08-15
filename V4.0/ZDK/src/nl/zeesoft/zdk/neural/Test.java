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
		json.rootElement.children.add(toJsElem("inputs",inputs));
		json.rootElement.children.add(toJsElem("outputs",outputs));
		json.rootElement.children.add(toJsElem("expectations",expectations));
		json.rootElement.children.add(toJsElem("errors",errors));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			inputs = fromJsElem("inputs",json.rootElement);
			outputs = fromJsElem("outputs",json.rootElement);
			expectations = fromJsElem("expectations",json.rootElement);
			errors = fromJsElem("errors",json.rootElement);
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
	
	protected JsElem toJsElem(String name,float[] values) {
		JsElem r = new JsElem(name,true);
		for (int i = 0; i< values.length; i++) {
			r.children.add(new JsElem(null,"" + values[i]));
		}
		return r;
	}
	
	protected float[] fromJsElem(String name,JsElem parent) {
		float[] r = null;
		JsElem elem = parent.getChildByName(name);
		if (elem!=null) {
			r = new float[elem.children.size()];
			int i = 0;
			for (JsElem v: elem.children) {
				r[i] = Float.parseFloat(v.value.toString());
				i++;
			}
		}
		return r;
	}
}
