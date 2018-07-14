package nl.zeesoft.zsd.dialog;

public class DialogVariableValue {
	public String	name			= "";
	public String	internalValue	= "";
	public String	typeValue		= "";
	
	public DialogVariableValue copy() {
		DialogVariableValue r = new DialogVariableValue();
		r.name = name;
		r.internalValue = internalValue;
		r.typeValue = typeValue;
		return r;
	}
}
