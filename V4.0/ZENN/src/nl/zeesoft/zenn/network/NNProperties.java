package nl.zeesoft.zenn.network;

import nl.zeesoft.zenn.GeneticCode;

public class NNProperties {
	private static final int	MIDDLE_LAYERS					= 100;
	private static final int	MIDDLE_LAYER_NODES				= 101;
	
	private static final int	BIAS_WEIGHT_START				= 102;
	
	private GeneticCode			code							= null;
	
	protected NNProperties(GeneticCode code) {
		this.code = code;
	}

	protected int getRequiredCodeSize(int inputNeurons,int outputNeurons,int maxLayers) {
		int maxWidth = getWidthRange(inputNeurons,outputNeurons) * 2;
		return
			(BIAS_WEIGHT_START + 1) + 
			(maxWidth * maxLayers) +
			outputNeurons +
			((maxWidth * maxWidth) * (maxLayers - 1)) +
			(maxWidth * outputNeurons)
			; 
	}
	
	protected int getMiddleLayers(int minLayers,int maxLayers) {
		return minLayers + code.getInteger(MIDDLE_LAYERS,(maxLayers - minLayers));
	}

	protected int getMiddleLayerNodes(int inputNeurons,int outputNeurons) {
		int range = getWidthRange(inputNeurons,outputNeurons);
		return range + code.getInteger(MIDDLE_LAYER_NODES,range);
	}
	
	protected int getBiasWeightStart() {
		return BIAS_WEIGHT_START;
	}
	
	protected float getBiasWeight(int index,boolean bias) {
		float r = code.get(BIAS_WEIGHT_START + index);
		r = -1.0F + (r * 2.0F);
		return r;
	}
	
	private int getWidthRange(int inputNeurons,int outputNeurons) {
		int r = inputNeurons;
		if (outputNeurons>r) {
			r = outputNeurons;
		}
		return r;
	}
}
