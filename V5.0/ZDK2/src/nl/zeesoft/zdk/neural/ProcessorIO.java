package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

public class ProcessorIO {
	public List<Sdr>		inputs	= new ArrayList<Sdr>();
	public List<Sdr>		outputs	= new ArrayList<Sdr>();
	public String			error	= "";
	
	public ProcessorIO() {
		
	}
	
	public ProcessorIO(Sdr ...sdrs) {
		for (Sdr sdr: sdrs) {
			inputs.add(sdr);
		}
	}
}
