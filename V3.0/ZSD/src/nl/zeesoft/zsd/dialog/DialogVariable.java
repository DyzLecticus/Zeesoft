package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringSymbolParser;

public class DialogVariable {
	public String					name			= "";
	public String					type			= "";
	public String					complexName		= "";
	public String					complexType		= "";
	public String					initialValue	= "";
	public List<DialogVariableQA>	examples		= new ArrayList<DialogVariableQA>();
	
	public ZStringSymbolParser getQuestion(boolean random) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		int num = 0;
		if (random && examples.size()>1) {
			ZIntegerGenerator generator = new ZIntegerGenerator(0,(examples.size() - 1));
			num = generator.getNewInteger();
		}
		if (num<examples.size()) {
			r = examples.get(num).question;
		}
		return r;
	}
}
