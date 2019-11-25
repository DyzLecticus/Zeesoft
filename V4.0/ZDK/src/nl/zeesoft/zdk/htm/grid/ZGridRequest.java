package nl.zeesoft.zdk.htm.grid;

public class ZGridRequest {
	public long			id				= 0;
	public long			dateTime		= 0;
	public Object[]		inputValues		= null;
	public String[]		inputLabels		= null;
	
	protected boolean	learn			= true;
	
	public ZGridRequest(int columns) {
		initialize(columns);
	}
		
	protected void initialize(int columns) {
		dateTime = System.currentTimeMillis();
		inputValues = new Object[columns];
		inputLabels = new String[columns];
	}
	
	public ZGridRequest copy() {
		ZGridRequest r = new ZGridRequest(inputValues.length);
		r.id = id;
		r.dateTime = dateTime;
		for (int i = 0; i < inputValues.length; i++) {
			r.inputValues[i] = inputValues[i];
			r.inputLabels[i] = inputLabels[i];
		}
		return r;
	}
}
