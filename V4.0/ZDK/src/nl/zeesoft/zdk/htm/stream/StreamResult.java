package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.util.SDR;

public class StreamResult {
	protected long		added		= System.nanoTime();
	
	public long			id			= 0;
	public SDR			inputSDR	= null;
	public List<SDR>	outputSDRs	= new ArrayList<SDR>();
}
