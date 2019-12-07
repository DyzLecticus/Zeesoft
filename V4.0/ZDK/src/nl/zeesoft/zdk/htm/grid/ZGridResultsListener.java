package nl.zeesoft.zdk.htm.grid;

/**
 * The ZGridResultsListener interface is used to listen to a ZGrid for results. 
 */
public interface ZGridResultsListener {
	public void processedRequest(ZGrid grid,ZGridResult result);
}
