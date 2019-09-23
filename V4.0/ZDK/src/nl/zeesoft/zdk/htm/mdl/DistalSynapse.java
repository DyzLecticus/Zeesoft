package nl.zeesoft.zdk.htm.mdl;

public class DistalSynapse extends Synapse {
	public String 	sourceCellId		= "";
	
	public DistalSynapse(String dendriteId,String sourceCellId) {
		super(dendriteId);
		this.sourceCellId = sourceCellId;
	}

	@Override
	public DistalSynapse copy() {
		DistalSynapse copy = new DistalSynapse(dendriteId,sourceCellId);
		copy.setId(getId());
		copy.permanence = permanence;
		return copy;
	}
}
