package nl.zeesoft.zmmt.syntesizer;

public class InstrumentConfiguration extends VelocityConfiguration {
	private	int			layer1MidiNum		= 0;
	private int			layer1BaseOctave	= 3;
	private int 		layer1Pressure		= 0;
	private int			layer1Reverb		= 64;

	private	int			layer2MidiNum		= -1;
	private int			layer2BaseOctave	= 3;
	private int 		layer2Pressure		= 24;
	private int			layer2Reverb		= 64;

	public InstrumentConfiguration copy() {
		InstrumentConfiguration r = new InstrumentConfiguration();
		r.setName(getName());
		
		r.setLayer1MidiNum(layer1MidiNum);
		r.setLayer1BaseOctave(layer1BaseOctave);
		r.setLayer1BaseVelocity(getLayer1BaseVelocity());
		r.setLayer1AccentVelocity(getLayer1AccentVelocity());
		r.setLayer1Pressure(layer1Pressure);
		r.setLayer1Reverb(layer1Reverb);
		
		r.setLayer2MidiNum(layer2MidiNum);
		r.setLayer2BaseOctave(layer2BaseOctave);
		r.setLayer2BaseVelocity(getLayer2BaseVelocity());
		r.setLayer2AccentVelocity(getLayer2AccentVelocity());
		r.setLayer2Pressure(layer2Pressure);
		r.setLayer2Reverb(layer2Reverb);

		return r;
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
