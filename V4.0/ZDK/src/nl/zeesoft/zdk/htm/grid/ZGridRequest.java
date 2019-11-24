package nl.zeesoft.zdk.htm.grid;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class ZGridRequest extends Locker {
	public long						id				= 0;
	public long						dateTime		= 0;
	public Object[]					inputValues		= null;
	
	public boolean					learn			= true;
	
	public SortedMap<String,SDR>	columnOutputs	= new TreeMap<String,SDR>();
	
	public ZGridRequest(Messenger msgr,int columns) {
		super(msgr);
		dateTime = System.currentTimeMillis();
		inputValues = new Object[columns];
	}
	
	public ZGridRequest(int columns) {
		super(null);
		inputValues = new Object[columns];
	}
	
	protected SDR getColumnOutput(String columnId) {
		lockMe(this);
		SDR r = columnOutputs.get(columnId);
		unlockMe(this);
		return r;
	}
	
	protected void setColumnOutput(String columnId,SDR output) {
		lockMe(this);
		columnOutputs.put(columnId,output);
		unlockMe(this);
	}
}
