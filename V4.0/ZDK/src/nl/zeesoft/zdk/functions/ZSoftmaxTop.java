package nl.zeesoft.zdk.functions;

public class ZSoftmaxTop implements ZFunction {
	@Override
	public float applyFunction(float v) {
		if (v < 0) {
			v = v * -1;
		}
		return v > 0 ? (float) Math.pow(Math.E,v) : 0;
	}
}
