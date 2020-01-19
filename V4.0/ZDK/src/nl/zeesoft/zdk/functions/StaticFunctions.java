package nl.zeesoft.zdk.functions;

import java.math.BigInteger;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * StaticFunctions contains a set of common and specialized functions that can be used for functional programming of basic mathematics.
 */
public class StaticFunctions {
	// Parameterized functions
	public static ZAdd								ADD					= new ZAdd();
	public static ZSubtract							SUBTRACT			= new ZSubtract();
	public static ZMultiply							MULTIPLY			= new ZMultiply();
	public static ZDivide							DIVIDE				= new ZDivide();
	public static ZSet								SET					= new ZSet();
	
	public static ZParamFunction[]					PARAM_FUNCTIONS		= {ADD,SUBTRACT,MULTIPLY,DIVIDE,SET};
	
	// Activators
	public static ZSigmoid							SIGMOID				= new ZSigmoid();
	public static ZSigmoidDerivative				SIGMOID_DER			= new ZSigmoidDerivative();
	public static ZTanH								TANH				= new ZTanH();
	public static ZTanHDerivative					TANH_DER			= new ZTanHDerivative();
	public static ZReLU								RELU				= new ZReLU();
	public static ZReLUDerivative					RELU_DER			= new ZReLUDerivative();
	public static ZLeakyReLU						LEAKY_RELU			= new ZLeakyReLU();
	public static ZLeakyReLUDerivative				LEAKY_RELU_DER		= new ZLeakyReLUDerivative();
	public static ZSoftmaxTop						SOFTMAX_TOP			= new ZSoftmaxTop();
	public static ZSoftmaxDerivative				SOFTMAX_DER			= new ZSoftmaxDerivative();
	
	public static ZActivator[]						ACTIVATORS			= {SIGMOID,TANH,RELU,LEAKY_RELU};
	public static ZActivator[]						OUTPUT_ACTIVATORS	= {SIGMOID,TANH,RELU,LEAKY_RELU,SOFTMAX_TOP};
	
	// Loss functions
	public static ZMeanAbsoluteError				MEAN_ABSOLUTE_ERROR	= new ZMeanAbsoluteError();
	public static ZMeanSquaredError					MEAN_SQUARED_ERROR	= new ZMeanSquaredError();
	
	public static ZLossFunction[]					LOSS_FUNCTIONS		= {MEAN_ABSOLUTE_ERROR,MEAN_SQUARED_ERROR};
	
	// Weight functions
	public static ZWeightDefault					WEIGHT_DEFAULT		= new ZWeightDefault();
	public static ZWeightKaiming					WEIGHT_KAIMING		= new ZWeightKaiming();
	public static ZWeightXavier						WEIGHT_XAVIER		= new ZWeightXavier();
	public static ZWeightZero						WEIGHT_ZERO			= new ZWeightZero();
	
	public static ZWeightFunction[]					WEIGHT_FUNCTIONS	= {WEIGHT_DEFAULT,WEIGHT_KAIMING,WEIGHT_XAVIER};
	public static ZWeightFunction[]					BIAS_FUNCTIONS		= {WEIGHT_DEFAULT,WEIGHT_KAIMING,WEIGHT_XAVIER,WEIGHT_ZERO};
	
	private static SortedMap<Integer,BigInteger>	factorialCache		= new TreeMap<Integer,BigInteger>();
	
	public static ZActivator getActivatorByClassName(String name) {
		return (ZActivator) getObjectFromArrayByClassName(OUTPUT_ACTIVATORS,name);
	}
	
	public static ZLossFunction getLossFunctionByClassName(String name) {
		return (ZLossFunction) getObjectFromArrayByClassName(LOSS_FUNCTIONS,name);
	}
	
	public static ZWeightFunction getWeightFunctionByClassName(String name) {
		return (ZWeightFunction) getObjectFromArrayByClassName(BIAS_FUNCTIONS,name);
	}

	public static BigInteger factorial(int number) {
		BigInteger r = factorialCache.get(number);
		if (r==null) {
			r = factorial(BigInteger.valueOf(number));
			factorialCache.put(number,r);
		}
		return r;
	}
	
	public static BigInteger factorial(BigInteger number) {
		BigInteger r = BigInteger.valueOf(1);
		for (long factor = 2; factor <= number.longValue(); factor++) {
			r = r.multiply(BigInteger.valueOf(factor));
		}
		return r;
	}

	/**
	 * Returns the distance between two points in a three dimensional coordinate system assuming all coordinates are greater or equal to zero.
	 * 
	 * @param posXA X-axis position of point A
	 * @param posYA Y-axis position of point A
	 * @param posZA Z-axis position of point A
	 * @param posXB X-axis position of point B
	 * @param posYB Y-axis position of point B
	 * @param posZB Z-axis position of point B
	 * @return The distance
	 */
	public static int getDistance(int posXA, int posYA, int posZA, int posXB, int posYB, int posZB) {
		return (int) getDistance((float)posXA,(float)posYA,(float)posZA,(float)posXB,(float)posYB,(float)posZB);
	}

	/**
	 * Returns the distance between two points in a three dimensional coordinate system assuming all coordinates are greater or equal to zero.
	 * 
	 * @param posXA X-axis position of point A
	 * @param posYA Y-axis position of point A
	 * @param posZA Z-axis position of point A
	 * @param posXB X-axis position of point B
	 * @param posYB Y-axis position of point B
	 * @param posZB Z-axis position of point B
	 * @return The distance
	 */
	public static float getDistance(float posXA, float posYA, float posZA, float posXB, float posYB, float posZB) {
		float r = 0;
		float distX = 0;
		float distY = 0;
		float distZ = 0;
		if (posXA>posXB) {
			distX = posXA - posXB;
		} else {
			distX = posXB - posXA;
		}
		if (posYA>posYB) {
			distY = posYA - posYB;
		} else {
			distY = posYB - posYA;
		}
		if (posZA>posZB) {
			distZ = posZA - posZB;
		} else {
			distZ = posZB - posZA;
		}
		r = (float) Math.sqrt((distX * distX) + (distY * distY));
		r = (float) Math.sqrt((r * r) + (distZ * distZ));
		return r;
	}
	
	private static Object getObjectFromArrayByClassName(Object[] objects, String name) {
		Object r = null;
		if (name.length()>0) {
			for (int i = 0; i < objects.length; i++) {
				if (objects[i].getClass().getName().equals(name)) {
					r = objects[i];
					break;
				}
			}
		}
		return r;
	}
}
