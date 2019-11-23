package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class Grid extends Worker {
	private GridColumnEncoder[]		encoders	= null;
	private List<GridRow>			rows		= new ArrayList<GridRow>();
	
	private long					uid			= 0;
	private Queue<GridRequest>		requests	= new LinkedList<GridRequest>();

	public Grid(int rows, int columns) {
		super(null,null);
		initialize(rows,columns);
	}
	
	public Grid(Messenger msgr, WorkerUnion union,int rows, int columns) {
		super(msgr, union);
		initialize(rows,columns);
	}

	public void setEncoder(int columnIndex,GridColumnEncoder encoder) {
		lockMe(this);
		encoders[columnIndex] = encoder;
		unlockMe(this);
	}
	
	public void addRequest(GridRequest request) {
		lockMe(this);
		uid++;
		request.id = uid;
		requests.add(request);
		unlockMe(this);
	}
	
	@Override
	public void start() {
		for (GridRow row: rows) {
			row.start();
		}
		super.start();
	}
	
	@Override
	public void stop() {
		for (GridRow row: rows) {
			row.stop();
		}
		super.stop();
	}
	
	public void destroy() {
		for (GridRow row: rows) {
			row.destroy();
		}
		rows.clear();
	}
	
	@Override
	protected void whileWorking() {
		// TODO Auto-generated method stub
		// Take from queue 
	}
	
	protected void addRow() {
		GridRow r = new GridRow(getMessenger(),getUnion());
		r.index = rows.size();
		rows.add(r);
	}

	protected void addColumn(int rowIndex) {
		if (rows.size()>rowIndex) {
			GridRow row = rows.get(rowIndex);
			row.addColumn();
		}
	}

	protected void initialize(int rows, int columns) {
		encoders = new GridColumnEncoder[columns];
		for (int r = 0; r < rows; r++) {
			addRow();
			for (int c = 0; c < columns; c++) {
				addColumn(r);
			}
		}
	}
}
