package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;

public class DialogVariable {
	public String					name		= "";
	public String					type		= "";
	public String					complexName	= "";
	public String					complexType	= "";
	public List<DialogVariableQA>	examples	= new ArrayList<DialogVariableQA>();
}
