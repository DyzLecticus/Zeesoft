package nl.zeesoft.zdk.matrix.functions;

import nl.zeesoft.zdk.matrix.ZActivator;
import nl.zeesoft.zdk.matrix.ZFunction;

public class ZTanH implements ZActivator {
	@Override
	public float applyFunction(float v) {
		return tanh(v);
	}

	@Override
	public ZFunction getDerivative() {
		return new ZTanHDerivative();
	}

	public static float tanh(float x) {
		float r = 0;
		if (x > 0) {
			r = (float) Math.tanh(x);
		} else if (x < 0) {
			r = (float) Math.tanh(x * -1) * -1;
		}
	    return r;
	}
}
