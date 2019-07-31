package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.matrix.ZActivator;
import nl.zeesoft.zdk.matrix.ZMatrix;
import nl.zeesoft.zdk.matrix.functions.ZSigmoid;

public class NeuralNet {
	public int				inputNeurons	= 1;
	public int				hiddenLayers	= 1;
	public int				hiddenNeurons	= 1;
	public int				outputNeurons	= 1;
	
	public ZActivator		activator		= new ZSigmoid();
	
	protected ZMatrix[]		layerValues		= null;
	protected ZMatrix[]		layerWeights	= null;
	protected ZMatrix[]		layerBiases		= null;

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
	
	public Cycle getNewCycle() {
		return new Cycle(this);
	}

	public Exercise getNewExercise() {
		return new Exercise(this);
	}

	public void runCycle(Cycle c) {
		c.prepare(this);
		if (c.error.length()==0) {
			c.outputs = feedForward(c.inputs);
			c.finalize(this);
		}
	}
	
	protected float[] feedForward(float[] inputs) {
		resetValues();
		getInputValues().fromArray(inputs);
		int p = 0;
		for (int i = 1; i < layerValues.length; i++) {
			layerValues[i] = ZMatrix.multiply(layerWeights[i],layerValues[p]);
			layerValues[i].add(layerBiases[i]);
			layerValues[i].applyFunction(activator);
			p++;
		}
		return getOutputValues().toArray();
	}
	
	private void resetValues() {
		for (int i = 0; i < layerValues.length; i++) {
			layerValues[i].set(0.0F);
		}
	}
	
	private ZMatrix getInputValues() {
		return layerValues[0];
	}
	
	private ZMatrix getOutputValues() {
		return layerValues[layerValues.length - 1];
	}
}
