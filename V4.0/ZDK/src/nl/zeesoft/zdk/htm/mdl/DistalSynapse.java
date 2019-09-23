package nl.zeesoft.zdk.htm.mdl;

public class DistalSynapse extends Synapse {
	public Cell source	= null;
	
	public DistalSynapse(String segmentId,int index) {
		super(segmentId,index);
	}
}
