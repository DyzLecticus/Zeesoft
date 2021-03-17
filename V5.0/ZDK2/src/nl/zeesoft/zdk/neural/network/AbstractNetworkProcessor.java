package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;

public class AbstractNetworkProcessor {
	public int				layer		= 0;
	public String			name		= "";
	public List<LinkConfig>	inputLinks	= new ArrayList<LinkConfig>();
}
