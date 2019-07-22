package nl.zeesoft.zbe.animal;

import java.text.DecimalFormat;
import java.util.List;

import nl.zeesoft.zbe.brain.Brain;
import nl.zeesoft.zbe.brain.Neuron;
import nl.zeesoft.zbe.brain.NeuronLayer;
import nl.zeesoft.zbe.brain.NeuronLink;
import nl.zeesoft.zdk.ZStringBuilder;

public class AnimalBrain extends Brain {
	public static final int			MIN_LAYERS		= 1;
	public static final int			MAX_LAYERS		= 5;
	
	public static final int			LEFT_RED		= 0;
	public static final int			LEFT_GREEN		= 1;
	public static final int			LEFT_BLUE		= 2;
	public static final int			LEFT_GREY		= 3;
	
	public static final int			FRONT_RED		= 4;
	public static final int			FRONT_GREEN		= 5;
	public static final int			FRONT_BLACK		= 6;
	public static final int			FRONT_BLUE		= 7;
	public static final int			FRONT_GREY		= 8;
	
	public static final int			RIGHT_RED		= 9;
	public static final int			RIGHT_GREEN		= 10;
	public static final int			RIGHT_BLUE		= 11;
	public static final int			RIGHT_GREY		= 12;
	
	public static final int			OUT_LEFT		= 0;
	public static final int			OUT_FRONT		= 1;
	public static final int			OUT_BACK		= 2;
	public static final int			OUT_RIGHT		= 3;
	
	public static final int			INPUT_NEURONS	= RIGHT_GREY + 1;
	public static final int			OUTPUT_NEURONS	= OUT_RIGHT + 1;
	
	public static final float[]		INTENSITIES		= {1.00F,0.75F,0.50F,0.25F};
	
	public ZStringBuilder initialize() {
		getCode().generate(10000);
		return initialize(INPUT_NEURONS,OUTPUT_NEURONS,MIN_LAYERS,MAX_LAYERS);
	}

	public static AnimalBrain getTrainableAnimalBrain(boolean herbivore, int minSuccesses, int timeOutMs) {
		AnimalBrain r = null;
		long started = System.currentTimeMillis();
		while (r==null) {
			AnimalBrain brain = new AnimalBrain();
			ZStringBuilder err = brain.initialize();
			if (err.length()>0) {
				break;
			} else {
				AnimalTestCycleSet tcs = new AnimalTestCycleSet();
				tcs.initialize(brain,herbivore);
				brain.runTestCycleSet(tcs);
				if (tcs.successes>=minSuccesses) {
					r = brain;
				}
			}
			if (r==null && (System.currentTimeMillis() - started) >= timeOutMs) {
				break;
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
	
	@Override
	protected Brain getCopyBrain() {
		return new AnimalBrain();
	}
}
