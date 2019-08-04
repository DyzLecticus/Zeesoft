package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.ZMatrix;
import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.functions.ZActivator;
import nl.zeesoft.zdk.functions.ZLossFunction;
import nl.zeesoft.zdk.functions.ZParamFunction;

public class NeuralNet {
	public int					inputNeurons	= 1;
	public int					hiddenLayers	= 1;
	public int					hiddenNeurons	= 1;
	public int					outputNeurons	= 1;
	
	public ZActivator			activator		= StaticFunctions.LEAKY_RELU;
	public ZActivator			outputActivator	= null;
	public ZParamFunction		errorFunction	= null;
	public float				learningRate	= 0.1F;
	
	protected ZMatrix[]			layerValues		= null;
	protected ZMatrix[]			layerWeights	= null;
	protected ZMatrix[]			layerBiases		= null;

	public NeuralNet(int inputNeurons, int hiddenLayers, int hiddenNeurons, int outputNeurons) {
		if (inputNeurons<1) {
			inputNeurons = 1;
		}
		if (hiddenLayers<1) {
			hiddenLayers = 1;
		}
		if (hiddenNeurons<1) {
			hiddenNeurons = 1;
		}
		if (outputNeurons<1) {
			outputNeurons = 1;
		}
		this.inputNeurons = inputNeurons;
		this.hiddenLayers = hiddenLayers;
		this.hiddenNeurons = hiddenNeurons;
		this.outputNeurons = outputNeurons;

		int totalLayers = hiddenLayers + 2;
		
		layerValues = new ZMatrix[totalLayers];
		layerWeights = new ZMatrix[totalLayers];
		layerBiases = new ZMatrix[totalLayers];
		int lNeurons = inputNeurons;
		int pNeurons = inputNeurons;
		for (int i = 0; i < layerValues.length; i++) {
			if (i == layerValues.length - 1) {
				lNeurons = outputNeurons;
			}
			layerValues[i] = new ZMatrix(lNeurons,1);
			if (i==0) {
				layerWeights[i] = new ZMatrix(1,1);
				layerBiases[i] = new ZMatrix(1,1);
			} else {
				layerWeights[i] = new ZMatrix(lNeurons,pNeurons);
				layerBiases[i] = new ZMatrix(lNeurons,1);
				pNeurons = lNeurons;
			}
			lNeurons = hiddenNeurons;
		}
	}
	
	public void randomizeWeightsAndBiases() {
		for (int i = 0; i < layerValues.length; i++) {
			layerWeights[i].randomize();
			layerBiases[i].randomize();;
		}
	}
	
	public Prediction getNewPrediction() {
		return new Prediction(this);
	}

	public Test getNewTest() {
		return new Test(this);
	}

	public TestSet getNewTestSet() {
		return new TestSet(this);
	}

	public void predict(Prediction p) {
		predict(p,null);
	}

	public void predict(Prediction p,ZLossFunction lossFunction) {
		p.prepare(this);
		if (p.error.length()==0) {
			p.outputs = feedForward(p.inputs);
			p.finalize(this,lossFunction);
		}
	}

	public void test(TestSet tSet) {
		for (Test t: tSet.tests) {
			predict(t,tSet.lossFunction);
		}
		tSet.finalize();
	}

	public void train(TestSet tSet) {
		for (Test t: tSet.tests) {
			train(t,tSet.lossFunction);
		}
		tSet.finalize();
	}

	public void train(Test t,ZLossFunction lossFunction) {
		predict(t,lossFunction);
		if (t.error.length()==0) {
			feedBackward(t.errors);
		}
	}
	
	public NeuralNet copy() {
		NeuralNet r = getNewNeuralNet(inputNeurons,hiddenLayers,hiddenNeurons,outputNeurons);
		r.activator = activator;
		r.outputActivator = outputActivator;
		r.learningRate = learningRate;
		for (int i = 0; i < r.layerValues.length; i++) {
			r.layerValues[i] = layerValues[i].copy();
			r.layerWeights[i] = layerWeights[i].copy();
			r.layerBiases[i] = layerBiases[i].copy();
		}
		return r;
	}
	
	protected float[] feedForward(float[] inputs) {
		resetValues();
		getInputValues().fromArray(inputs);
		int p = 0;
		for (int i = 1; i < layerValues.length; i++) {
			layerValues[i] = ZMatrix.multiply(layerWeights[i],layerValues[p]);
			layerValues[i].add(layerBiases[i]);
			if (i == layerValues.length - 1 && outputActivator!=null) {
				layerValues[i].applyFunction(outputActivator);
				if (outputActivator==StaticFunctions.SOFTMAX_TOP) {
					float total = getOutputValues().getColumnValuesAdded(0);
					if (total!=0) {
						getOutputValues().divide(total);
					}
				}
			} else {
				layerValues[i].applyFunction(activator);
			}
			p++;
		}
		return getOutputValues().toArray();
	}

	protected void feedBackward(float[] errs) {
		ZMatrix errors = ZMatrix.getFromArray(errs);
		
		int p = layerValues.length - 2;
		for (int i = (layerValues.length - 1); i > 0; i--) {
			ZMatrix gradients = layerValues[i].copy();
			if (i == layerValues.length - 1 && outputActivator!=null) {
				// TODO: Softmax derivative
				gradients.applyFunction(outputActivator.getDerivative());
			} else {
				gradients.applyFunction(activator.getDerivative());
			}
			gradients.multiply(errors);
			gradients.multiply(learningRate);

			ZMatrix pTValues = ZMatrix.transpose(layerValues[p]);
			ZMatrix deltas = ZMatrix.multiply(gradients,pTValues);
			layerWeights[i].add(deltas); 
			
			layerBiases[i].add(gradients);
			
			ZMatrix tWeights = ZMatrix.transpose(layerWeights[i]);
			errors = ZMatrix.multiply(tWeights,errors);
			p--;
		}
	}

	protected void resetValues() {
		for (int i = 0; i < layerValues.length; i++) {
			layerValues[i].set(0.0F);
		}
	}
	
	protected ZMatrix getInputValues() {
		return layerValues[0];
	}
	
	protected ZMatrix getOutputValues() {
		return layerValues[layerValues.length - 1];
	}
	
	protected NeuralNet getNewNeuralNet(int inputNeurons, int hiddenLayers, int hiddenNeurons, int outputNeurons) {
		return new NeuralNet(inputNeurons,hiddenLayers,hiddenNeurons,outputNeurons);
	}
}
