package nl.zeesoft.zsd.dialog;

public class DialogVariableValue {
	public String	name			= "";
	public String	internalValue	= "";
	public String	externalValue	= "";
	
	public DialogVariableValue copy() {
		DialogVariableValue r = new DialogVariableValue();
		r.name = name;
		r.internalValue = internalValue;
		r.externalValue = externalValue;
		return r;
	}
}
