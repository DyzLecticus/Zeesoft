package nl.zeesoft.zdk.htm2.mdl;

public abstract class Dendrite extends ModelObject {
	public String			cellId			= "";
	
	public Dendrite(String cellId) {
		this.cellId = cellId;
	}
}
