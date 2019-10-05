package nl.zeesoft.zdk.htm.stream;

import java.util.List;

import nl.zeesoft.zdk.htm.proc.Classification;

public interface ValueClassifierListener {
	public void classifiedValue(StreamResult result,List<Classification> classifications);
}
