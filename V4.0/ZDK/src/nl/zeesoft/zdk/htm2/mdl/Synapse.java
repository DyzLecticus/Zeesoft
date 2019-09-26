package nl.zeesoft.zdk.htm2.mdl;

public abstract class Synapse extends ModelObject {
	public String	dendriteId	= "";
	public float	permanence	= 0;

	public Synapse(String dendriteId) {
		this.dendriteId = dendriteId;
	}
}
