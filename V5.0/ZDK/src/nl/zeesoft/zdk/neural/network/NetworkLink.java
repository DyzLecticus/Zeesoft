package nl.zeesoft.zdk.neural.network;

public class NetworkLink {
	public String					fromName	= "";
	public int						fromIndex	= 0;
	public String					toName		= "";
	public int						toIndex		= 0;
	
	public NetworkLink() {
		
	}
	
	public NetworkLink(String fromName, int fromIndex, String toName, int toIndex) {
		this.fromName = fromName;
		this.fromIndex = fromIndex;
		this.toName = toName;
		this.toIndex = toIndex;
	}
}
