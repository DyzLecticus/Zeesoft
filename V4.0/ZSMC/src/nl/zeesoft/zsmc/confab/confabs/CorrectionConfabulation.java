package nl.zeesoft.zsmc.confab.confabs;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.Module;

public class CorrectionConfabulation extends ConfabulationObject {
	public String				contextSymbol	= "";
	public boolean				validate		= true;
	public boolean				parallel		= true;
	public String				alphabet		= "";
	
	public ZStringSymbolParser	corrected		= new ZStringSymbolParser();
	public List<Correction>		corrections		= new ArrayList<Correction>();
	
	@Override
	public void initialize(Messenger msgr, WorkerUnion uni) {
		super.initialize(msgr,uni);
		for (int m = 0; m < symbols.size(); m++) {
			Module mod = new Module(msgr);
			modules.add(mod);
		}
	}
}

