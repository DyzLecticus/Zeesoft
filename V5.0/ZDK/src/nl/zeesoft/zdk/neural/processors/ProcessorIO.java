package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;

public class ProcessorIO {
	public String				id							= "";
	public boolean				sequential					= false;
	public boolean				learn						= true;
	public int					timeoutMs					= 1000;
	public List<SDR>			inputs						= new ArrayList<SDR>();
	public List<SDR>			outputs						= null;
	public Str					error						= new Str();
}
