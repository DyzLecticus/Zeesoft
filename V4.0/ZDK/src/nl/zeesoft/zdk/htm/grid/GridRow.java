package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class GridRow extends Worker {
	protected int 				index	= 0;
	protected List<GridColumn>	columns	= new ArrayList<GridColumn>();

	protected GridRow(Messenger msgr, WorkerUnion union) {
		super(msgr, union);
	}
	
	protected void addColumn() {
		GridColumn r = new GridColumn(getMessenger(),getUnion());
		r.index = columns.size();
		columns.add(r);
	}
	
	@Override
	public void start() {
		for (GridColumn col: columns) {
			col.start();
		}
		super.start();
	}
	
	@Override
	public void stop() {
		for (GridColumn col: columns) {
			col.stop();
		}
		super.stop();
	}
	
	protected void destroy() {
		for (GridColumn col: columns) {
			col.destroy();
		}
		columns.clear();
	}

	@Override
	protected void whileWorking() {
		// TODO Auto-generated method stub
		
	}
}
