package nl.zeesoft.zsmc.confab.confabs;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.Module;

public class SynonymConfabulation extends ConfabulationObject {
	public String				contextSymbol	= "";
	public int					width			= 4;
	public boolean				parallel		= true;
	
	public List<SynonymResult>	results			= new ArrayList<SynonymResult>();

	@Override
	public void initialize(Messenger msgr, WorkerUnion uni) {
		super.initialize(msgr,uni);
		for (int m = 0; m < ((width * 2) + 1); m++) {
			Module mod = new Module(msgr);
			modules.add(mod);
		}
	}
}
