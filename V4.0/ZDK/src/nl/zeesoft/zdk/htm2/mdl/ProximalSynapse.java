package nl.zeesoft.zdk.htm2.mdl;

public class ProximalSynapse extends Synapse {
	public int 		inputIndex	= 0;
	public float	permanence	= 0;
	
	public ProximalSynapse(String dendriteId,int inputIndex) {
		super(dendriteId);
		this.inputIndex = inputIndex;
	}

	@Override
	public ProximalSynapse copy() {
		ProximalSynapse copy = new ProximalSynapse(dendriteId,inputIndex);
		copy.setId(getId());
		copy.permanence = permanence;
		return copy;
	}
}
