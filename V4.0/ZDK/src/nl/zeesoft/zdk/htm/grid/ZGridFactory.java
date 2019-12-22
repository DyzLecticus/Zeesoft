package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDKFactory;
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
	private WorkerUnion									union		= null;
	
	private int											numRows		= 4;
	private int											numColumns	= 2;
	
	private SortedMap<String,ZGridFactoryColumn>		columns		= new TreeMap<String,ZGridFactoryColumn>();
	
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
			updatedDimensions();
			unlockMe(this);
		}
	}
	
	/**
	 * Removes all configuration from all grid positions.
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
			ZGridFactoryColumn column = new ZGridFactoryColumn();
			column.columnId = id;
			column.rowIndex = 0;
			column.columnIndex = columnIndex;
			column.encoder = encoder;
			columns.put(id,column);
		}
		unlockMe(this);
	}
	
	/**
	 * Returns the encoder from the specified position in the grid.
	 * 
	 * @param columnIndex The column index
	 * @return The encoder or null
	 */
	public ZGridColumnEncoder getEncoder(int columnIndex) {
		ZGridColumnEncoder r = null;
		lockMe(this);
		String id = ZGridColumn.getColumnId(0,columnIndex);
		ZGridFactoryColumn col = columns.get(id);
		if (col!=null && col.encoder!=null) {
			r = col.encoder;
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
		removeColumn(0,columnIndex);
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
			ZGridFactoryColumn column = new ZGridFactoryColumn();
			column.columnId = id;
			column.rowIndex = rowIndex;
			column.columnIndex = columnIndex;
			column.processorConfig = config;
			columns.put(id,column);
		}
		unlockMe(this);
	}
	
	/**
	 * Returns the processor configuration from the specified position in the grid.
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 * @return The processor configuration or null
	 */
	public ProcessorConfigObject getProcessor(int rowIndex,int columnIndex) {
		ProcessorConfigObject r = null;
		lockMe(this);
		String id = ZGridColumn.getColumnId(0,columnIndex);
		ZGridFactoryColumn col = columns.get(id);
		if (col!=null && col.processorConfig!=null) {
			r = col.processorConfig;
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
			removeColumn(rowIndex,columnIndex);
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
			String id = ZGridColumn.getColumnId(rowIndex,columnIndex);
			ZGridFactoryColumn column = columns.get(id);
			if (column!=null) {
				ZGridColumnContext context = new ZGridColumnContext();
				context.sourceRow = sourceRow;
				context.sourceColumn = sourceColumn;
				context.sourceIndex = sourceIndex;
				column.contexts.add(context);
			}
		}
		unlockMe(this);
	}
	
	/**
	 * Removes all column contexts from the specified position in the grid.
	 * 
	 * @param rowIndex The row index
	 * @param columnIndex The column index
	 */
	public void clearColumnContext(int rowIndex,int columnIndex) {
		lockMe(this);
		String id = ZGridColumn.getColumnId(rowIndex,columnIndex);
		ZGridFactoryColumn column = columns.get(id);
		if (column!=null) {
			column.contexts.clear();
		}
		unlockMe(this);
	}
	
	/**
	 * Builds a new grid.
	 * 
	 * @return A new grid
	 */
	public ZGrid buildNewGrid() {
		ZGrid r = null;
		lockMe(this);
		r = new ZGrid(getMessenger(),union,numRows,numColumns);
		for (ZGridFactoryColumn column: columns.values()) {
			if (column.encoder!=null) {
				r.setEncoder(column.columnIndex,column.encoder);
			} else if (column.rowIndex>0) {
				r.setProcessor(column.rowIndex,column.columnIndex,column.processorConfig.copy());
			}
			for (ZGridColumnContext context: column.contexts) {
				r.addColumnContext(column.rowIndex,column.columnIndex,context.sourceRow,context.sourceColumn,context.sourceIndex);
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
					ZGridFactoryColumn col = new ZGridFactoryColumn();
					col.columnId = id;
					for (int r = 0; r < numRows; r++) {
						for (int c = 0; c < numColumns; c++) {
							String test = ZGridColumn.getColumnId(r,c);
							if (test.equals(id)) {
								col.rowIndex = r;
								col.columnIndex = c;
							}
						}
					}
					columns.put(id,col);
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
		ZGridEncoderValue valueEncoder = new ZGridEncoderValue();
		
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
		
		ZGridEncoderValue valueEncoder = new ZGridEncoderValue(256);
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

	protected void removeColumn(int rowIndex, int columnIndex) {
		String id = ZGridColumn.getColumnId(rowIndex,columnIndex);
		columns.remove(id);
	}
	
	protected void updatedDimensions() {
		List<String> columnIds = getColumnIds();
		List<String> colIds = new ArrayList<String>(columns.keySet());
		for (String colId: colIds) {
			if (!columnIds.contains(colId)) {
				columns.remove(colId);
			}
		}
	}
	
	protected List<String> getColumnIds() {
		List<String> r = new ArrayList<String>(); 
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				r.add(ZGridColumn.getColumnId(row, col));
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
}
