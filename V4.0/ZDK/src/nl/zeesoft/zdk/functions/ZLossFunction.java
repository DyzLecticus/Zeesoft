package nl.zeesoft.zdk.functions;

public interface ZLossFunction {
	public float calculateLoss(float[] outputs,float[] expectations);
}
