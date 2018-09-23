package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class CorrectionConfabulation extends ConfabulationObject {
	public String				contextSymbol	= "";
	public boolean				expect			= true;
	public String				alphabet		= "";
	
	public ZStringSymbolParser	corrected		= new ZStringSymbolParser();
	public List<Correction>		corrections		= new ArrayList<Correction>();
	
	@Override
	public void initialize() {
		super.initialize();
		for (int m = 0; m < symbols.size(); m++) {
			Module mod = new Module();
			modules.add(mod);
		}
	}
}

