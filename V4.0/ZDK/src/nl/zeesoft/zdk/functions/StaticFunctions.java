package nl.zeesoft.zdk.functions;

public class StaticFunctions {
	public static ZAdd						ADD					= new ZAdd();
	public static ZSubtract					SUBTRACT			= new ZSubtract();
	public static ZMultiply					MULTIPLY			= new ZMultiply();
	public static ZDivide					DIVIDE				= new ZDivide();
	public static ZSet						SET					= new ZSet();
	
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
	
	public static ZMeanSquaredError			MEAN_SQUARED_ERROR	= new ZMeanSquaredError();
	public static ZCrossEntropy				CROSS_ENTROPY		= new ZCrossEntropy();
	
	public static ZActivator[]				ACTIVATORS			= {SIGMOID,TANH,RELU,LEAKY_RELU};
	public static ZActivator[]				OUTPUT_ACTIVATORS	= {SIGMOID,TANH,RELU,LEAKY_RELU,SOFTMAX_TOP};
}
