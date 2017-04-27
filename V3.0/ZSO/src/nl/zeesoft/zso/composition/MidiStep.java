package nl.zeesoft.zso.composition;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zso.orchestra.SampleOrchestra;

public class MidiStep extends Step {
	private String			instrument		= "";
	private	List<Integer>	notes			= new ArrayList<Integer>();
	private int				velocity		= 100;
	
	public MidiStep(String instrument,int bar,int number,List<Integer> notes,int velocity,long durationMs) {
		super(SampleOrchestra.SYNTHESIZER,bar,number);
		this.instrument = instrument;
		this.notes = notes;
		this.velocity = velocity;
		setDurationMs(durationMs);
	}
	
	public MidiStep(String instrument,int bar,int number,int note,int velocity,long durationMs) {
		super(SampleOrchestra.SYNTHESIZER,bar,number);
		this.instrument = instrument;
		this.notes.add(note);
		this.velocity = velocity;
		setDurationMs(durationMs);
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public List<Integer> getNotes() {
		return notes;
	}
}
