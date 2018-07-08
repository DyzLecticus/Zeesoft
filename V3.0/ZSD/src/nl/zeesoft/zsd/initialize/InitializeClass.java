package nl.zeesoft.zsd.initialize;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

public class InitializeClass {
	public String 			name		= "";
	public String 			className	= "";
	public List<String>		fileNames	= new ArrayList<String>();
	public long				ms			= 0;
	public Initializable	obj			= null;
	public ZStringBuilder 	errors		= new ZStringBuilder();
}
