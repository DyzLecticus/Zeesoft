package nl.zeesoft.zdk.functions;

public class ZMeanAbsoluteError implements ZLossFunction {
	protected ZMeanAbsoluteError() {
		
	}

	@Override
	public float calculateLoss(float[] outputs, float[] expectations) {
		float r = 0;
		for (int i = 0; i < outputs.length; i++) {
			r += expectations[i] - outputs[i];
		}
		r = r / outputs.length;
		if (r<0) {
			r = r * -1;
		}
		return r;
	}
}
