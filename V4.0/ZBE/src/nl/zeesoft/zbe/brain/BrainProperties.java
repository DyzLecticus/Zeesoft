package nl.zeesoft.zbe.brain;

import nl.zeesoft.zbe.GeneticCode;

public class BrainProperties {
	private static final int	MIDDLE_LAYERS					= 100;
	private static final int	MIDDLE_LAYER_NODES				= 101;
	
	private static final int	THRESHOLD_WEIGHT_START			= 102;
	
	private GeneticCode			code							= null;
	
	protected BrainProperties(GeneticCode code) {
		this.code = code;
	}

	protected int getRequiredCodeSize(int inputNeurons,int outputNeurons,int maxLayers) {
		int range = inputNeurons;
		if (outputNeurons>range) {
			range = outputNeurons;
		}
		int width = (range * 2);
		return (THRESHOLD_WEIGHT_START + 1) + ((width * width) + width) * (maxLayers - 1); 
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
	
	protected float getThresholdWeight(int objectId) {
		return code.get(objectId + THRESHOLD_WEIGHT_START);
	}
}
