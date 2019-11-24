package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class ZGridRequest extends Locker {
	public long								id				= 0;
	public long								dateTime		= 0;
	public Object[]							inputValues		= null;
	
	protected boolean						learn			= true;
	
	private SortedMap<String,List<SDR>>		columnOutputs	= new TreeMap<String,List<SDR>>();
	
	public ZGridRequest(Messenger msgr,int columns) {
		super(msgr);
		dateTime = System.currentTimeMillis();
		inputValues = new Object[columns];
	}
	
	public ZGridRequest(int columns) {
		super(null);
		inputValues = new Object[columns];
	}
	
	public List<String> getColumnIds() {
		lockMe(this);
		List<String> r = new ArrayList<String>(columnOutputs.keySet());
		unlockMe(this);
		return r;
	}
	
	public SDR getColumnOutput(String columnId,int index) {
		lockMe(this);
		SDR r = null;
		List<SDR> outputs = columnOutputs.get(columnId);
		if (outputs!=null && outputs.size()>index) {
			r = outputs.get(index);
		}
		unlockMe(this);
		return r;
	}

	public List<SDR> getColumnOutput(String columnId) {
		lockMe(this);
		List<SDR> r = new ArrayList<>(columnOutputs.get(columnId));
		unlockMe(this);
		return r;
	}
	
	protected void setColumnOutput(String columnId,List<SDR> outputs) {
		lockMe(this);
		columnOutputs.put(columnId,outputs);
		unlockMe(this);
	}
}
