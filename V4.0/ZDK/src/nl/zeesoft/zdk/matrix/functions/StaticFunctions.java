package nl.zeesoft.zdk.matrix.functions;

public class StaticFunctions {
	public static ZAdd						ADD			= new ZAdd();
	public static ZSubtract					SUBTRACT	= new ZSubtract();
	public static ZMultiply					MULTIPLY	= new ZMultiply();
	public static ZDivide					DIVIDE		= new ZDivide();
	public static ZSet						SET			= new ZSet();
	
	public static ZSigmoid					SIGMOID		= new ZSigmoid();
	public static ZSigmoidDerivative		SIGMOID_DER	= new ZSigmoidDerivative();
	public static ZTanH						TANH		= new ZTanH();
	public static ZTanHDerivative			TANH_DER	= new ZTanHDerivative();
	public static ZReLU						RELU		= new ZReLU();
	public static ZReLUDerivative			RELU_DER	= new ZReLUDerivative();
	public static ZLeakyReLU				L_RELU		= new ZLeakyReLU();
	public static ZLeakyReLUDerivative		L_RELU_DER	= new ZLeakyReLUDerivative();
	
	public static ZSoftmaxTop				SOFTMAX_TOP	= new ZSoftmaxTop();
}
