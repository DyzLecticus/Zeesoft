package nl.zeesoft.zdk.functions;

public class ZAdd implements ZParamFunction {
	protected ZAdd() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v,float p) {
		return v + p;
	}
}
