package nl.zeesoft.zbe.brain;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZIntegerGenerator;

public class Brain {
	private ZIntegerGenerator				generator		= new ZIntegerGenerator(0,100);
	
	private GeneticCode						code			= new GeneticCode(10000);
	private BrainProperties					properties		= new BrainProperties(code);
	
	private NeuronLayer						inputLayer		= new NeuronLayer(); 
	private List<NeuronLayer>				middleLayers	= new ArrayList<NeuronLayer>();
	private NeuronLayer						outputLayer		= new NeuronLayer(); 
	
	public GeneticCode getCode() {
		return code;
	}
	
	public NeuronLayer getInputLayer() {
		return inputLayer;
	}
	
	public NeuronLayer getOutputLayer() {
		return outputLayer;
	}
	
	public void initialize(int inputNeurons,int outputNeurons,int minLayers,int maxLayers) {
		destroy();
		
		// Initialize layers and nodes
		int nodeId = 1;
		for (int n = 0; n < inputNeurons; n++) {
			inputLayer.neurons.add(new Neuron(nodeId));
			nodeId++;
		}
		int nodesPerLayer = properties.getMiddleLayerNodes(inputLayer, outputLayer);
		for (int l = 0; l < properties.getMiddleLayers(minLayers, maxLayers); l++) {
			NeuronLayer layer = new NeuronLayer();
			for (int n = 0; n < nodesPerLayer; n++) {
				layer.neurons.add(new Neuron(nodeId));
				nodeId++;
			}
			middleLayers.add(layer);
		}
		for (int n = 0; n < outputNeurons; n++) {
			outputLayer.neurons.add(new Neuron(nodeId));
			nodeId++;
		}

		// Initialize thresholds
		List<NeuronLayer> layers = getLayers();
		for (NeuronLayer layer: layers) {
			for (Neuron neuron: layer.neurons) {
				neuron.threshold = getRandomFloat();
			}
		}

		// Initialize links
		int nl = 0;
		for (NeuronLayer layer: layers) {
			nl++;
			if (nl<layers.size()) {
				NeuronLayer nextLayer = layers.get(nl);
				for (Neuron neuron: layer.neurons) {
					for (Neuron target: nextLayer.neurons) {
						NeuronLink link = new NeuronLink();
						link.target = target;
						link.source = neuron;
						link.weight = getRandomFloat();
						neuron.targets.add(link);
						target.sources.add(link);  
					}
				}
			}
		}
	}
	
	public void destroy() {
		inputLayer.destroy();
		for (NeuronLayer layer: middleLayers) {
			layer.destroy();
		}
		middleLayers.clear();
		outputLayer.destroy();
	}
	
	public void runCycles(List<Cycle> cycles) {
		for (Cycle cycle: cycles) {
			runCycle(cycle);
		}
	}
	
	public void runCycle(Cycle cycle) {
		List<NeuronLayer> layers = getLayers();
		resetNodeValues(layers);
		for (int n = 0; n < cycle.inputs.length; n++) {
			if (n<inputLayer.neurons.size()) {
				inputLayer.neurons.get(n).value = cycle.inputs[n];
			}
		}
		for (NeuronLayer layer: layers) {
			if (layer!=inputLayer) {
				for (Neuron neuron: layer.neurons) {
					float value = 0.0F;
					float maxValue = 0.0F;
					for (NeuronLink link: neuron.sources) {
						maxValue += link.weight;
						if (link.source.value>link.source.threshold) {
							value += link.weight;
						}
					}
					neuron.value = (value / maxValue);
				}
			}
		}
		for (int n = 0; n < cycle.outputs.length; n++) {
			if (n<outputLayer.neurons.size()) {
				cycle.outputs[n] = outputLayer.neurons.get(n).value;
			}
		}
	}
	
	protected List<NeuronLayer> getLayers() {
		List<NeuronLayer> r = new ArrayList<NeuronLayer>();
		r.add(inputLayer);
		for (NeuronLayer layer: middleLayers) {
			r.add(layer);
		}
		r.add(outputLayer);
		return r;
	}
	
	private void resetNodeValues(List<NeuronLayer> layers) {
		for (NeuronLayer layer: layers) {
			for (Neuron neuron: layer.neurons) {
				neuron.value = 0.0F;
			}
		}
	}
	
	private float getRandomFloat() {
		return (0.01F * (float)generator.getNewInteger());
	}
}
