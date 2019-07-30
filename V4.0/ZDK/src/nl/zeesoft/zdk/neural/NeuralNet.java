package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.matrix.ZMatrix;

public class NeuralNet {
	public int				inputNeurons	= 1;
	public int				hiddenLayers	= 1;
	public int				hiddenNeurons	= 1;
	public int				outputNeurons	= 1;
	
	public ZMatrix[]		layerValues		= null;
	public ZMatrix[]		layerWeights	= null;
	public ZMatrix[]		layerBiases		= null;

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
				// Dummies used to align layers
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
}
