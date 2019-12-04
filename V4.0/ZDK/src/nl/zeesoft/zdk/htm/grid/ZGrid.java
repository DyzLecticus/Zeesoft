package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.Classifier;
import nl.zeesoft.zdk.htm.proc.ClassifierConfig;
import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.proc.ProcessorConfigObject;
import nl.zeesoft.zdk.htm.proc.ProcessorObject;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ZGrid extends Worker implements ZGridRequestNext {
	public static final String		STATE_STOPPED	= "STOPPED";
	public static final String		STATE_STARTING	= "STARTING";
	public static final String		STATE_STARTED	= "STARTED";
	public static final String		STATE_STOPPING	= "STOPPING";
	
	private String					state			= STATE_STOPPED;
	
	private List<ZGridRow>			rows			= new ArrayList<ZGridRow>();
	private ZGridResults			results			= null;
	
	private boolean					learn			= true;

	public ZGrid(int rows, int columns) {
		super(null,null);
		initialize(null,rows,columns);
	}
	
	public ZGrid(Messenger msgr, WorkerUnion union,int rows, int columns) {
		super(msgr, union);
		initialize(msgr,rows,columns);
	}
	
	public String getState() {
		String r = "";
		lockMe(this);
		r = state;
		unlockMe(this);
		return r;
	}
	
	public boolean isActive() {
		boolean r = false;
		lockMe(this);
		r = !state.equals(STATE_STOPPED);
		unlockMe(this);
		return r;
	}

	public void addListener(ZGridResultsListener listener) {
		results.addListener(listener);
	}

	public void setLearn(boolean learn) {
		lockMe(this);
		this.learn = learn;
		unlockMe(this);
	}

	public void setEncoder(int columnIndex,ZGridColumnEncoder encoder) {
		lockMe(this);
		if (state.equals(STATE_STOPPED)) {
			if (rows.size()>0 && rows.get(0).columns.size()>columnIndex) {
				rows.get(0).columns.get(columnIndex).encoder = encoder;
			}
		}
		unlockMe(this);
	}

	public void setProcessor(int rowIndex,int columnIndex,ProcessorObject processor) {
		lockMe(this);
		if (state.equals(STATE_STOPPED)) {
			if (rowIndex>0 && rows.size()>rowIndex && rows.get(rowIndex).columns.size()>columnIndex) {
				rows.get(rowIndex).columns.get(columnIndex).processor = processor;
			}
		}
		unlockMe(this);
	}

	public ProcessorObject setProcessor(int rowIndex,int columnIndex,ProcessorConfigObject config) {
		ProcessorObject r = null;
		r = getNewProcessor(config);
		if (r!=null) {
			setProcessor(rowIndex,columnIndex,r);
		}
		return r;
	}

	public void addColumnContext(int rowIndex,int columnIndex,int sourceRow,int sourceColumn) {
		addColumnContext(rowIndex,columnIndex,sourceRow,sourceColumn,0);
	}

	public void addColumnContext(int rowIndex,int columnIndex,int sourceRow,int sourceColumn,int sourceIndex) {
		lockMe(this);
		if (state.equals(STATE_STOPPED)) {
			if (rows.size()>rowIndex && rows.get(rowIndex).columns.size()>columnIndex && sourceRow<rowIndex) {
				ZGridColumnContext context = new ZGridColumnContext();
				context.sourceRow = sourceRow;
				context.sourceColumn = sourceColumn;
				context.sourceIndex = sourceIndex;
				rows.get(rowIndex).columns.get(columnIndex).contexts.add(context);
			}
		}
		unlockMe(this);
	}
	
	public void randomizePoolerConnections() {
		lockMe(this);
		if (state.equals(STATE_STOPPED)) {
			for (ZGridRow row: rows) {
				for (ZGridColumn col: row.columns) {
					if (col.processor!=null && col.processor instanceof Pooler) {
						((Pooler) col.processor).randomizeConnections();
					}
				}
			}
		}
		unlockMe(this);
	}

	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		lockMe(this);
		if (state.equals(STATE_STOPPED)) {
			for (ZGridRow row: rows) {
				for (ZGridColumn col: row.columns) {
					ZStringBuilder desc = null;
					if (col.encoder!=null) {
						desc = col.encoder.getDescription();
					} else if (col.processor!=null) {
						desc = col.processor.getDescription();
					}
					if (desc!=null) {
						desc.replace("\n","\n  ");
						if (r.length()>0) {
							r.append("\n");
						}
						r.append("- Column ");
						r.append(col.getId());
						r.append(" = ");
						r.append(desc);
					}
				}
			}
		}
		unlockMe(this);
		return r;
	}
	
	public ZGridRequest getNewRequest() {
		return new ZGridRequest(rows.get(0).columns.size());
	}
	
	public long addRequest(ZGridRequest request) {
		lockMe(this);
		request.learn = learn;
		long r = results.assignRequestId(request);
		ZGridResult result = new ZGridResult(getMessenger(),request.copy()); 
		rows.get(0).addResult(result);
		unlockMe(this);
		return r;
	}
	
	@Override
	public void start() {
		boolean r = false;
		lockMe(this);
		r = state.equals(STATE_STOPPED);
		if (r) {
			state = STATE_STARTING;
		}
		unlockMe(this);
		if (r) {
			for (ZGridRow row: rows) {
				row.start();
			}
			super.start();
		}
	}
	
	@Override
	public void stop() {
		boolean r = false;
		lockMe(this);
		r = state.equals(STATE_STARTED);
		if (r) {
			state = STATE_STOPPING;
		}
		unlockMe(this);
		if (r) {
			for (ZGridRow row: rows) {
				row.stop();
			}
			super.stop();
		}
	}

	public void whileInactive() {
		whileActive(false);
	}
	
	public void whileActive() {
		whileActive(true);
	}
	
	public void destroy() {
		lockMe(this);
		if (state.equals(STATE_STOPPED)) {
			for (ZGridRow row: rows) {
				row.destroy();
			}
			rows.clear();
			results.destroy();
		}
		unlockMe(this);
	}

	@Override
	public void processedRequest(ZGridResult result) {
		results.addResult(result);
	}
	
	@Override
	protected void whileWorking() {
		results.flush();
	}
	
	@Override
	protected void startedWorking() {
		lockMe(this);
		state = STATE_STARTED;
		unlockMe(this);
	}
	
	@Override
	protected void stoppedWorking() {
		lockMe(this);
		state = STATE_STOPPED;
		unlockMe(this);
		results.flush();
	}

	protected void whileActive(boolean active) {
		while(isActive()==active) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				if (getMessenger()!=null) {
					getMessenger().error(this,"Waiting for grid activity to stop was interrupted",e);
				} else {
					e.printStackTrace();
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
		if (rows < 1) {
			rows = 1;
		}
		if (columns < 1) {
			columns = 1;
		}
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
	
	protected ProcessorObject getNewProcessor(ProcessorConfigObject config) {
		ProcessorObject r = null;
		if (config instanceof PoolerConfig) {
			r = getNewPooler((PoolerConfig)config);
		} else if (config instanceof MemoryConfig) {
			r = getNewMemory((MemoryConfig)config);
		} else if (config instanceof ClassifierConfig) {
			r = getNewClassifier((ClassifierConfig)config);
		}
		return r;
	}
	
	protected Pooler getNewPooler(PoolerConfig config) {
		return new Pooler(config);
	}
	
	protected Memory getNewMemory(MemoryConfig config) {
		return new Memory(config);
	}
	
	protected Classifier getNewClassifier(ClassifierConfig config) {
		return new Classifier(config);
	}
}
