package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderDateTime;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderValue;
import nl.zeesoft.zdk.htm.proc.ClassifierConfig;
import nl.zeesoft.zdk.htm.proc.DetectorConfig;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.MergerConfig;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.proc.ProcessorConfigObject;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * A ZGridFactory can be used to create and modify ZGrid configurations.
 * The configuration can then be used to instantiate an actual ZGrid. 
 */
public class ZGridFactory extends Locker implements JsAble {
	private WorkerUnion								union			= null;
	
	private int										numRows			= 4;
	private int										numColumns		= 2;
	
	private SortedMap<String,ZGridFactoryColumn>	columns			= new TreeMap<String,ZGridFactoryColumn>();
	
	public ZGridFactory() {
		super(null);
	}
	
	public ZGridFactory(Messenger msgr, WorkerUnion uni) {
		super(msgr);
		this.union = uni;
	}
	
	/**
	 * Changes the dimensions of the grid to match the specified number of rows and columns.
	 * 
	 * @param numRows The number of rows
	 * @param numColumns The number of columns
	 */
	public void setDimensions(int numRows, int numColumns) {
		if (numRows>0 && numColumns>0) {
			lockMe(this);
			this.numRows = numRows;
			this.numColumns = numColumns;
			updatedDimensionsNoLock();
			unlockMe(this);
		}
	}
	
	/**
	 * Returns the number of rows.
	 * 
	 * @return The number of rows
	 */
	public int getNumRows() {
		int r = 0;
		lockMe(this);
		r = numRows;
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns the number of columns.
	 * 
	 * @return The number of columns
	 */
	public int getNumColumns() {
		int r = 0;
		lockMe(this);
		r = numColumns;
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns a list of column ids of all positions in the grid.
	 * 
	 * @return A list of column ids 
	 */
	public List<String> getColumnIds() {
		List<String> r = null;
		lockMe(this);
		r = getColumnIdsNoLock();
		unlockMe(this);
		return r;
	}
	
	/**
	 * Removes all configurations from all grid positions.
	 */
	public void clear() {
		lockMe(this);
		clearNoLock();
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
		lockMe(this);
		if (columnIndex<numColumns) {
			String id = ZGridColumn.getColumnId(0,columnIndex);
			ZGridFactoryColumn col = new ZGridFactoryColumn();
			col.columnId = id;
			col.rowIndex = 0;
			col.columnIndex = columnIndex;
			col.encoder = encoder;
			columns.put(id,col);
		}
		unlockMe(this);
	}
	
	/**
	 * Returns a copy of the encoder from the specified position in the grid.
	 * 
	 * @param columnIndex The column index
	 * @return A copy of the encoder or null
	 */
	public ZGridColumnEncoder getEncoder(int columnIndex) {
		ZGridColumnEncoder r = null;
		lockMe(this);
		ZGridFactoryColumn col = getColumnNoLock(0,columnIndex);
		if (col!=null && col.encoder!=null) {
			r = col.encoder.copy();
		}
		unlockMe(this);
		return r;
	}

	/**
	 * Returns the encoder value key for the specified column.
	 * 
	 * @param columnIndex The column index
	 * @return The encoder value key or an empty string
	 */
	public String getValueKey(int columnIndex) {
		String r = "";
		lockMe(this);
		ZGridFactoryColumn col = getColumnNoLock(0,columnIndex);
		if (col!=null && col.encoder!=null) {
			r = col.encoder.getValueKey();
		}
		unlockMe(this);
		return r;
	}

	/**
	 * Removes the encoder from the specified position in the grid.
	 * 
	 * @param columnIndex The column index
	 */
	public void removeEncoder(int columnIndex) {
		lockMe(this);
		removeColumnNoLock(0,columnIndex);
		unlockMe(this);
	}
	
	/**
	 * Sets a processor configuration at the specified position in the grid.
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 * @param config The processor configuration
	 */
	public void setProcessor(int rowIndex,int columnIndex,ProcessorConfigObject config) {
		lockMe(this);
		if (rowIndex>0 && rowIndex<numRows && columnIndex<numColumns) {
			String id = ZGridColumn.getColumnId(rowIndex,columnIndex);
			ZGridFactoryColumn col = new ZGridFactoryColumn();
			col.columnId = id;
			col.rowIndex = rowIndex;
			col.columnIndex = columnIndex;
			col.processorConfig = config;
			columns.put(id,col);
		}
		unlockMe(this);
	}
	
	/**
	 * Returns a copy of the processor configuration from the specified position in the grid.
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 * @return A copy of the processor configuration or null
	 */
	public ProcessorConfigObject getProcessor(int rowIndex,int columnIndex) {
		ProcessorConfigObject r = null;
		lockMe(this);
		ZGridFactoryColumn col = getColumnNoLock(rowIndex,columnIndex);
		if (col!=null && col.processorConfig!=null) {
			r = col.processorConfig.copy();
		}
		unlockMe(this);
		return r;
	}
	
	/**
	 * Removes the processor configuration from the specified position in the grid.
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 */
	public void removeProcessor(int rowIndex,int columnIndex) {
		if (rowIndex>0) {
			lockMe(this);
			removeColumnNoLock(rowIndex,columnIndex);
			unlockMe(this);
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
		lockMe(this);
		if (numRows>rowIndex && numColumns>columnIndex && sourceRow<rowIndex && sourceColumn<numColumns) {
			ZGridFactoryColumn col = getColumnNoLock(rowIndex,columnIndex);
			if (col!=null) {
				ZGridColumnContext context = new ZGridColumnContext();
				context.sourceRow = sourceRow;
				context.sourceColumn = sourceColumn;
				context.sourceIndex = sourceIndex;
				col.contexts.add(context);
			}
		}
		unlockMe(this);
	}
	
	/**
	 * Returns a copy of the contexts for a specific position in the grid.
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 * @return A copy of the contexts or an empty list
	 */
	public List<ZGridColumnContext> getColumnContexts(int rowIndex,int columnIndex) {
		List<ZGridColumnContext> r = new ArrayList<ZGridColumnContext>();
		lockMe(this);
		ZGridFactoryColumn col = getColumnNoLock(rowIndex,columnIndex);
		if (col!=null) {
			for (ZGridColumnContext context: col.contexts) {
				r.add(context.copy());
			}
		}
		unlockMe(this);
		return r;
	}
	
	/**
	 * Removes all column contexts from the specified position in the grid.
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 */
	public void clearColumnContext(int rowIndex,int columnIndex) {
		lockMe(this);
		ZGridFactoryColumn col = getColumnNoLock(rowIndex,columnIndex);
		if (col!=null) {
			col.contexts.clear();
		}
		unlockMe(this);
	}

	/**
	 * Returns a list of configuration errors or an empty string builder.
	 * This method only tests for common configuration omissions and mistakes.
	 * 
	 * @return A list of configuration errors or an empty string builder
	 */
	public ZStringBuilder testConfiguration() {
		ZStringBuilder r = new ZStringBuilder();
		lockMe(this);
		// Encoders
		for (int ci = 0; ci < numColumns; ci++) {
			ZGridFactoryColumn col = getColumnNoLock(0,ci);
			if (col!=null && col.encoder!=null) {
				ZGridColumnEncoder encoder = col.encoder;
				ZStringBuilder err = encoder.testScalarOverlap();
				if (err.length()>0) {
					appendLine(r,col.columnId + ": " + err);
				}
			}
		}
		// Pooler and memory IO
		for (ZGridFactoryColumn col: getPoolerAndMemoryColumnsNoLock()) {
			int expectedLength = 0;
			if (col.processorConfig instanceof PoolerConfig) {
				PoolerConfig poolerConfig = (PoolerConfig) col.processorConfig;
				expectedLength = poolerConfig.getInputLength();
			} else if (col.processorConfig instanceof MemoryConfig) {
				MemoryConfig memoryConfig = (MemoryConfig) col.processorConfig;
				expectedLength = memoryConfig.getLength();
			}
			int inputLength = 0;
			for (int ri = col.rowIndex - 1; ri >= 0; ri--) {
				inputLength = getColumnOutputLengthNoLock(ri,col.columnIndex);
				if (inputLength>0) {
					break;
				}
			}
			if (inputLength!=expectedLength) {
				appendLine(r,col.columnId + ": " + col.processorConfig.getClass().getSimpleName() + " input length does not match expectation: " + inputLength + " <> " + expectedLength);
			}
		}
		// Memory context dimensions
		for (ZGridFactoryColumn col: getMemoryColumnsNoLock()) {
			MemoryConfig memoryConfig = (MemoryConfig) col.processorConfig;
			int i = 0;
			for (Integer expectedLength: memoryConfig.getContextDimensions()) {
				if (i >= col.contexts.size()) {
					appendLine(r,col.columnId + ": " + col.processorConfig.getClass().getSimpleName() + " missing column context for dimension index: " + i + ", length: " + expectedLength);
				} else {
					ZGridColumnContext context = col.contexts.get(i);
					int inputLength = getColumnOutputLengthNoLock(context.sourceRow,context.sourceColumn);
					if (inputLength!=expectedLength) {
						appendLine(r,col.columnId + ": " + col.processorConfig.getClass().getSimpleName() + " column context input length does not match expectation: " + inputLength + " <> " + expectedLength + ", index: " + i);
					}
				}
				i++;
			}
		}
		// Classifier context configuration
		for (ZGridFactoryColumn col: getClassifierColumnsNoLock()) {
			if (col.contexts.size()==0) {
				appendLine(r,col.columnId + ": " + col.processorConfig.getClass().getSimpleName() + " missing column context configuration");
			} else {
				ZGridColumnContext context = col.contexts.get(0);
				ZGridFactoryColumn sCol = getColumnNoLock(context.sourceRow,context.sourceColumn);
				if (sCol!=null) {
				    if (sCol.encoder!=null) {
						ClassifierConfig classifierConfig = (ClassifierConfig) col.processorConfig;
						if (!sCol.encoder.getValueKey().equals(classifierConfig.getValueKey())) {
							appendLine(r,col.columnId + ": " + col.processorConfig.getClass().getSimpleName() + " column context encoder value key does not match expectation: " + col.encoder.getValueKey() + " <> " + classifierConfig.getValueKey());
						}
				    }
				} else {
					appendLine(r,col.columnId + ": " + col.processorConfig.getClass().getSimpleName() + " missing column context input");
				}
			}
		}
		// Detector context configuration
		for (ZGridFactoryColumn col: getDetectorColumnsNoLock()) {
			if (col.contexts.size()<=1) {
				appendLine(r,col.columnId + ": " + col.processorConfig.getClass().getSimpleName() + " missing column context configuration(s)");
			} else {
				ZGridColumnContext context = col.contexts.get(0);
				ZGridFactoryColumn sCol = getColumnNoLock(context.sourceRow,context.sourceColumn);
				if (sCol==null || sCol.processorConfig==null) {
					appendLine(r,col.columnId + ": " + col.processorConfig.getClass().getSimpleName() + " missing column context activation input");
				}
				context = col.contexts.get(1);
				sCol = getColumnNoLock(context.sourceRow,context.sourceColumn);
				if (sCol==null || sCol.processorConfig==null) {
					appendLine(r,col.columnId + ": " + col.processorConfig.getClass().getSimpleName() + " missing column context burst input");
				} else if (context.sourceIndex>0 && !(sCol.processorConfig instanceof MemoryConfig)) {
					appendLine(r,col.columnId + ": " + col.processorConfig.getClass().getSimpleName() + " missing column context burst input");
				}
			}
		}
		unlockMe(this);
		return r;
	}
	
	/**
	 * Builds a new grid.
	 * 
	 * @return A new grid
	 */
	public ZGrid buildNewGrid() {
		ZGrid r = null;
		lockMe(this);
		r = getNewGrid(getMessenger(),union,numRows,numColumns);
		for (ZGridFactoryColumn col: columns.values()) {
			if (col.encoder!=null) {
				r.setEncoder(col.columnIndex,col.encoder);
			} else if (col.rowIndex>0) {
				r.setProcessor(col.rowIndex,col.columnIndex,col.processorConfig.copy());
			}
			for (ZGridColumnContext context: col.contexts) {
				r.addColumnContext(col.rowIndex,col.columnIndex,context.sourceRow,context.sourceColumn,context.sourceIndex);
			}
		}
		unlockMe(this);
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		lockMe(this);
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("rows","" + numRows));
		json.rootElement.children.add(new JsElem("columns","" + numColumns));
		JsElem cfgsElem = new JsElem("configurations",true);
		json.rootElement.children.add(cfgsElem);
		for (ZGridFactoryColumn col: columns.values()) {
			if (col.encoder!=null) {
				JsFile cfgJs = col.encoder.toJson();
				cfgJs.rootElement.children.add(0,new JsElem("className",col.encoder.getClass().getName(),true));
				cfgJs.rootElement.children.add(0,new JsElem("columnId",col.columnId,true));
				if (cfgJs.rootElement.getChildByName("valueKey")==null) {
					cfgJs.rootElement.children.add(new JsElem("valueKey",col.encoder.getValueKey(),true));
				}
				cfgsElem.children.add(cfgJs.rootElement);
			} else if (col.processorConfig!=null) {
				JsFile cfgJs = col.processorConfig.toJson();
				cfgJs.rootElement.children.add(0,new JsElem("className",col.processorConfig.getClass().getName(),true));
				cfgJs.rootElement.children.add(0,new JsElem("columnId",col.columnId,true));
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
		unlockMe(this);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			int rs = json.rootElement.getChildInt("rows");
			int cs = json.rootElement.getChildInt("columns");
			JsElem cfgsElem = json.rootElement.getChildByName("configurations");
			if (rs>0 && cs>0 && cfgsElem!=null) {
				lockMe(this);
				clearNoLock();
				numRows = rs;
				numColumns = cs;
				for (JsElem cfgElem: cfgsElem.children) {
					String id = cfgElem.getChildString("columnId");
					String className = cfgElem.getChildString("className");
					Object obj = ZDKFactory.getNewClassInstanceForName(className);

					boolean found = false;
					ZGridFactoryColumn col = new ZGridFactoryColumn();
					col.columnId = id;
					for (int r = 0; r < numRows; r++) {
						for (int c = 0; c < numColumns; c++) {
							String test = ZGridColumn.getColumnId(r,c);
							if (test.equals(id)) {
								col.rowIndex = r;
								col.columnIndex = c;
								found = true;
								break;
							}
						}
						if (found) {
							break;
						}
					}
					if (found && obj!=null && obj instanceof JsAble) {
						columns.put(id,col);
						JsFile cfgJs = new JsFile();
						cfgJs.rootElement = cfgElem;
						if (obj instanceof ZGridColumnEncoder) {
							col.encoder = (ZGridColumnEncoder) obj;
							col.encoder.fromJson(cfgJs);
						} else if (obj instanceof ProcessorConfigObject) {
							ProcessorConfigObject config = (ProcessorConfigObject) obj;
							config.fromJson(cfgJs);
							col.processorConfig = config;
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
				unlockMe(this);
			}
		}
	}
	
	public void initializeDefaultGrid() {
		clear();
		
		setDimensions(4,2);
		
		// Create encoders
		ZGridEncoderDateTime dateTimeEncoder = new ZGridEncoderDateTime();
		dateTimeEncoder.setIncludeMonth(false);
		dateTimeEncoder.setIncludeDayOfWeek(false);
		
		ZGridEncoderValue valueEncoder = new ZGridEncoderValue();
		valueEncoder.setMaxValue(50);
		
		// Add encoders
		setEncoder(0,dateTimeEncoder);
		setEncoder(1,valueEncoder);

		// Add processors
		setProcessor(1,0,new PoolerConfig(dateTimeEncoder.length(),1024,21));
		
		PoolerConfig poolerConfig = new PoolerConfig(valueEncoder.length(),1024,21);
		setProcessor(1,1,poolerConfig);

		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		memoryConfig.addContextDimension(1024);
		setProcessor(2,1,memoryConfig);

		setProcessor(3,0,new DetectorConfig());

		setProcessor(3,1,new ClassifierConfig(1));

		// Route output from dateTime pooler to memory context
		addColumnContext(2,1,1,0);
		
		// Route output from value pooler and memory burst to detector context
		addColumnContext(3,0,1,1);
		addColumnContext(3,0,2,1,1);
		
		// Route value DateTimeSDR from encoder to classifier
		addColumnContext(3,1,0,1);
	}
	
	public void initializeTestGrid() {
		clear();
		
		setDimensions(5,5);
		
		// Create encoders
		ZGridEncoderDateTime dateTimeEncoder = new ZGridEncoderDateTime();
		dateTimeEncoder.setIncludeMonth(false);
		dateTimeEncoder.setIncludeDayOfWeek(false);
		dateTimeEncoder.setIncludeHourOfDay(false);
		dateTimeEncoder.setIncludeMinute(false);
		dateTimeEncoder.setScale(2);
		
		ZGridEncoderValue valueEncoder = new ZGridEncoderValue( );
		valueEncoder.setBits(16);
		valueEncoder.setMaxValue(30);
		
		ZGridEncoderValue posXEncoder = new ZGridEncoderValue(64,"POSX");
		posXEncoder.setMaxValue(20);

		ZGridEncoderValue posYEncoder = new ZGridEncoderValue(64,"POSY");
		posYEncoder.setMaxValue(20);

		ZGridEncoderValue posZEncoder = new ZGridEncoderValue(64,"POSZ");
		posZEncoder.setMaxValue(20);
		
		// Add encoders
		setEncoder(0,dateTimeEncoder);
		setEncoder(1,valueEncoder);
		setEncoder(2,posXEncoder);
		setEncoder(3,posYEncoder);
		setEncoder(4,posZEncoder);
		
		// Add processors
		setProcessor(2,0,new PoolerConfig(dateTimeEncoder.length(),1024,21));
		
		PoolerConfig poolerConfig = new PoolerConfig(valueEncoder.length(),1024,21);
		setProcessor(2,1,poolerConfig);

		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		memoryConfig.addContextDimension(1024);
		memoryConfig.addContextDimension(1024);
		setProcessor(3,1,memoryConfig);

		setProcessor(4,0,new DetectorConfig());

		setProcessor(4,1,new ClassifierConfig(1));

		setProcessor(1,3,new MergerConfig());

		poolerConfig = new PoolerConfig(posXEncoder.length() * 3,1024,21);
		setProcessor(2,3,poolerConfig);

		// Route output from position encoders to position merger context
		addColumnContext(1,3,0,2);
		addColumnContext(1,3,0,4);
		
		// Route output from dateTime and position poolers to memory context
		addColumnContext(3,1,2,0);
		addColumnContext(3,1,2,3);

		// Route output from value pooler and memory burst to detector context
		addColumnContext(4,0,2,1);
		addColumnContext(4,0,3,1,1);

		// Route value DateTimeSDR from encoder to classifier
		addColumnContext(4,1,0,1);
	}

	protected void removeColumnNoLock(int rowIndex, int columnIndex) {
		String id = ZGridColumn.getColumnId(rowIndex,columnIndex);
		columns.remove(id);
	}
	
	protected void updatedDimensionsNoLock() {
		List<String> columnIds = getColumnIdsNoLock();
		List<String> colIds = new ArrayList<String>(columns.keySet());
		for (String colId: colIds) {
			if (!columnIds.contains(colId)) {
				columns.remove(colId);
			}
		}
	}
	
	protected List<String> getColumnIdsNoLock() {
		List<String> r = new ArrayList<String>(); 
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				r.add(ZGridColumn.getColumnId(row, col));
			}
		}
		return r;
	}
	
	protected ZGridFactoryColumn getColumnNoLock(int rowIndex, int columnIndex) {
		String id = ZGridColumn.getColumnId(rowIndex,columnIndex);
		return columns.get(id);
	}
	
	protected List<ZGridFactoryColumn> getColumnsNoLock(String configClassSimpleName) {
		List<ZGridFactoryColumn> r = new ArrayList<ZGridFactoryColumn>();
		for (int ri = 1; ri < numRows; ri++) {
			for (int ci = 0; ci < numColumns; ci++) {
				ZGridFactoryColumn col = getColumnNoLock(ri,ci);
				if (col!=null && col.processorConfig!=null && 
					(
						(configClassSimpleName.equals(PoolerConfig.class.getSimpleName()) && col.processorConfig instanceof PoolerConfig) ||
						(configClassSimpleName.equals(MemoryConfig.class.getSimpleName()) && col.processorConfig instanceof MemoryConfig) ||
						(configClassSimpleName.equals(MergerConfig.class.getSimpleName()) && col.processorConfig instanceof MergerConfig) ||
						(configClassSimpleName.equals(ClassifierConfig.class.getSimpleName()) && col.processorConfig instanceof ClassifierConfig) ||
						(configClassSimpleName.equals(DetectorConfig.class.getSimpleName()) && col.processorConfig instanceof DetectorConfig)
					)
					) {
					r.add(col);
				}
			}
		}
		return r;
	}
	
	protected List<ZGridFactoryColumn> getPoolerAndMemoryColumnsNoLock() {
		List<ZGridFactoryColumn> r = getColumnsNoLock(PoolerConfig.class.getSimpleName());
		for (ZGridFactoryColumn col: getColumnsNoLock(MemoryConfig.class.getSimpleName())) {
			r.add(col);
		}
		return r;
	}
	
	protected List<ZGridFactoryColumn> getMemoryColumnsNoLock() {
		return getColumnsNoLock(MemoryConfig.class.getSimpleName());
	}
	
	protected List<ZGridFactoryColumn> getClassifierColumnsNoLock() {
		return getColumnsNoLock(ClassifierConfig.class.getSimpleName());
	}
	
	protected List<ZGridFactoryColumn> getDetectorColumnsNoLock() {
		return getColumnsNoLock(DetectorConfig.class.getSimpleName());
	}
	
	protected int getColumnOutputLengthNoLock(int rowIndex, int columnIndex) {
		int r = 0;
		ZGridFactoryColumn col = getColumnNoLock(rowIndex,columnIndex);
		if (col!=null) {
			if (col.encoder!=null) {
				r = col.encoder.length();
			} else if (col.processorConfig!=null) {
				if (col.processorConfig instanceof PoolerConfig) {
					PoolerConfig cfg = (PoolerConfig) col.processorConfig;
					r = cfg.getOutputLength();
				} else if (col.processorConfig instanceof MemoryConfig) {
					MemoryConfig cfg = (MemoryConfig) col.processorConfig;
					r = cfg.getLength();
				} else if (col.processorConfig instanceof MergerConfig) {
					MergerConfig cfg = (MergerConfig) col.processorConfig;
					for (ZGridColumnContext context: col.contexts) {
						r += getColumnOutputLengthNoLock(context.sourceRow,context.sourceColumn);
						if (cfg.isUnion()) {
							break;
						}
					}
					if (!cfg.isUnion()) {
						for (int pr = rowIndex - 1; pr >= 0; pr--) {
							int add = getColumnOutputLengthNoLock(pr,columnIndex);
							if (add>0) {
								r += add;
								break;
							}
						}
					}
				}
			}
		}
		return r;
	}
	
	protected void clearNoLock() {
		for (ZGridFactoryColumn column: columns.values()) {
			column.contexts.clear();
		}
		columns.clear();
	}
	
	protected ZGrid getNewGrid(Messenger msgr, WorkerUnion uni,int numRows, int numColumns) {
		return new ZGrid(msgr,uni,numRows,numColumns);
	}
	
	private static void appendLine(ZStringBuilder str, String line) {
		if (str.length()>0) {
			str.append("\n");
		}
		str.append(line);
	}
}
