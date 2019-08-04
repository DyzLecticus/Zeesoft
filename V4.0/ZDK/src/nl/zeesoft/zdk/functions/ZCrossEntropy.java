package nl.zeesoft.zdk.functions;

public class ZCrossEntropy implements ZLossFunction {
	private static float	EPSILON		= 0.0000000001F;
	
	protected ZCrossEntropy() {
		
	}

	@Override
	public float calculateLoss(float[] outputs, float[] expectations) {
		float r = 0;
		for (int i = 0; i < outputs.length; i++) {
			float out = outputs[i];
			if (out<EPSILON) {
				out = EPSILON;
			} else if (out > 1 - EPSILON) {
				out = 1 - EPSILON;
			}
			r += expectations[i] * Math.log(out);
		}
		r = 1 / outputs.length * r;
		return -r;
	}
}
