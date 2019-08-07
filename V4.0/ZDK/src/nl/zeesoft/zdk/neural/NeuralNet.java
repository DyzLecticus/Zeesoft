package nl.zeesoft.zdk.neural;

import nl.zeesoft.zdk.ZMatrix;
import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.functions.ZActivator;
import nl.zeesoft.zdk.functions.ZLossFunction;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

/**
 * NeuralNet provides a simple multi layer perceptron network.
 * It requires the number of input neurons, hidden layers, neurons per hidden layer, and output neurons to be specified.
 * The activator functions and a separate output layer activator can be configured as well.
 * The learning rate can be used to tune the speed at which the network converges.
 * This class relies heavily on the ZMatrix class and StaticFunctions for its calculations and activations.
 */
public class NeuralNet implements JsAble {
	public int					inputNeurons	= 1;
	public int					hiddenLayers	= 1;
	public int					hiddenNeurons	= 1;
	public int					outputNeurons	= 1;
	
	public ZActivator			activator		= StaticFunctions.LEAKY_RELU;
	public ZActivator			outputActivator	= null;
	public float				learningRate	= 0.1F;
	
	public ZMatrix[]			layerValues		= null;
	public ZMatrix[]			layerWeights	= null;
	public ZMatrix[]			layerBiases		= null;

	public NeuralNet(JsFile json) {
		fromJson(json);
	}

	public NeuralNet(int inputNeurons, int hiddenLayers, int hiddenNeurons, int outputNeurons) {
		initialize(inputNeurons,hiddenLayers,hiddenNeurons,outputNeurons);
	}
	
	/**
	 * Returns the size of the neural network.
	 * 
	 * The size of the network is the total number of neurons and links.
	 * 
	 * @return The size of the neural network
	 */
	public int size() {
		return calculateSize(inputNeurons,hiddenLayers,hiddenNeurons,outputNeurons); 
	}

	/**
	 * Calculates the size of a neural network with the specified dimensions.
	 * 
	 * The size of a network is the total number of neurons and links.
	 * 
	 * @param inputNeurons The number of input neurons
	 * @param hiddenLayers The number of hidden layers
	 * @param hiddenNeurons The number of neurons per hidden layer
	 * @param outputNeurons The number of output neurons
	 * @return The size of a neural network with the specified dimensions
	 */
	public static int calculateSize(int inputNeurons,int hiddenLayers,int hiddenNeurons,int outputNeurons) {
		return 
			(inputNeurons + (inputNeurons * hiddenNeurons)) + 
			(hiddenLayers * hiddenNeurons)  +
			((hiddenLayers - 1) * hiddenNeurons * hiddenNeurons) +
			(outputNeurons + (outputNeurons * hiddenNeurons))
			; 
	}

	/**
	 * Randomizes all weights and biases in the neural network.
	 */
	public void randomizeWeightsAndBiases() {
		for (int i = 1; i < layerValues.length; i++) {
			layerWeights[i].randomize();
			layerBiases[i].randomize();;
		}
	}
	
	/**
	 * Returns a new prediction with the input and output arrays initialized for this neural network.
	 * 
	 * @return A new prediction
	 */
	public Prediction getNewPrediction() {
		return new Prediction(this);
	}

	/**
	 * Returns a new test with all the arrays initialized for this neural network.
	 * 
	 * @return A new test
	 */
	public Test getNewTest() {
		return new Test(this);
	}

	/**
	 * Returns a new test set with the number of input and output neurons initialized for this neural network.
	 * 
	 * @return A new test set
	 */
	public TestSet getNewTestSet() {
		return new TestSet(this);
	}

	/**
	 * Uses the neural network to get the predicted output for a certain set of inputs.
	 * 
	 * @param p The prediction
	 */
	public void predict(Prediction p) {
		predict(p,null);
	}

	/**
	 * Uses the neural network to get the predicted output for a certain set of inputs.
	 * This can also be used to get the output of a Test.
	 * 
	 * @param p The prediction
	 * @param lossFunction The optional loss function which is applied in case the prediction is and instance of a Test
	 */
	public void predict(Prediction p,ZLossFunction lossFunction) {
		p.prepare(this);
		if (p.error.length()==0) {
			p.outputs = feedForward(p.inputs);
			p.finalize(this,lossFunction);
		}
	}

	/**
	 * Uses the neural network to get the predicted outputs for a certain set of tests.
	 * 
	 * @param tSet The test set
	 */
	public void test(TestSet tSet) {
		for (Test t: tSet.tests) {
			predict(t,tSet.lossFunction);
		}
		tSet.finalize();
	}

	/**
	 * Trains the neural network using a certain set of tests.
	 * 
	 * @param tSet The test set
	 */
	public void train(TestSet tSet) {
		for (Test t: tSet.tests) {
			train(t,tSet.lossFunction);
		}
		tSet.finalize();
	}

	/**
	 * Trains the neural network using a specific test.
	 * 
	 * @param t The test
	 * @param lossFunction The optional loss function
	 */
	public void train(Test t,ZLossFunction lossFunction) {
		predict(t,lossFunction);
		if (t.error.length()==0) {
			feedBackward(t.errors);
		}
	}
	
	public NeuralNet copy() {
		NeuralNet r = getNewNeuralNet(inputNeurons,hiddenLayers,hiddenNeurons,outputNeurons);
		r.activator = activator;
		r.outputActivator = outputActivator;
		r.learningRate = learningRate;
		for (int i = 0; i < r.layerValues.length; i++) {
			r.layerValues[i] = layerValues[i].copy();
			r.layerWeights[i] = layerWeights[i].copy();
			r.layerBiases[i] = layerBiases[i].copy();
		}
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("inputNeurons","" + inputNeurons));
		json.rootElement.children.add(new JsElem("hiddenLayers","" + hiddenLayers));
		json.rootElement.children.add(new JsElem("hiddenNeurons","" + hiddenNeurons));
		json.rootElement.children.add(new JsElem("outputNeurons","" + outputNeurons));
		json.rootElement.children.add(new JsElem("activator",activator.getClass().getName(),true));
		String outAct = "";
		if (outputActivator!=null) {
			outAct = outputActivator.getClass().getName();
		}
		json.rootElement.children.add(new JsElem("outputActivator",outAct,true));
		json.rootElement.children.add(new JsElem("learningRate","" + learningRate));
		
		JsElem valuesElem = new JsElem("values",true);
		json.rootElement.children.add(valuesElem);
		for (int i = 0; i < layerValues.length; i++) {
			valuesElem.children.add(new JsElem(null,layerValues[i].toStringBuilder(),true));
		}
		
		JsElem weightsElem = new JsElem("weights",true);
		json.rootElement.children.add(weightsElem);
		for (int i = 0; i < layerValues.length; i++) {
			weightsElem.children.add(new JsElem(null,layerWeights[i].toStringBuilder(),true));
		}
		
		JsElem biasesElem = new JsElem("biases",true);
		json.rootElement.children.add(biasesElem);
		for (int i = 0; i < layerValues.length; i++) {
			biasesElem.children.add(new JsElem(null,layerBiases[i].toStringBuilder(),true));
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			int inputNeurons = json.rootElement.getChildInt("inputNeurons",this.inputNeurons);
			int hiddenLayers = json.rootElement.getChildInt("hiddenLayers",this.hiddenLayers);
			int hiddenNeurons = json.rootElement.getChildInt("hiddenNeurons",this.hiddenNeurons);
			int outputNeurons = json.rootElement.getChildInt("outputNeurons",this.outputNeurons);
			initialize(inputNeurons,hiddenLayers,hiddenNeurons,outputNeurons);

			activator = StaticFunctions.getActivatorByClassName(json.rootElement.getChildString("activator"));
			outputActivator = StaticFunctions.getActivatorByClassName(json.rootElement.getChildString("outputActivator"));
			learningRate = json.rootElement.getChildFloat("learningRate",this.learningRate);
			
			JsElem valuesElem = json.rootElement.getChildByName("values");
			if (valuesElem!=null && valuesElem.children.size()==layerValues.length) {
				for (int i = 0; i < layerValues.length; i++) {
					layerValues[i] = ZMatrix.fromStringBuilder(valuesElem.children.get(i).value);
				}
			}
			
			JsElem weightsElem = json.rootElement.getChildByName("weights");
			if (weightsElem!=null && weightsElem.children.size()==layerValues.length) {
				for (int i = 0; i < layerValues.length; i++) {
					layerWeights[i] = ZMatrix.fromStringBuilder(weightsElem.children.get(i).value);
				}
			}
			
			JsElem biasesElem = json.rootElement.getChildByName("biases");
			if (biasesElem!=null && biasesElem.children.size()==layerValues.length) {
				for (int i = 0; i < layerValues.length; i++) {
					layerBiases[i] = ZMatrix.fromStringBuilder(biasesElem.children.get(i).value);
				}
			}
		}
	}

	/**
	 * Initializes the neural network with the specified dimensions.
	 * 
	 * @param inputNeurons The number of input neurons
	 * @param hiddenLayers The number of hidden layers
	 * @param hiddenNeurons The number of neurons per hidden layer
	 * @param outputNeurons The number of output neurons
	 */
	public void initialize(int inputNeurons, int hiddenLayers, int hiddenNeurons, int outputNeurons) {
		if (inputNeurons<1) {
			inputNeurons = 1;
		}
		if (hiddenLayers<1) {
			hiddenLayers = 1;
		}
		if (hiddenNeurons<1) {
			hiddenNeurons = 1;
		}
		if (outputNeurons<1) {
			outputNeurons = 1;
		}
		this.inputNeurons = inputNeurons;
		this.hiddenLayers = hiddenLayers;
		this.hiddenNeurons = hiddenNeurons;
		this.outputNeurons = outputNeurons;

		int totalLayers = hiddenLayers + 2;
		
		layerValues = new ZMatrix[totalLayers];
		layerWeights = new ZMatrix[totalLayers];
		layerBiases = new ZMatrix[totalLayers];
		int lNeurons = inputNeurons;
		int pNeurons = inputNeurons;
		for (int i = 0; i < layerValues.length; i++) {
			if (i == layerValues.length - 1) {
				lNeurons = outputNeurons;
			}
			layerValues[i] = new ZMatrix(lNeurons,1);
			if (i==0) {
				layerWeights[i] = new ZMatrix(1,1);
				layerBiases[i] = new ZMatrix(1,1);
			} else {
				layerWeights[i] = new ZMatrix(lNeurons,pNeurons);
				layerBiases[i] = new ZMatrix(lNeurons,1);
				pNeurons = lNeurons;
			}
			lNeurons = hiddenNeurons;
		}
	}
	
	protected float[] feedForward(float[] inputs) {
		resetValues();
		getInputValues().fromArray(inputs);
		int p = 0;
		for (int i = 1; i < layerValues.length; i++) {
			layerValues[i] = ZMatrix.multiply(layerWeights[i],layerValues[p]);
			layerValues[i].add(layerBiases[i]);
			if (i == layerValues.length - 1 && outputActivator!=null) {
				layerValues[i].applyFunction(outputActivator);
				if (outputActivator==StaticFunctions.SOFTMAX_TOP) {
					float total = getOutputValues().getColumnValuesAdded(0);
					if (total!=0) {
						getOutputValues().divide(total);
					}
				}
			} else {
				layerValues[i].applyFunction(activator);
			}
			p++;
		}
		return getOutputValues().toArray();
	}

	protected void feedBackward(float[] errs) {
		ZMatrix errors = ZMatrix.getFromArray(errs);
		
		int p = layerValues.length - 2;
		for (int i = (layerValues.length - 1); i > 0; i--) {
			ZMatrix gradients = layerValues[i].copy();
			if (i == layerValues.length - 1 && outputActivator!=null) {
				gradients.applyFunction(outputActivator.getDerivative());
				if (outputActivator==StaticFunctions.SOFTMAX_TOP) {
					// TODO: Replace softmax derivative shortcut with a real implementation
				}
			} else {
				gradients.applyFunction(activator.getDerivative());
			}
			gradients.multiply(errors);
			gradients.multiply(learningRate);

			ZMatrix pTValues = ZMatrix.transpose(layerValues[p]);
			ZMatrix deltas = ZMatrix.multiply(gradients,pTValues);
			layerWeights[i].add(deltas); 
			
			layerBiases[i].add(gradients);
			
			ZMatrix tWeights = ZMatrix.transpose(layerWeights[i]);
			errors = ZMatrix.multiply(tWeights,errors);
			p--;
		}
	}

	protected void resetValues() {
		for (int i = 0; i < layerValues.length; i++) {
			layerValues[i].set(0.0F);
		}
	}
	
	protected ZMatrix getInputValues() {
		return layerValues[0];
	}
	
	protected ZMatrix getOutputValues() {
		return layerValues[layerValues.length - 1];
	}
	
	protected NeuralNet getNewNeuralNet(int inputNeurons, int hiddenLayers, int hiddenNeurons, int outputNeurons) {
		return new NeuralNet(inputNeurons,hiddenLayers,hiddenNeurons,outputNeurons);
	}
}
