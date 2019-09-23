package nl.zeesoft.zdk.htm.mdl;

public class Synapse implements ModelObject {
	public String	segmentId	= "";
	public int		index		= 0;
	
	public Synapse(String segmentId,int index) {
		this.segmentId = segmentId;
		this.index = index;
	}
	
	@Override
	public String getId() {
		return getClass().getSimpleName() + ":" + segmentId + "-" + index;
	}
}
