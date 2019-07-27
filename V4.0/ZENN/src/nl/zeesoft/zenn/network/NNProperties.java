package nl.zeesoft.zenn.network;

import nl.zeesoft.zenn.GeneticCode;

public class NNProperties {
	private static final int	MIDDLE_LAYERS					= 100;
	private static final int	MIDDLE_LAYER_NODES				= 101;
	
	private static final int	THRESHOLD_WEIGHT_START			= 102;
	
	private GeneticCode			code							= null;
	
	protected NNProperties(GeneticCode code) {
		this.code = code;
	}

	protected int getRequiredCodeSize(int inputNeurons,int outputNeurons,int maxLayers) {
		int range = inputNeurons;
		if (outputNeurons>range) {
			range = outputNeurons;
		}
		int width = (range * 2);
		return
			(THRESHOLD_WEIGHT_START + 1) + 
			(width * maxLayers) +
			outputNeurons +
			((width * width) * (maxLayers - 1)) +
			(width * outputNeurons)
			; 
	}
	
	protected int getMiddleLayers(int minLayers,int maxLayers) {
		return minLayers + code.getInteger(MIDDLE_LAYERS,(maxLayers - minLayers));
	}

	protected int getMiddleLayerNodes(int inputNeurons,int outputNeurons) {
		int range = inputNeurons;
		if (outputNeurons>range) {
			range = outputNeurons;
		}
		return range + code.getInteger(MIDDLE_LAYER_NODES,range);
	}
	
	protected int getThresholdWeightStart() {
		return THRESHOLD_WEIGHT_START;
	}
	
	protected float getThresholdWeight(int index,boolean threshold) {
		float r = code.get(THRESHOLD_WEIGHT_START + index);
		if (threshold) {
			r = 0.0F + (r * 0.5F);
		} else  {
			r = -0.1F + (r * 0.2F);
		}
		return r;
	}
}
