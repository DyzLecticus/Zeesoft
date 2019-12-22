package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.proc.ProcessorConfigObject;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class ZGridFactory {
	private int											rows						= 4;
	private int											columns						= 2;
	
	private SortedMap<Integer,ZGridColumnEncoder>		encoders					= new TreeMap<Integer,ZGridColumnEncoder>();
	private SortedMap<String,ZGridFactoryProcessor>		processorConfigurations		= new TreeMap<String,ZGridFactoryProcessor>();
	
	public ZGridFactory(Messenger msgr, WorkerUnion union) {
	}
	
	public void setDimensions(int rows, int columns) {
		if (rows>0 && columns>0) {
			this.rows = rows;
			this.columns = columns;
			updatedDimensions();
		}
	}
	
	public void setEncoder(int columnIndex,ZGridColumnEncoder encoder) {
		encoders.put(columnIndex,encoder);
	}
	
	public void removeEncoder(int columnIndex) {
		encoders.remove(columnIndex);
	}
	
	public void addProcessor(int rowIndex,int columnIndex,ProcessorConfigObject config) {
		String id = ZGridColumn.getColumnId(rowIndex,columnIndex);
		ZGridFactoryProcessor processor = new ZGridFactoryProcessor();
		processor.columnId = id;
		processor.row = rowIndex;
		processor.column = columnIndex;
		processor.config = config;
		processorConfigurations.put(id,processor);
	}
	
	public void removeProcessor(int rowIndex,int columnIndex) {
		String id = ZGridColumn.getColumnId(rowIndex,columnIndex);
		processorConfigurations.remove(id);
	}
	
	public ZGrid buildNewGrid(Messenger msgr, WorkerUnion uni) {
		ZGrid r = null;
		r = new ZGrid(msgr,uni,rows,columns);
		for (Entry<Integer,ZGridColumnEncoder> entry: encoders.entrySet()) {
			r.setEncoder(entry.getKey(),entry.getValue().copy());
		}
		for (ZGridFactoryProcessor processor: processorConfigurations.values()) {
			r.setProcessor(processor.row,processor.column,processor.config.copy());
		}
		return r;
	}
	
	protected void updatedDimensions() {
		List<Integer> colIdxs = new ArrayList<Integer>(encoders.keySet());
		for (Integer colIdx: colIdxs) {
			if (colIdx>=columns) {
				encoders.remove(colIdx);
			}
		}
		List<String> columnIds = getColumnIds();
		List<String> colIds = new ArrayList<String>(processorConfigurations.keySet());
		for (String colId: colIds) {
			if (!columnIds.contains(colId)) {
				processorConfigurations.remove(colId);
			}
		}
	}
	
	protected List<String> getColumnIds() {
		List<String> r = new ArrayList<String>(); 
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				r.add(ZGridColumn.getColumnId(row, col));
			}
		}
		return r;
	}
}
