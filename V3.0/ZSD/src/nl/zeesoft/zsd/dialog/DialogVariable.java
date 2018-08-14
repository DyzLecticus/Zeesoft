package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringSymbolParser;

public class DialogVariable {
	public String						name			= "";
	public String						type			= "";
	public String						complexName		= "";
	public String						complexType		= "";
	public String						initialValue	= "";
	public boolean						overwrite		= true;
	public boolean						session			= false;
	public List<DialogVariablePrompt>	prompts			= new ArrayList<DialogVariablePrompt>();
	
	public ZStringSymbolParser getPrompt(boolean random) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		int num = 0;
		if (random && prompts.size()>1) {
			ZIntegerGenerator generator = new ZIntegerGenerator(0,(prompts.size() - 1));
			num = generator.getNewInteger();
		}
		if (num<prompts.size()) {
			r = prompts.get(num).prompt;
		}
		return r;
	}
}
