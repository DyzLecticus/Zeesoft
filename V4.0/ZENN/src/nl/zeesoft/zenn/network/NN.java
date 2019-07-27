package nl.zeesoft.zenn.network;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zenn.GeneticCode;

public class NN implements JsAble {
	private GeneticCode						code			= null;
	private NNProperties					properties		= null;
	
	// Used for copies
	private int								minLayers		= 0;
	private int								maxLayers		= 0;
	
	private NeuronLayer						inputLayer		= new NeuronLayer(); 
	private List<NeuronLayer>				middleLayers	= new ArrayList<NeuronLayer>();
	private NeuronLayer						outputLayer		= new NeuronLayer(); 
	
	private SortedMap<Integer,Neuron>		neuronsById		= new TreeMap<Integer,Neuron>();
	
	public GeneticCode getCode() {
		return code;
	}
	
	public void setCode(GeneticCode code) {
		this.code = code;
		properties = new NNProperties(code);
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
			int id = 1;
			for (int n = 0; n < inputNeurons; n++) {
				Neuron neuron = new Neuron(id,inputStartPosX);
				neuronsById.put(id,neuron);
				inputLayer.neurons.add(neuron);
				inputStartPosX++;
				id++;
			}
			int numMidLayers = properties.getMiddleLayers(minLayers, maxLayers);
			for (int l = 0; l < numMidLayers; l++) {
				NeuronLayer layer = new NeuronLayer();
				for (int n = 0; n < nodesPerLayer; n++) {
					Neuron neuron = new Neuron(id,n);
					neuronsById.put(id,neuron);
					layer.neurons.add(neuron);
					id++;
				}
				middleLayers.add(layer);
			}
			for (int n = 0; n < outputNeurons; n++) {
				Neuron neuron = new Neuron(id,outputStartPosX);
				neuronsById.put(id,neuron);
				outputLayer.neurons.add(neuron);
				outputStartPosX++;
				id++;
			}
	
			// Initialize thresholds
			int i = properties.getThresholdWeightStart();
			List<NeuronLayer> layers = getLayers();
			for (NeuronLayer layer: layers) {
				for (Neuron neuron: layer.neurons) {
					if (layer!=inputLayer) {
						neuron.threshold = properties.getThresholdWeight(i,true);
						i++;
					} else {
						neuron.threshold = 0.0F;
					}
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
							link.weight = properties.getThresholdWeight(i,false);
							i++;
							neuron.targets.add(link);
							target.sources.add(link);  
						}
					}
				}
			}
		}
		return err;
	}

	@Override
	public JsFile toJson() {
		return toJson(true);
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			ZStringBuilder code = json.rootElement.getChildZStringBuilder("code");
			int inputNeurons = json.rootElement.getChildInt("inputNeurons");
			int outputNeurons = json.rootElement.getChildInt("outputNeurons");
			int minLayers = json.rootElement.getChildInt("minLayers");
			int maxLayers = json.rootElement.getChildInt("maxLayers");
			if (code.length()>0) {
				ZStringEncoder encoder = new ZStringEncoder(code);
				encoder.decompress();
				this.code.setCode(encoder);
				initialize(inputNeurons,outputNeurons,minLayers,maxLayers);
			}
			JsElem neuronsElem = json.rootElement.getChildByName("neurons");
			if (neuronsElem!=null) {
				for (JsElem neuronElem: neuronsElem.children) {
					int id = neuronElem.getChildInt("id");
					Neuron neuron = neuronsById.get(id);
					if (neuron!=null) {
						JsFile neuronJson = new JsFile();
						neuronJson.rootElement = neuronElem;
						neuron.fromJson(neuronJson);
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
		neuronsById.clear();
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
				if (input.value>input.threshold) {
					cycle.firedNeurons.add(input);
				}
			}
		}
		for (NeuronLayer layer: layers) {
			if (layer!=inputLayer) {
				for (Neuron neuron: layer.neurons) {
					if (neuron.value>neuron.threshold) {
						cycle.firedNeurons.add(neuron);
					}
					float value = 0.0F;
					float maxValue = 0.0F;
					for (NeuronLink link: neuron.sources) {
						if (link.weight>0.0F) {
							maxValue += link.weight;
						}
						if (link.source.value>link.source.threshold) {
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
		return copy(true,0.0F);
	}
	
	public NN copy(float mutationPercentage) {
		return copy(false,mutationPercentage);
	}
	
	public NN copy(boolean copyThresholdWeight,float mutationPercentage) {
		NN r = getCopyNN();
		r.setCode(new GeneticCode(getCode().getCode()));
		if (mutationPercentage>0.0F) {
			r.getCode().mutate(mutationPercentage);
		}
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
	
	protected JsFile toJson(boolean includeNN) {
		ZStringEncoder encoder = new ZStringEncoder(code.getCode());
		encoder.compress();

		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("inputNeurons","" + inputLayer.neurons.size(),true));
		json.rootElement.children.add(new JsElem("outputNeurons","" + outputLayer.neurons.size(),true));
		json.rootElement.children.add(new JsElem("minLayers","" + minLayers,true));
		json.rootElement.children.add(new JsElem("maxLayers","" + maxLayers,true));
		json.rootElement.children.add(new JsElem("code",encoder,true));
		if (includeNN) {
			JsElem neuronsElem = new JsElem("neurons",true);
			json.rootElement.children.add(neuronsElem);
			for (Neuron neuron: neuronsById.values()) {
				JsFile neuronJson = neuron.toJson();
				neuronsElem.children.add(neuronJson.rootElement);
			}
		}
		return json;
	}

	private void resetNodeValues(List<NeuronLayer> layers) {
		for (NeuronLayer layer: layers) {
			for (Neuron neuron: layer.neurons) {
				neuron.value = 0.0F;
			}
		}
	}
}
