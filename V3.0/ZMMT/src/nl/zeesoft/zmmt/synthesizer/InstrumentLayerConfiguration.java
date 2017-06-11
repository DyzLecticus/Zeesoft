package nl.zeesoft.zmmt.synthesizer;

public class InstrumentLayerConfiguration {
	private	int			midiNum			= 0;

	private int			modulation		= 0;
	private int 		pan				= 64;
	private int			volume			= 110;
	private int			filter			= 127;
	private int			reverb			= 24;
	private int			chorus			= 0;

	private int 		pressure		= 0;

	private int			baseOctave		= 3;
	private int			baseVelocity	= 100;
	private int			accentVelocity	= 110;
	
	public InstrumentLayerConfiguration copy() {
		InstrumentLayerConfiguration r = new InstrumentLayerConfiguration();

		r.setMidiNum(midiNum);

		r.setModulation(modulation);
		r.setPressure(pressure);
		r.setPan(pan);
		r.setVolume(volume);
		r.setFilter(filter);
		r.setReverb(reverb);
		r.setChorus(chorus);
		
		r.setBaseOctave(baseOctave);
		r.setBaseVelocity(baseVelocity);
		r.setAccentVelocity(accentVelocity);

		return r;
	}
	
	public int getMidiNum() {
		return midiNum;
	}
	
	public void setMidiNum(int midiNum) {
		this.midiNum = midiNum;
	}
	
	public int getBaseOctave() {
		return baseOctave;
	}
	
	public void setBaseOctave(int baseOctave) {
		this.baseOctave = baseOctave;
	}

	public int getPressure() {
		return pressure;
	}
	
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}
	
	public int getPan() {
		return pan;
	}

	public void setPan(int pan) {
		this.pan = pan;
	}

	public int getReverb() {
		return reverb;
	}

	public void setReverb(int reverb) {
		this.reverb = reverb;
	}

	public int getModulation() {
		return modulation;
	}

	public void setModulation(int modulation) {
		this.modulation = modulation;
	}

	public int getBaseVelocity() {
		return baseVelocity;
	}

	public void setBaseVelocity(int baseVelocity) {
		this.baseVelocity = baseVelocity;
	}

	public int getAccentVelocity() {
		return accentVelocity;
	}

	public void setAccentVelocity(int accentVelocity) {
		this.accentVelocity = accentVelocity;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getFilter() {
		return filter;
	}

	public void setFilter(int filter) {
		this.filter = filter;
	}

	public int getChorus() {
		return chorus;
	}

	public void setChorus(int chorus) {
		this.chorus = chorus;
	}
}
