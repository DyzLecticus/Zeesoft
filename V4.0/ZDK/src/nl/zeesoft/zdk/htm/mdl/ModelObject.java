package nl.zeesoft.zdk.htm.mdl;

public abstract class ModelObject {
	private String id	= "";
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public abstract ModelObject copy();
}
