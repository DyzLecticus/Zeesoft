package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderDateTime;
import nl.zeesoft.zdk.htm.proc.Classifier;
import nl.zeesoft.zdk.htm.proc.ClassifierConfig;
import nl.zeesoft.zdk.htm.proc.Detector;
import nl.zeesoft.zdk.htm.proc.DetectorConfig;
import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.Merger;
import nl.zeesoft.zdk.htm.proc.MergerConfig;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.proc.ProcessorConfigObject;
import nl.zeesoft.zdk.htm.proc.ProcessorObject;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.StateWorker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * A ZGrid consists of several rows and columns where each column can process a certain input value.
 * It uses multithreading to maximize the throughput of grid requests.
 * The first row of a ZGrid is reserved for ZGridColumnEncoder objects that translate request input values into SDRs.
 * The remaining rows can be used for Pooler, Memory, Classifier, Merger and custom processors.
 * Context routing can be used to route the output of a column to the context of another column.
 */
public class ZGrid extends StateWorker implements ZGridRequestNext, JsAble {
	public static final String		SELF_KEY			= "SELF";
	
	protected static final int		SLEEP_NS			= 10000;
	
	private int						numRows				= 1;
	private int						numColumns			= 1;
	
	private List<ZGridRow>			rows				= new ArrayList<ZGridRow>();
	private ZGridResults			results				= null;
	
	private boolean					learn				= true;
	private int						classifyMaxSteps	= Integer.MAX_VALUE;
	private boolean					detectAnomalies		= true;

	public ZGrid(int rows, int columns) {
		super(null,null);
		initialize(rows,columns);
	}
	
	public ZGrid(Messenger msgr, WorkerUnion union,int rows, int columns) {
		super(msgr, union);
		initialize(rows,columns);
	}
	
	/**
	 * Adds a result listener to the grid.
	 * 
	 * @param listener The result listener to add
	 */
	public void addListener(ZGridResultsListener listener) {
		results.addListener(listener);
	}

	/**
	 * Indicates processors should learn from requests (default = true).
	 * 
	 * @param learn Indicates processors should learn from requests
	 */
	public void setLearn(boolean learn) {
		lockMe(this);
		this.learn = learn;
		unlockMe(this);
	}

	/**
	 * Sets the maximum number of steps for which classifications will be produced.
	 * Use -1 to turn off all classifications.
	 * Use 0 to turn off all predictions.
	 * 
	 * @param classifyMaxSteps The maximum number of classification steps
	 */
	public void setClassifyMaxSteps(int classifyMaxSteps) {
		lockMe(this);
		this.classifyMaxSteps = classifyMaxSteps;
		unlockMe(this);
	}
	
	/**
	 * Indicates anomalies should be detected.
	 * 
	 * @param detectAnomalies Indicates anomalies should be detected
	 */
	public void setDetectAnomalies(boolean detectAnomalies) {
		lockMe(this);
		this.detectAnomalies = detectAnomalies;
		unlockMe(this);
	}

	/**
	 * Sets the encoder for the specified column.
	 * Encoders are always placed in the first row of the grid.
	 * 
	 * @param columnIndex The column index
	 * @param encoder The encoder to be placed at the specified column index
	 */
	public void setEncoder(int columnIndex,ZGridColumnEncoder encoder) {
		boolean r = false;
		lockMe(this);
		if (getStateNoLock().equals(STATE_STOPPED)) {
			if (numRows>0 && numColumns>columnIndex) {
				rows.get(0).columns.get(columnIndex).encoder = encoder;
				r = true;
			}
		}
		unlockMe(this);
		if (!r && getMessenger()!=null) {
			getMessenger().error(this,"Failed to set " + encoder.getClass().getSimpleName() + " at column: " + columnIndex);
		}
	}

	/**
	 * Sets a processor at the specified position in the grid.
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 * @param processor The processor
	 */
	public void setProcessor(int rowIndex,int columnIndex,ProcessorObject processor) {
		boolean r = false;
		if (rowIndex>0) {
			lockMe(this);
			if (getStateNoLock().equals(STATE_STOPPED)) {
				if (rowIndex>0 && numRows>rowIndex && numColumns>columnIndex) {
					rows.get(rowIndex).columns.get(columnIndex).processor = processor;
					r = true;
				}
			}
			unlockMe(this);
		}
		if (!r && getMessenger()!=null) {
			getMessenger().error(this,"Failed to set " + processor.getClass().getSimpleName() + " at row/column: " + rowIndex + "/" + columnIndex);
		}
	}

	/**
	 * Creates a processor at the specified position in the grid using the specified configuration.
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 * @param config The processor configuration
	 * @return The processor
	 */
	public ProcessorObject setProcessor(int rowIndex,int columnIndex,ProcessorConfigObject config) {
		ProcessorObject r = null;
		r = getNewProcessor(config);
		if (r!=null) {
			setProcessor(rowIndex,columnIndex,r);
		} else if (getMessenger()!=null) {
			getMessenger().error(this,"Failed to create processor for configuration " + config.getClass().getSimpleName());
		}
		return r;
	}

	/**
	 * Adds column context to the specified columns of a row using a single source (1 - N).
	 * 
	 * @param rowIndex The row index
	 * @param columnIndexes The array of column indexes
	 * @param sourceRow The source row index
	 * @param sourceColumn The source column index
	 */
	public void addColumnContexts(int rowIndex,int[] columnIndexes,int sourceRow,int sourceColumn) {
		addColumnContexts(rowIndex,columnIndexes,sourceRow,sourceColumn,0);
	}

	/**
	 * Adds column context to the specified columns of a row using a single source (1 - N).
	 * 
	 * @param rowIndex The row index
	 * @param columnIndexes The array of column indexes
	 * @param sourceRow The source row index
	 * @param sourceColumn The source column index
	 * @param sourceIndex The source column result index
	 */
	public void addColumnContexts(int rowIndex,int[] columnIndexes,int sourceRow,int sourceColumn,int sourceIndex) {
		for (int i = 0; i < columnIndexes.length; i++) {
			addColumnContext(rowIndex,columnIndexes[i],sourceRow,sourceColumn,sourceIndex);
		}
	}

	/**
	 * Adds column context to the specified column using multiple sources (N - 1).
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 * @param sourceRow The source row index
	 * @param sourceColumns The array of source column indexes
	 */
	public void addColumnContexts(int rowIndex,int columnIndex,int sourceRow,int[] sourceColumns) {
		addColumnContexts(rowIndex,columnIndex,sourceRow,sourceColumns,0);
	}
	
	/**
	 * Adds column context to the specified column using multiple sources (N - 1).
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 * @param sourceRow The source row index
	 * @param sourceColumns The array of source column indexes
	 * @param sourceIndex The source column result index
	 */
	public void addColumnContexts(int rowIndex,int columnIndex,int sourceRow,int[] sourceColumns,int sourceIndex) {
		for (int i = 0; i < sourceColumns.length; i++) {
			addColumnContext(rowIndex,columnIndex,sourceRow,sourceColumns[i],sourceIndex);
		}
	}

	/**
	 * Adds column context to the specified column using a single source (1 - 1).
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 * @param sourceRow The source row index
	 * @param sourceColumn The source column index
	 */
	public void addColumnContext(int rowIndex,int columnIndex,int sourceRow,int sourceColumn) {
		addColumnContext(rowIndex,columnIndex,sourceRow,sourceColumn,0);
	}

	/**
	 * Adds column context to the specified column using a single source (1 - 1).
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 * @param sourceRow The source row index
	 * @param sourceColumn The source column index
	 * @param sourceIndex The source column result index
	 */
	public void addColumnContext(int rowIndex,int columnIndex,int sourceRow,int sourceColumn,int sourceIndex) {
		boolean r = false;
		lockMe(this);
		if (getStateNoLock().equals(STATE_STOPPED)) {
			if (numRows>rowIndex && numColumns>columnIndex && sourceRow<rowIndex && sourceColumn<numColumns) {
				ZGridColumnContext context = new ZGridColumnContext();
				context.sourceRow = sourceRow;
				context.sourceColumn = sourceColumn;
				context.sourceIndex = sourceIndex;
				rows.get(rowIndex).columns.get(columnIndex).contexts.add(context);
				r = true;
			}
		}
		unlockMe(this);
		if (!r && getMessenger()!=null) {
			getMessenger().error(this,"Failed to add column context at row/column: " + rowIndex + "/" + columnIndex);
		}
	}
	
	/**
	 * Randomizes connections for all poolers in the grid.
	 */
	public void randomizePoolerConnections() {
		lockMe(this);
		if (getStateNoLock().equals(STATE_STOPPED)) {
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

	/**
	 * Trains the date time pooler for a certain expected interval.
	 * 
	 * @param dateTimeStart The start date time in milliseconds since the epoch
	 * @param intervalMs The interval milliseconds
	 * @param increments The number of increments
	 */
	public void trainDateTimePooler(long dateTimeStart,long intervalMs,int increments) {
		if (intervalMs>0 && increments>0) {
			lockMe(this);
			if (getStateNoLock().equals(STATE_STOPPED)) {
				ZGridEncoderDateTime encoder = null;
				Pooler pooler = null;
				for (ZGridColumn col: rows.get(0).columns) {
					if (col.encoder instanceof ZGridEncoderDateTime) {
						for (int r = 1; r < numRows; r++) {
							ProcessorObject processor = rows.get(r).columns.get(col.index).processor; 
							if (processor!=null && processor instanceof Pooler) {
								encoder = (ZGridEncoderDateTime) col.encoder;
								pooler = (Pooler) processor;
								break;
							}
						}
					}
				}
				if (encoder!=null && pooler!=null) {
					long dateTime = dateTimeStart;
					for (int i = 0 ; i < increments; i++) {
						SDR sdr = encoder.getSDRForDateTime(dateTime);
						pooler.getSDRForInput(sdr,true);
						dateTime += intervalMs;
					}
					
				}
			}
			unlockMe(this);
		}
	}
	
	/**
	 * Returns a description of this grid including its encoders and processors.
	 * 
	 * @return The description
	 */
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Grid dimensions: ");
		r.append("" + numRows);
		r.append("*");
		r.append("" + numColumns);
		lockMe(this);
		if (getStateNoLock().equals(STATE_STOPPED)) {
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

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("rows","" + numRows));
		json.rootElement.children.add(new JsElem("columns","" + numColumns));
		lockMe(this);
		if (getStateNoLock().equals(STATE_STOPPED)) {
			JsElem cfgsElem = new JsElem("configurations",true);
			json.rootElement.children.add(cfgsElem);
			for (ZGridRow row: rows) {
				for (ZGridColumn col: row.columns) {
					if (col.encoder!=null) {
						JsFile cfgJs = col.encoder.toJson();
						cfgJs.rootElement.children.add(0,new JsElem("className",col.encoder.getClass().getName(),true));
						cfgJs.rootElement.children.add(0,new JsElem("columnId",col.getId(),true));
						if (cfgJs.rootElement.getChildByName("valueKey")==null) {
							cfgJs.rootElement.children.add(new JsElem("valueKey",col.encoder.getValueKey(),true));
						}
						cfgsElem.children.add(cfgJs.rootElement);
					} else if (col.processor!=null) {
						JsFile cfgJs = col.processor.getConfig().toJson();
						cfgJs.rootElement.children.add(0,new JsElem("className",col.processor.getConfig().getClass().getName(),true));
						cfgJs.rootElement.children.add(0,new JsElem("columnId",col.getId(),true));
						if (col.contexts.size()>0) {
							JsElem ctxsElem = new JsElem("contexts",true);
							cfgJs.rootElement.children.add(ctxsElem);
							for (ZGridColumnContext ctx: col.contexts) {
								ctxsElem.children.add(ctx.toJson().rootElement);
							}
						}
						cfgsElem.children.add(cfgJs.rootElement);
					}
				}
			}
		}
		unlockMe(this);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			int rs = json.rootElement.getChildInt("rows");
			int cs = json.rootElement.getChildInt("columns");
			JsElem cfgsElem = json.rootElement.getChildByName("configurations");
			if (rs>0 && cs>0 && numRows==rs && numColumns==cs && cfgsElem!=null) {
				lockMe(this);
				if (getStateNoLock().equals(STATE_STOPPED)) {
					for (JsElem cfgElem: cfgsElem.children) {
						String id = cfgElem.getChildString("columnId");
						String className = cfgElem.getChildString("className");
						ZGridColumn col = getColumnById(id);
						if (col!=null) {
							Object obj = ZDKFactory.getNewClassInstanceForName(className);
							if (obj!=null && obj instanceof JsAble) {
								JsFile cfgJs = new JsFile();
								cfgJs.rootElement = cfgElem;
								if (obj instanceof ZGridColumnEncoder) {
									col.encoder = (ZGridColumnEncoder) obj;
									col.encoder.fromJson(cfgJs);
								} else if (obj instanceof ProcessorConfigObject) {
									ProcessorConfigObject config = (ProcessorConfigObject) obj;
									config.fromJson(cfgJs);
									col.processor = getNewProcessor(config);
									JsElem ctxsElem = cfgElem.getChildByName("contexts");
									if (ctxsElem!=null) {
										for (JsElem ctxElem: ctxsElem.children) {
											JsFile ctxJs = new JsFile();
											ctxJs.rootElement = ctxElem;
											ZGridColumnContext ctx = new ZGridColumnContext();
											ctx.fromJson(ctxJs);
											col.contexts.add(ctx);
										}
									}
								}
							}
						}
					}
				}
				unlockMe(this);
			}
		}
	}
	
	/**
	 * Returns a map of the state data.
	 * 
	 * @return A map of the state data
	 */
	public SortedMap<String,ZStringBuilder> getStateData() {
		SortedMap<String,ZStringBuilder> r = new TreeMap<String,ZStringBuilder>();
		lockMe(this);
		if (getStateNoLock().equals(STATE_STOPPED)) {
			ZStringBuilder self = new ZStringBuilder();
			self.append("" + results.getUid());
			self.append(",");
			self.append("" + learn);
			self.append(",");
			self.append("" + classifyMaxSteps);
			self.append(",");
			self.append("" + detectAnomalies);
			r.put(SELF_KEY,self);
			
			for (ZGridRow row: rows) {
				for (ZGridColumn col: row.columns) {
					ZStringBuilder state = null;
					if (col.encoder!=null) {
						state = col.encoder.toStringBuilder();
					} else if (col.processor!=null) {
						state = col.processor.toStringBuilder();
					}
					if (state!=null && state.length()>0) {
						r.put(col.getId(),state);
					}
				}
			}
		}
		unlockMe(this);
		return r;
	}
	
	/**
	 * Sets the state data.
	 * 
	 * @param stateDataMap A tree map of key strings and state data string builders 
	 */
	public void setStateData(SortedMap<String,ZStringBuilder> stateDataMap) {
		for (Entry<String,ZStringBuilder> entry: stateDataMap.entrySet()) {
			setStateData(entry.getKey(),entry.getValue());
		}
	}

	/**
	 * Sets the state data of a specific element.
	 * 
	 * @param key The key of the element
	 * @param stateData The state data
	 */
	public void setStateData(String key, ZStringBuilder stateData) {
		boolean r = false;
		lockMe(this);
		if (getStateNoLock().equals(STATE_STOPPED)) {
			ZGridColumn col = getColumnById(key);
			if (col!=null) {
				if (col.encoder!=null) {
					col.encoder.fromStringBuilder(stateData);
					r = true;
				} else if (col.processor!=null) {
					col.processor.fromStringBuilder(stateData);
					r = true;
				}
			} else if (key.equals(SELF_KEY)) {
				List<ZStringBuilder> elems = stateData.split(",");
				if (elems.size()==4) {
					results.setUid(Long.parseLong("" + elems.get(0)));
					learn = Boolean.parseBoolean("" + elems.get(1));
					classifyMaxSteps = Integer.parseInt("" + elems.get(2));
					detectAnomalies = Boolean.parseBoolean("" + elems.get(3));
					r = true;
				}
			}
		}
		unlockMe(this);
		if (!r && getMessenger()!=null) {
			getMessenger().error(this,"Failed to set state data for key: " + key);
		}
	}

	/**
	 * Returns a new grid request.
	 * 
	 * @return A new grid request
	 */
	public ZGridRequest getNewRequest() {
		return new ZGridRequest(numColumns);
	}
	
	/**
	 * Adds a request to the grid for processing.
	 * 
	 * @param request The request to add
	 * @return The id assigned to the request
	 */
	public long addRequest(ZGridRequest request) {
		lockMe(this);
		request.learn = learn;
		request.classifyMaxSteps = classifyMaxSteps;
		request.detectAnomalies = detectAnomalies;
		long r = results.assignRequestId(request);
		ZGridResult result = new ZGridResult(getMessenger(),request.copy()); 
		rows.get(0).addResult(result);
		unlockMe(this);
		return r;
	}

	@Override
	public void processedRequest(ZGridResult result) {
		results.addResult(result);
	}
	
	/**
	 * Destroys the grid and its processors.
	 */
	public void destroy() {
		lockMe(this);
		if (getStateNoLock().equals(STATE_STOPPED)) {
			for (ZGridRow row: rows) {
				row.destroy();
			}
			rows.clear();
			results.destroy();
		}
		unlockMe(this);
	}
	
	@Override
	protected void onStart() {
		if (getMessenger()!=null) {
			getMessenger().debug(this,"Starting grid ...");
		}
		for (ZGridRow row: rows) {
			row.start();
		}
	}
	
	@Override
	protected void onStop() {
		if (getMessenger()!=null) {
			getMessenger().debug(this,"Stopping grid ...");
		}
		for (ZGridRow row: rows) {
			row.stop();
		}
	}
	
	@Override
	protected void whileWorking() {
		results.flush();
	}
	
	@Override
	protected void startedWorking() {
		super.startedWorking();
		if (getMessenger()!=null) {
			getMessenger().debug(this,"Started grid");
		}
	}
	
	@Override
	protected void stoppedWorking() {
		super.stoppedWorking();
		results.flush();
		if (getMessenger()!=null) {
			getMessenger().debug(this,"Stopped grid");
		}
	}
	
	protected ZGridRow addRow() {
		ZGridRow r = new ZGridRow(getMessenger(),getUnion());
		r.index = rows.size();
		rows.add(r);
		return r;
	}

	protected void addColumn(int rowIndex) {
		if (numRows>rowIndex) {
			ZGridRow row = rows.get(rowIndex);
			row.addColumn();
		}
	}

	protected void initialize(int rows, int columns) {
		if (rows < 1) {
			rows = 1;
		}
		if (columns < 1) {
			columns = 1;
		}
		numRows = rows;
		numColumns = columns;
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
		results = new ZGridResults(getMessenger(),this);
	}
	
	protected ZGridColumn getColumnById(String id) {
		ZGridColumn r = null;
		for (ZGridRow row: rows) {
			for (ZGridColumn col: row.columns) {
				if (col.getId().equals(id)) {
					r = col;
					break;
				}
			}
			if (r!=null) {
				break;
			}
		}
		return r;
	}
	
	protected ProcessorObject getNewProcessor(ProcessorConfigObject config) {
		ProcessorObject r = null;
		if (config instanceof PoolerConfig) {
			r = getNewPooler((PoolerConfig)config);
		} else if (config instanceof MemoryConfig) {
			r = getNewMemory((MemoryConfig)config);
		} else if (config instanceof ClassifierConfig) {
			r = getNewClassifier((ClassifierConfig)config);
		} else if (config instanceof DetectorConfig) {
			r = getNewDetector((DetectorConfig)config);
		} else if (config instanceof MergerConfig) {
			r = getNewMerger((MergerConfig)config);
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
	
	protected Detector getNewDetector(DetectorConfig config) {
		return new Detector(config);
	}
	
	protected Merger getNewMerger(MergerConfig config) {
		return new Merger(config);
	}
}
