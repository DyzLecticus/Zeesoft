package nl.zeesoft.zbe.brain;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

public class Brain {
	private GeneticCode						code			= new GeneticCode(10000);
	private BrainProperties					properties		= new BrainProperties(code);
	
	// Used for copies
	private int								minLayers		= 0;
	private int								maxLayers		= 0;
	
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
	
	public ZStringBuilder initialize(int inputNeurons,int outputNeurons,int minLayers,int maxLayers) {
		ZStringBuilder err = new ZStringBuilder();
		
		destroy();
		
		this.minLayers = minLayers;
		this.maxLayers = maxLayers;
		
		int max = inputNeurons;
		if (outputNeurons>max) {
			max = outputNeurons;
		}
		int requiredCodeSize = properties.getRequiredCodeSize(inputNeurons, outputNeurons, maxLayers);
		if (requiredCodeSize>code.size()) {
			err.append("The minimum genetic code length for the specified dimensions is " + (requiredCodeSize * 3));
		} else {
			// Initialize layers and nodes
			int objectId = 1;
			for (int n = 0; n < inputNeurons; n++) {
				inputLayer.neurons.add(new Neuron(objectId));
				objectId++;
			}
			int nodesPerLayer = properties.getMiddleLayerNodes(inputLayer, outputLayer);
			for (int l = 0; l < properties.getMiddleLayers(minLayers, maxLayers); l++) {
				NeuronLayer layer = new NeuronLayer();
				for (int n = 0; n < nodesPerLayer; n++) {
					layer.neurons.add(new Neuron(objectId));
					objectId++;
				}
				middleLayers.add(layer);
			}
			for (int n = 0; n < outputNeurons; n++) {
				outputLayer.neurons.add(new Neuron(objectId));
				objectId++;
			}
	
			// Initialize thresholds
			List<NeuronLayer> layers = getLayers();
			for (NeuronLayer layer: layers) {
				for (Neuron neuron: layer.neurons) {
					neuron.threshold = properties.getThresholdWeight(neuron.id);
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
							NeuronLink link = new NeuronLink(objectId);
							objectId++;
							link.target = target;
							link.source = neuron;
							link.weight = properties.getThresholdWeight(link.id);
							neuron.targets.add(link);
							target.sources.add(link);  
						}
					}
				}
			}
		}
		return err;
	}
	
	public void destroy() {
		inputLayer.destroy();
		for (NeuronLayer layer: middleLayers) {
			layer.destroy();
		}
		middleLayers.clear();
		outputLayer.destroy();
	}
	
	public void runTestCycleSet(TestCycleSet tcs) {
		for (Cycle cycle: tcs.cycles) {
			runCycle(cycle);
		}
		tcs.finalize();
	}
	
	public void runCycle(Cycle cycle) {
		List<NeuronLayer> layers = getLayers();
		resetNodeValues(layers);
		for (int n = 0; n < cycle.inputs.length; n++) {
			if (n<inputLayer.neurons.size()) {
				Neuron input = inputLayer.neurons.get(n);
				input.value = cycle.inputs[n];
				if (input.value>=input.threshold) {
					cycle.firedNeurons.add(input);
				}
			}
		}
		for (NeuronLayer layer: layers) {
			if (layer!=inputLayer) {
				for (Neuron neuron: layer.neurons) {
					if (neuron.value>=neuron.threshold) {
						cycle.firedNeurons.add(neuron);
					}
					float value = 0.0F;
					float maxValue = 0.0F;
					for (NeuronLink link: neuron.sources) {
						maxValue += link.weight;
						if (link.source.value>=link.source.threshold) {
							value += link.weight;
							cycle.firedLinks.add(link);
						}
					}
					neuron.value = (value / maxValue);
				}
			}
		}
		cycle.finalize(this);
	}
	
	public Brain copy() {
		return copy(true);
	}
	
	public Brain copy(boolean copyThresholdWeight) {
		Brain r = getCopyBrain();
		r.getCode().setCode(getCode().getCode());
		r.initialize(inputLayer.neurons.size(),outputLayer.neurons.size(),minLayers,maxLayers);
		List<NeuronLayer> copyLayers = r.getLayers();
		if (copyThresholdWeight) {
			int l = 0;
			for (NeuronLayer layer: getLayers()) {
				NeuronLayer copyLayer = copyLayers.get(l);
				int n = 0;
				for (Neuron neuron: layer.neurons) {
					Neuron copyNeuron = copyLayer.neurons.get(n);
					copyNeuron.threshold = neuron.threshold;
					int li = 0;
					for (NeuronLink link: neuron.targets) {
						NeuronLink copyLink = copyNeuron.targets.get(li);
						copyLink.weight = link.weight;
						li++;
					}
					n++;
				}
				l++;
			}
		}
		return r;
	}
	
	protected Brain getCopyBrain() {
		return new Brain();
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
}
