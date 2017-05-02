package nl.zeesoft.zmmt.syntesizer;

public class InstrumentConfiguration extends VelocityConfiguration {
	private String		name				= "";
	private int			polyphony			= 4;

	private	int			layer1MidiNum		= 0;
	private int			layer1BaseOctave	= 3;

	private	int			layer2MidiNum		= -1;
	private int			layer2BaseOctave	= 3;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getPolyphony() {
		return polyphony;
	}
	
	public void setPolyphony(int polyphony) {
		this.polyphony = polyphony;
	}
	
	public int getLayer1MidiNum() {
		return layer1MidiNum;
	}
	
	public void setLayer1MidiNum(int midiNum) {
		this.layer1MidiNum = midiNum;
	}
	
	public int getLayer1BaseOctave() {
		return layer1BaseOctave;
	}
	
	public void setLayer1BaseOctave(int baseOctave) {
		this.layer1BaseOctave = baseOctave;
	}

	public int getLayer2MidiNum() {
		return layer2MidiNum;
	}

	public void setLayer2MidiNum(int midiNum) {
		this.layer2MidiNum = midiNum;
	}

	public int getLayer2BaseOctave() {
		return layer2BaseOctave;
	}

	public void setLayer2BaseOctave(int baseOctave) {
		this.layer2BaseOctave = baseOctave;
	}
}
