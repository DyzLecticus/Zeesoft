package nl.zeesoft.zenn.network;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.GeneticCode;

public class NN {
	private GeneticCode						code			= new GeneticCode(10000);
	private NNProperties					properties		= new NNProperties(code);
	
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
			int nodesPerLayer = properties.getMiddleLayerNodes(inputNeurons,outputNeurons);
			int inputStartPosX = (nodesPerLayer - inputNeurons) / 2;
			int outputStartPosX = (nodesPerLayer - outputNeurons) / 2;
			int objectId = 1;
			for (int n = 0; n < inputNeurons; n++) {
				inputLayer.neurons.add(new Neuron(objectId,inputStartPosX));
				inputStartPosX++;
				objectId++;
			}
			int numMidLayers = properties.getMiddleLayers(minLayers, maxLayers);
			for (int l = 0; l < numMidLayers; l++) {
				NeuronLayer layer = new NeuronLayer();
				for (int n = 0; n < nodesPerLayer; n++) {
					layer.neurons.add(new Neuron(objectId,n));
					objectId++;
				}
				middleLayers.add(layer);
			}
			for (int n = 0; n < outputNeurons; n++) {
				outputLayer.neurons.add(new Neuron(objectId,outputStartPosX));
				outputStartPosX++;
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
		for (TestCycle tc: tcs.cycles) {
			runCycle(tc);
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
						if (link.weight>0.0F) {
							maxValue += link.weight;
						}
						if (link.source.value>=link.source.threshold) {
							if (link.weight>0.0F) {
								value += link.weight;
							} else {
								value -= (link.weight * -1);
							}
							cycle.firedLinks.add(link);
						}
					}
					if (value>0.0F && maxValue>0.0F) {
						neuron.value = (value / maxValue);
					}
				}
			}
		}
		cycle.finalize(this);
	}
	
	public NN copy() {
		return copy(true);
	}
	
	public NN copy(boolean copyThresholdWeight) {
		NN r = getCopyNN();
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

	public void toSystemOut() {
		DecimalFormat df = new DecimalFormat("0.00");
		
		List<NeuronLayer> layers = getLayers();
		
		ZStringBuilder layout = new ZStringBuilder();
		int width = layers.get(1).neurons.size();
		for (NeuronLayer layer: layers) {
			if (layout.length()>0) {
				layout.append("\n");
			}
			for (int x = 0; x < width; x++) {
				Neuron neuron = layer.getNeuronByPosX(x);
				if (neuron!=null) {
					layout.append("[ ]");
				} else {
					layout.append("   ");
				}
			}
		}
		System.out.println("Neuron layout;");
		System.out.println(layout);
		
		System.out.println();
		int l = 0;
		for (NeuronLayer layer: layers) {
			String name = "Middle " + l;
			l++;
			if (l==1) {
				name = "Input";
			} else if (l==layers.size()) {
				name = "Output";
			}
			System.out.println("Layer: " + name);
			for (Neuron neuron: layer.neurons) {
				ZStringBuilder sources = new ZStringBuilder();
				for (NeuronLink link: neuron.sources) {
					if (sources.length()>0) {
						sources.append(", ");
					}
					sources.append("" + String.format("%03d",link.source.id) + "/" + df.format(link.weight));
				}
				if (sources.length()>0) {
					sources.insert(0,", sources/weights: ");
				}
				System.out.println("  Neuron: " + String.format("%03d",neuron.id) + ", threshold: " + df.format(neuron.threshold) + ", value: " + df.format(neuron.value) + sources);
			}
		}
	}

	protected NN getCopyNN() {
		return new NN();
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
