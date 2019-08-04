package nl.zeesoft.zdk.functions;

public class StaticFunctions {
	// Parameterized functions
	public static ZAdd						ADD					= new ZAdd();
	public static ZSubtract					SUBTRACT			= new ZSubtract();
	public static ZMultiply					MULTIPLY			= new ZMultiply();
	public static ZDivide					DIVIDE				= new ZDivide();
	public static ZSet						SET					= new ZSet();
	
	public static ZParamFunction[]			PARAM_FUNCTIONS		= {ADD,SUBTRACT,MULTIPLY,DIVIDE,SET};
	
	// Activators
	public static ZSigmoid					SIGMOID				= new ZSigmoid();
	public static ZSigmoidDerivative		SIGMOID_DER			= new ZSigmoidDerivative();
	public static ZTanH						TANH				= new ZTanH();
	public static ZTanHDerivative			TANH_DER			= new ZTanHDerivative();
	public static ZReLU						RELU				= new ZReLU();
	public static ZReLUDerivative			RELU_DER			= new ZReLUDerivative();
	public static ZLeakyReLU				LEAKY_RELU			= new ZLeakyReLU();
	public static ZLeakyReLUDerivative		LEAKY_RELU_DER		= new ZLeakyReLUDerivative();
	public static ZSoftmaxTop				SOFTMAX_TOP			= new ZSoftmaxTop();
	public static ZSoftmaxDerivative		SOFTMAX_DER			= new ZSoftmaxDerivative();
	
	public static ZActivator[]				ACTIVATORS			= {SIGMOID,TANH,RELU,LEAKY_RELU};
	public static ZActivator[]				OUTPUT_ACTIVATORS	= {SIGMOID,TANH,RELU,LEAKY_RELU,SOFTMAX_TOP};
	
	// Loss functions
	public static ZMeanAbsoluteError		MEAN_ABSOLUTE_ERROR	= new ZMeanAbsoluteError();
	public static ZMeanSquaredError			MEAN_SQUARED_ERROR	= new ZMeanSquaredError();
	
	public static ZLossFunction[]			LOSS_FUNCTIONS		= {MEAN_ABSOLUTE_ERROR,MEAN_SQUARED_ERROR};
	
	public static ZActivator getActivatorByClassName(String name) {
		ZActivator r = null;
		if (name.length()>0) {
			for (int i = 0; i < OUTPUT_ACTIVATORS.length; i++) {
				if (OUTPUT_ACTIVATORS[i].getClass().getName().equals(name)) {
					r = OUTPUT_ACTIVATORS[i];
					break;
				}
			}
		}
		return r;
	}
	
	public static ZLossFunction getLossFunctionByClassName(String name) {
		ZLossFunction r = null;
		if (name.length()>0) {
			for (int i = 0; i < LOSS_FUNCTIONS.length; i++) {
				if (LOSS_FUNCTIONS[i].getClass().getName().equals(name)) {
					r = LOSS_FUNCTIONS[i];
					break;
				}
			}
		}
		return r;
	}
}
