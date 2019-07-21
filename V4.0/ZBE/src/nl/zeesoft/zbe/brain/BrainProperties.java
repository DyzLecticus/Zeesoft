package nl.zeesoft.zbe.brain;

public class BrainProperties {
	private static final int	MIDDLE_LAYERS					= 0;
	private static final int	MIDDLE_LAYER_NODES				= 1;
	
	private GeneticCode			code							= null;
	
	protected BrainProperties(GeneticCode code) {
		this.code = code;
	}
	
	protected int getMiddleLayers(int minLayers,int maxLayers) {
		return minLayers + code.getInteger(MIDDLE_LAYERS,(maxLayers - minLayers));
	}
	
	protected int getMiddleLayerNodes(NeuronLayer inputLayer,NeuronLayer outputLayer) {
		int range = inputLayer.neurons.size();
		if (outputLayer.neurons.size()>range) {
			range = outputLayer.neurons.size();
		}
		return range + code.getInteger(MIDDLE_LAYER_NODES,range);
	}
}
