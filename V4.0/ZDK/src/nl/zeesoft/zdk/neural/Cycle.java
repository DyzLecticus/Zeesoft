package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.ZStringBuilder;

public class Cycle {
	public ZStringBuilder	error	= new ZStringBuilder();
	public float[] 			inputs	= null;
	public float[] 			outputs	= null;
	
	protected Cycle() {
		
	}

	protected Cycle(NeuralNet nn) {
		inputs = new float[nn.inputNeurons];
		initializeValues(inputs);
		outputs = new float[nn.outputNeurons];
		initializeValues(outputs);
	}
	
	public Cycle copy() {
		Cycle r = new Cycle();
		copyTo(r);
		return r;
	}
	
	protected void prepare(NeuralNet nn) {
		if (inputs.length!=nn.inputNeurons) {
			error.append("Number of inputs must equal the number of neural net input neurons");
		} else if (outputs.length!=nn.outputNeurons) {
			error.append("Number of outputs must equal the number of neural net output neurons");
		}
		if (error.length()==0) {
			initializeValues(outputs);
		}
	}
		
	protected void finalize(NeuralNet nn) {
		// Override to implement
	}
	
	protected void copyTo(Cycle c) {
		c.error = new ZStringBuilder(error);
		c.inputs = new float[inputs.length];
		copyValues(c.inputs,inputs);
		c.outputs = new float[outputs.length];
		copyValues(c.outputs,outputs);
	}
	
	protected void initializeValues(float[] a) {
		for (int i = 0; i< a.length; i++) {
			a[i] = 0;
		}
	}
	
	protected void copyValues(float[] to,float[] from) {
		for (int i = 0; i< to.length; i++) {
			to[i] = from[i];
		}
	}
}
