package nl.zeesoft.zbe.brain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

public class BrainTest extends Brain {
	public static final int			INPUT_NEURONS	= 12;
	public static final int			OUTPUT_NEURONS	= 4;
	public static final int			MIN_LAYERS		= 1;
	public static final int			MAX_LAYERS		= 5;
	
	public static final int			LEFT_RED		= 0;
	public static final int			LEFT_GREEN		= 1;
	public static final int			LEFT_BLUE		= 2;
	public static final int			LEFT_GREY		= 3;
	
	public static final int			FRONT_RED		= 4;
	public static final int			FRONT_GREEN		= 5;
	public static final int			FRONT_BLUE		= 6;
	public static final int			FRONT_GREY		= 7;
	
	public static final int			RIGHT_RED		= 8;
	public static final int			RIGHT_GREEN		= 9;
	public static final int			RIGHT_BLUE		= 10;
	public static final int			RIGHT_GREY		= 11;
	
	public static final int			OUT_LEFT		= 0;
	public static final int			OUT_FRONT		= 1;
	public static final int			OUT_BACK		= 2;
	public static final int			OUT_RIGHT		= 3;
	
	public static final float[]		INTENSITIES		= {1.00F,0.75F,0.50F,0.25F};
	
	public BrainTest() {
		getCode().generate(10000);
		initialize(INPUT_NEURONS,OUTPUT_NEURONS,MIN_LAYERS,MAX_LAYERS);
	}
	
	public void layersToSystemOut() {
		DecimalFormat df = new DecimalFormat("0.00");
		
		int l = 0;
		List<NeuronLayer> layers = getLayers();
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
	
	public List<Cycle> getTestCycles(boolean herbivore) {
		List<Cycle> r = new ArrayList<Cycle>();
		
		TestCycle tc = null;
		
		int leftFoodInput = LEFT_GREEN;
		int frontFoodInput = FRONT_GREEN;
		int rightFoodInput = RIGHT_GREEN;
		if (!herbivore) {
			leftFoodInput = LEFT_BLUE;
			frontFoodInput = FRONT_BLUE;
			rightFoodInput = RIGHT_BLUE;
		}
		
		tc = new TestCycle();
		tc.initialize(this);
		tc.expectedOutputs[OUT_BACK] = 1.0F;
		r.add(tc);

		// Move toward food
		for (int d = 3; d >= 1; d--) {
			tc = new TestCycle();
			tc.initialize(this);
			tc.inputs[frontFoodInput] = INTENSITIES[d]; 
			tc.expectedOutputs[OUT_BACK] = 1.0F;
			r.add(tc);
		}
		
		// Eat food
		tc = new TestCycle();
		tc.initialize(this);
		tc.inputs[frontFoodInput] = INTENSITIES[0]; 
		tc.expectedOutputs[OUT_FRONT] = 1.0F;
		r.add(tc);

		// Turn toward food
		for (int d = 3; d >= 0; d--) {
			tc = new TestCycle();
			tc.initialize(this);
			tc.inputs[leftFoodInput] = INTENSITIES[d]; 
			tc.expectedOutputs[OUT_RIGHT] = 1.0F;
			r.add(tc);
			
			tc = new TestCycle();
			tc.initialize(this);
			tc.inputs[rightFoodInput] = INTENSITIES[d]; 
			tc.expectedOutputs[OUT_LEFT] = 1.0F;
			r.add(tc);
		}

		// Turn away from walls
		for (int d = 3; d >= 0; d--) {
			tc = new TestCycle();
			tc.initialize(this);
			tc.inputs[FRONT_GREY] = INTENSITIES[d]; 
			tc.expectedOutputs[OUT_RIGHT] = 1.0F;
			tc.expectedOutputs[OUT_LEFT] = 1.0F;
			r.add(tc);
		}

		// Turn away from corners
		for (int d = 3; d >= 0; d--) {
			tc = new TestCycle();
			tc.initialize(this);
			tc.inputs[FRONT_GREY] = INTENSITIES[d]; 
			tc.inputs[LEFT_GREY] = INTENSITIES[d]; 
			tc.expectedOutputs[OUT_LEFT] = 1.0F;
			r.add(tc);
			
			tc = new TestCycle();
			tc.initialize(this);
			tc.inputs[FRONT_GREY] = INTENSITIES[d]; 
			tc.inputs[RIGHT_GREY] = INTENSITIES[d]; 
			tc.expectedOutputs[OUT_RIGHT] = 1.0F;
			r.add(tc);
		}

		// Turn/move away from carnivores
		for (int d = 3; d >= 0; d--) {
			tc = new TestCycle();
			tc.initialize(this);
			tc.inputs[LEFT_RED] = INTENSITIES[d]; 
			tc.expectedOutputs[OUT_BACK] = 1.0F;
			r.add(tc);

			tc = new TestCycle();
			tc.initialize(this);
			tc.inputs[FRONT_RED] = INTENSITIES[d]; 
			tc.expectedOutputs[OUT_RIGHT] = 1.0F;
			tc.expectedOutputs[OUT_LEFT] = 1.0F;
			r.add(tc);
			
			tc = new TestCycle();
			tc.initialize(this);
			tc.inputs[RIGHT_RED] = INTENSITIES[d]; 
			tc.expectedOutputs[OUT_BACK] = 1.0F;
			r.add(tc);
		}
		
		return r;
	}
}
