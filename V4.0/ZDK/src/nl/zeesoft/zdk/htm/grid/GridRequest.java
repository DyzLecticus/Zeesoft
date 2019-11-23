package nl.zeesoft.zdk.htm.grid;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class GridRequest extends Locker {
	public long						id				= 0;
	public Object[]					inputValues		= null;
	
	public SortedMap<String,SDR>	columnInputs	= new TreeMap<String,SDR>();
	public SortedMap<String,SDR>	columnOutputs	= new TreeMap<String,SDR>();
	
	public GridRequest(Messenger msgr,int columns) {
		super(msgr);
		inputValues = new Object[columns];
	}
	
	public GridRequest(int columns) {
		super(null);
		inputValues = new Object[columns];
	}
	
	protected SDR getColumnInput(String columnId) {
		lockMe(this);
		SDR r = columnInputs.get(columnId);
		unlockMe(this);
		return r;
	}
	
	protected void setColumnInput(String columnId,SDR input) {
		lockMe(this);
		columnInputs.put(columnId,input);
		unlockMe(this);
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
