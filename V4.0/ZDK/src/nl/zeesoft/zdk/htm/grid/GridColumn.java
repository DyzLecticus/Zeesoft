package nl.zeesoft.zdk.htm.grid;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class GridColumn extends Worker {
	protected int 				index	= 0;

	protected GridColumn(Messenger msgr, WorkerUnion union) {
		super(msgr, union);
	}

	protected void destroy() {
		
	}
	
	@Override
	protected void whileWorking() {
		// TODO Auto-generated method stub
		
	}
}
