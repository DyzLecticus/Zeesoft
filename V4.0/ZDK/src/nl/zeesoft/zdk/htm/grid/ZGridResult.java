package nl.zeesoft.zdk.htm.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.proc.Classifier;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

/**
 * A ZGridResult is sent to ZGridResultListener implementations to communicate ZGridRequest processing results.
 */
public class ZGridResult extends Locker {
	private ZGridRequest					request			= null;
	private SortedMap<String,List<SDR>>		columnOutputs	= new TreeMap<String,List<SDR>>();
	private List<Classification>			classifications	= new ArrayList<Classification>();
	
	public ZGridResult(Messenger msgr,ZGridRequest request) {
		super(msgr);
		this.request = request;
	}
	
	/**
	 * Returns the request that led to this result.
	 * 
	 * @return The request
	 */
	public ZGridRequest getRequest() {
		return request;
	}
	
	/**
	 * Returns a list of all the column ids in this result.
	 * 
	 * @return A list of columns ids
	 */
	public List<String> getColumnIds() {
		lockMe(this);
		List<String> r = new ArrayList<String>(columnOutputs.keySet());
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns the specified column output SDR for a certain column.
	 * 
	 * @param columnId The id of the column
	 * @param index The index of the column output SDR
	 * @return The column output SDR
	 */
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

	/**
	 * Returns the first column output SDR for a certain column.
	 * 
	 * @param columnId The id of the column
	 * @return The column output SDR
	 */
	public List<SDR> getColumnOutput(String columnId) {
		lockMe(this);
		List<SDR> r = new ArrayList<>(columnOutputs.get(columnId));
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns all Classification objects of all column output SDRs in this result.
	 *  
	 * @return All Classification objects in this result
	 */
	public List<Classification> getClassifications() {
		lockMe(this);
		List<Classification> r = new ArrayList<Classification>(classifications);
		unlockMe(this);
		return r;
	}
	
	protected void setColumnOutput(String columnId,List<SDR> outputs) {
		lockMe(this);
		if (outputs==null) {
			columnOutputs.remove(columnId);
		} else {
			columnOutputs.put(columnId,outputs);
			for (SDR outputSDR: outputs) {
				if (outputSDR instanceof DateTimeSDR) {
					DateTimeSDR classificationSDR = (DateTimeSDR) outputSDR;
					if (classificationSDR.keyValues.containsKey(Classifier.CLASSIFICATION_KEY)) {
						Object obj = classificationSDR.keyValues.get(Classifier.CLASSIFICATION_KEY);
						if (obj instanceof Classification) {
							classifications.add((Classification) obj);
						}
					}
				}
			}
		}
		unlockMe(this);
	}
}
