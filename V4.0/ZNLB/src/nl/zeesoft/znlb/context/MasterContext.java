package nl.zeesoft.znlb.context;

import java.util.ArrayList;
import java.util.List;

public class MasterContext {
	public String			name		= "";
	public String			desc		= "";
	public List<Context>	contexts	= new ArrayList<Context>();
	
	public void addContext(String name, String desc) {
		Context c = new Context();
		c.name = name;
		c.desc = desc;
		contexts.add(c);
	}
}
