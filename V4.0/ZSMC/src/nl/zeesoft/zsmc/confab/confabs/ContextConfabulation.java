package nl.zeesoft.zsmc.confab.confabs;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confab.Module;

public class ContextConfabulation extends ConfabulationObject {
	public List<ContextResult>	results		= new ArrayList<ContextResult>();
	
	@Override
	public void initialize(Messenger msgr, WorkerUnion uni) {
		super.initialize(msgr,uni);
		modules.add(new Module(msgr));
	}
}
