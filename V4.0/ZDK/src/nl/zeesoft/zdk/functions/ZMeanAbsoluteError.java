package nl.zeesoft.zdk.functions;

public class ZMeanAbsoluteError implements ZLossFunction {
	protected ZMeanAbsoluteError() {
		
	}

	@Override
	public float calculateLoss(float[] outputs, float[] expectations) {
		float r = 0;
		for (int i = 0; i < outputs.length; i++) {
			float diff = expectations[i] - outputs[i];
			if (diff<0) {
				diff = diff * -1;
			}
			r += diff;
		}
		r = r / outputs.length;
		return r;
	}
}
