package nl.zeesoft.zdk.functions;

public class ZMeanSquaredError implements ZLossFunction {
	protected ZMeanSquaredError() {
		
	}

	@Override
	public float calculateLoss(float[] outputs, float[] expectations) {
		float r = 0;
		if (outputs.length>0) {
			for (int i = 0; i < outputs.length; i++) {
				r += Math.pow(expectations[i] - outputs[i],2);
			}
			r = r / outputs.length;
		}
		return r;
	}
}
