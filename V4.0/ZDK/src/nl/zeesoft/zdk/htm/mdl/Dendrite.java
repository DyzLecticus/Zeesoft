package nl.zeesoft.zdk.htm.mdl;

public abstract class Dendrite extends ModelObject {
	public String			cellId			= "";
	
	public Dendrite(String cellId) {
		this.cellId = cellId;
	}
}
