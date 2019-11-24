package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.proc.ProcessorObject;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ZGrid extends Worker implements ZGridRequestNext {
	private List<ZGridRow>		rows		= new ArrayList<ZGridRow>();
	private ZGridResults			results		= null;
	
	private List<ZGridListener>	listeners	= new ArrayList<ZGridListener>();

	public ZGrid(int rows, int columns) {
		super(null,null);
		initialize(null,rows,columns);
	}
	
	public ZGrid(Messenger msgr, WorkerUnion union,int rows, int columns) {
		super(msgr, union);
		initialize(msgr,rows,columns);
	}
	
	public void addListener(ZGridListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}

	public void setEncoder(int columnIndex,ZGridColumnEncoder encoder) {
		lockMe(this);
		rows.get(0).columns.get(columnIndex).encoder = encoder;
		unlockMe(this);
	}

	public void setProcessor(int rowIndex,int columnIndex,ProcessorObject processor) {
		lockMe(this);
		rows.get(rowIndex).columns.get(columnIndex).processor = processor;
		unlockMe(this);
	}
	
	public long addRequest(ZGridRequest request) {
		lockMe(this);
		long r = results.assignRequestId(request);
		rows.get(0).addRequest(request);
		unlockMe(this);
		return r;
	}
	
	@Override
	public void start() {
		for (ZGridRow row: rows) {
			row.start();
		}
		super.start();
	}
	
	@Override
	public void stop() {
		for (ZGridRow row: rows) {
			row.stop();
		}
		super.stop();
	}
	
	public void destroy() {
		for (ZGridRow row: rows) {
			row.destroy();
		}
		rows.clear();
	}

	@Override
	public void processedRequest(ZGridRequest request) {
		results.addResult(request);
	}
	
	@Override
	protected void whileWorking() {
		List<ZGridRequest> requests = results.flush();
		if (requests.size()>0) {
			lockMe(this);
			List<ZGridListener> list = new ArrayList<ZGridListener>(listeners);
			unlockMe(this);
			for (ZGridRequest request: requests) {
				for (ZGridListener listener: list) {
					try {
						listener.processedRequest(request);
					} catch(Exception e) {
						if (getMessenger()!=null) {
							getMessenger().error(this,"Grid listener exception",e);
						} else {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	protected ZGridRow addRow() {
		ZGridRow r = new ZGridRow(getMessenger(),getUnion());
		r.index = rows.size();
		rows.add(r);
		return r;
	}

	protected void addColumn(int rowIndex) {
		if (rows.size()>rowIndex) {
			ZGridRow row = rows.get(rowIndex);
			row.addColumn();
		}
	}

	protected void initialize(Messenger msgr,int rows, int columns) {
		setSleep(1);
		ZGridRow pRow = null;
		for (int r = 0; r < rows; r++) {
			ZGridRow row = addRow();
			if (pRow!=null) {
				pRow.nextProcessor = row; 
			}
			for (int c = 0; c < columns; c++) {
				addColumn(r);
			}
			pRow = row;
		}
		if (pRow!=null) {
			pRow.nextProcessor = this; 
		}
		results = new ZGridResults(msgr);
	}
}
