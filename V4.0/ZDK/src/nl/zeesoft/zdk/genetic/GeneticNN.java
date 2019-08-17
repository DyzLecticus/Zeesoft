package nl.zeesoft.zdk.genetic;

import nl.zeesoft.zdk.ZMatrix;
import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.neural.NeuralNet;

/**
 * A GeneticNN can be used to generate combinations of GeneticCode and corresponding NeuralNet instances.
 * It can also be used to generate a NeuralNet instance for a given GeneticCode.
 */
public class GeneticNN {
	private static int		HIDDEN_LAYERS		= 0;
	private static int		HIDDEN_NEURONS		= 1;
	private static int		ACTIVATOR			= 2;
	private static int		OUTPUT_ACTIVATOR	= 3;
	private static int		LEARNING_RATE		= 4;
	private static int		WEIGHT_BIAS_START	= 5;

	public int				inputNeurons		= 1;
	public int				maxHiddenLayers		= 1;
	public int				maxHiddenNeurons	= 2;
	public int				outputNeurons		= 1;
	public int				codePropertyStart	= 0;
	
	public GeneticCode		code				= null;
	public NeuralNet		neuralNet			= null;

	public GeneticNN(int inputNeurons, int maxHiddenLayers, int maxHiddenNeurons, int outputNeurons, int codePropertyStart) {
		initialize(inputNeurons,maxHiddenLayers,maxHiddenNeurons,outputNeurons,codePropertyStart,null);
	}

	public GeneticNN(int inputNeurons, int maxHiddenLayers, int maxHiddenNeurons, int outputNeurons, int codePropertyStart, GeneticCode code) {
		initialize(inputNeurons,maxHiddenLayers,maxHiddenNeurons,outputNeurons,codePropertyStart,code);
	}
	
	public int calculateMinCodeLength() {
		return calculateMinCodeLength(inputNeurons,maxHiddenLayers,maxHiddenNeurons,outputNeurons,codePropertyStart);
	}
	
	public void generateNewNN() {
		generateNewNN(null);
	}
	
	public void generateNewNN(GeneticCode code) {
		this.code = code;
		if (this.code==null) {
			this.code = new GeneticCode(calculateMinCodeLength());
		}
		neuralNet = getNewNN(inputNeurons,maxHiddenLayers,maxHiddenNeurons,outputNeurons,codePropertyStart,this.code);
	}

	public GeneticNN copy() {
		GeneticCode copyCode = null;
		if (this.code!=null) {
			copyCode = new GeneticCode();
			copyCode.setCode(code.getCode());
		}
		GeneticNN r = new GeneticNN(inputNeurons,maxHiddenLayers,maxHiddenNeurons,outputNeurons,codePropertyStart,copyCode);
		if (neuralNet!=null) {
			r.neuralNet = neuralNet.copy();
		}
		return r;
	}

	public static int calculateMinCodeLength(int inputNeurons, int maxHiddenLayers, int maxHiddenNeurons, int outputNeurons,int codePropertyStart) {
		int r = codePropertyStart;
		r += NeuralNet.calculateSize(inputNeurons, maxHiddenLayers, maxHiddenNeurons, outputNeurons);
		r = r * 3;
		return r;
	}
	
	public static NeuralNet getNewNN(int inputNeurons, int maxHiddenLayers, int maxHiddenNeurons, int outputNeurons,int codePropertyStart, GeneticCode code) {
		NeuralNet r = null;
		
		if (inputNeurons<1) {
			inputNeurons = 1;
		}
		int hiddenLayers = 1;
		if (maxHiddenLayers>1) {
			hiddenLayers = code.getIntegerMinMax(codePropertyStart + HIDDEN_LAYERS,1,maxHiddenLayers);
		}
		int hiddenNeurons = 2;
		if (maxHiddenNeurons>2) {
			hiddenNeurons = code.getIntegerMinMax(codePropertyStart + HIDDEN_NEURONS,2,maxHiddenNeurons);
		}
		if (outputNeurons<1) {
			outputNeurons = 1;
		}

		r = new NeuralNet(inputNeurons,hiddenLayers,hiddenNeurons,outputNeurons);
		
		int activator = code.getInteger(codePropertyStart + ACTIVATOR,StaticFunctions.ACTIVATORS.length - 1);
		int outputActivator = code.getInteger(codePropertyStart + OUTPUT_ACTIVATOR,StaticFunctions.OUTPUT_ACTIVATORS.length - 1);
		r.activator = StaticFunctions.ACTIVATORS[activator];
		r.outputActivator = StaticFunctions.OUTPUT_ACTIVATORS[outputActivator];
		r.learningRate = 0.1F * code.get(codePropertyStart + LEARNING_RATE);
		
		r.randomizeWeightsAndBiases();
		int prop = WEIGHT_BIAS_START;
		for (int i = 1; i < r.layerValues.length; i++) {
			prop = assignProperties(code,prop,r.layerWeights[i]);
			if (prop>=code.size()) {
				break;
			}
			prop = assignProperties(code,prop,r.layerBiases[i]);
			if (prop>=code.size()) {
				break;
			}
		}
		r.applyWeightFunctions();
		
		return r;
	}
	
	protected void initialize(int inputNeurons, int maxHiddenLayers, int maxHiddenNeurons, int outputNeurons,int codePropertyStart, GeneticCode code) {
		if (inputNeurons<1) {
			inputNeurons = 1;
		}
		if (maxHiddenLayers<1) {
			maxHiddenLayers = 1;
		}
		if (maxHiddenNeurons<2) {
			maxHiddenLayers = 2;
		}
		if (outputNeurons<1) {
			outputNeurons = 1;
		}
		this.inputNeurons = inputNeurons;
		this.maxHiddenLayers = maxHiddenLayers;
		this.maxHiddenNeurons = maxHiddenNeurons;
		this.outputNeurons = outputNeurons;
		this.codePropertyStart = codePropertyStart;
		generateNewNN(code);
	}
	
	private static int assignProperties(GeneticCode code,int prop,ZMatrix m) {
		for (int row = 0; row < m.rows; row++) {
			for (int col = 0; col < m.cols; col++) {
				if (prop>=code.size()) {
					break;
				}
				float val = -1 + code.get(prop) * 2;
				m.data[row][col] = val;
				prop++;
			}
		}
		return prop;
	}
}
