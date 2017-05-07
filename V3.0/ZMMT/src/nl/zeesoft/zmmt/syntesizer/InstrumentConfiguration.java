package nl.zeesoft.zmmt.syntesizer;

public class InstrumentConfiguration extends VelocityConfiguration {
	private int			tracks				= 4;

	private	int			layer1MidiNum		= 0;
	private int			layer1BaseOctave	= 3;
	private int 		layer1Pressure		= 0;
	private int			layer1Reverb		= 64;

	private	int			layer2MidiNum		= -1;
	private int			layer2BaseOctave	= 3;
	private int 		layer2Pressure		= 24;
	private int			layer2Reverb		= 64;

	public int getTracks() {
		return tracks;
	}
	
	public void setTracks(int tracks) {
		this.tracks = tracks;
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

	public int getLayer1Pressure() {
		return layer1Pressure;
	}
	
	public void setLayer1Pressure(int layer1Pressure) {
		this.layer1Pressure = layer1Pressure;
	}
	
	public int getLayer1Reverb() {
		return layer1Reverb;
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

	public void setLayer1Reverb(int layer1Reverb) {
		this.layer1Reverb = layer1Reverb;
	}
	
	public int getLayer2Pressure() {
		return layer2Pressure;
	}
	
	public void setLayer2Pressure(int layer2Pressure) {
		this.layer2Pressure = layer2Pressure;
	}

	public int getLayer2Reverb() {
		return layer2Reverb;
	}

	public void setLayer2Reverb(int layer2Reverb) {
		this.layer2Reverb = layer2Reverb;
	}
}
