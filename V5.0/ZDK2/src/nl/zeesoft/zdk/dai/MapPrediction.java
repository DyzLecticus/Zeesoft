package nl.zeesoft.zdk.dai;

public class MapPrediction {
	public ObjMap	predictedMap	= null;
	
	public int		votes			= 0;
	public float	support			= 0F;
	
	public MapPrediction() {
		
	}
	
	public MapPrediction(ObjMap pm) {
		this.predictedMap = pm;
	}
	
	@Override
	public String toString() {
		return predictedMap + ", support: " + support;
	}
}
