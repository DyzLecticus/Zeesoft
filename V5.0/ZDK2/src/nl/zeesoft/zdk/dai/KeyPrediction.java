package nl.zeesoft.zdk.dai;

public class KeyPrediction {
	public String	key				= "";
	public Object	predictedValue	= null;
	public float	support			= 0F;
	public float	confidence		= 0F;
	
	public KeyPrediction() {
		
	}
	
	public KeyPrediction(String key, Object predictedValue) {
		this.key = key;
		this.predictedValue = predictedValue;
	}
}
