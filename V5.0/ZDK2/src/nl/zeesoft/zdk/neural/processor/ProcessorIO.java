package nl.zeesoft.zdk.neural.processor;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.neural.Sdr;

public class ProcessorIO {
	// TODO: timeout handling
	public int				timeoutMs	= 10000;
	public List<Sdr>		inputs		= new ArrayList<Sdr>();
	public Object			inputValue	= null;
	
	public List<Sdr>		outputs		= new ArrayList<Sdr>();
	public Object			outputValue	= null;
	public String			error		= "";
	
	public ProcessorIO() {
		
	}
	
	public ProcessorIO(Sdr ...sdrs) {
		for (Sdr sdr: sdrs) {
			inputs.add(sdr);
		}
	}
}
