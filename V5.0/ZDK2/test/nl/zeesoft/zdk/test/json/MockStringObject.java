package nl.zeesoft.zdk.test.json;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.HistoricalFloat;

public class MockStringObject {
	public HistoricalFloat			hist	= new HistoricalFloat();
	public List<HistoricalFloat>	hists	= new ArrayList<HistoricalFloat>();
	
	public void change() {
		hist.push(1.0F);
		hist.push(0.0F);
		hist.push(1.0F);
		hist.push(1.0F);
		
		HistoricalFloat hf = new HistoricalFloat();
		hf.push(1.0F);
		hf.push(1.0F);
		hf.push(0.0F);
		hf.push(0.0F);
		hists.add(hf);
		
		hf = new HistoricalFloat();
		hf.push(0.0F);
		hf.push(0.0F);
		hf.push(0.0F);
		hf.push(1.0F);
		hists.add(hf);
	}
}
