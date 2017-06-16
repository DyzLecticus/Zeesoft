package nl.zeesoft.zmmt.synthesizer;

public class InstrumentLayerConfiguration {
	private	int			midiNum			= 0;

	private int 		pressure		= 0;

	private int			modulation		= 0;
	private int			reverb			= 24;
	private int			chorus			= 0;

	private int			filter			= 64;
	private int			resonance		= 64;
	private int			attack			= 64;
	private int			decay			= 64;
	private int			release			= 64;
	private int			vibRate			= 64;
	private int			vibDepth		= 64;
	private int			vibDelay		= 64;

	private int			baseOctave		= 3;
	private int			baseVelocity	= 100;
	private int			accentVelocity	= 110;
	
	public InstrumentLayerConfiguration copy() {
		InstrumentLayerConfiguration r = new InstrumentLayerConfiguration();

		r.setMidiNum(midiNum);

		r.setPressure(pressure);

		r.setModulation(modulation);
		r.setReverb(reverb);
		r.setChorus(chorus);

		r.setFilter(filter);
		r.setResonance(resonance);
		r.setAttack(attack);
		r.setDecay(decay);
		r.setRelease(release);
		r.setVibRate(vibRate);
		r.setVibDepth(vibDepth);
		r.setVibDelay(vibDelay);

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

	public int getResonance() {
		return resonance;
	}

	public void setResonance(int resonance) {
		this.resonance = resonance;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDecay() {
		return decay;
	}

	public void setDecay(int decay) {
		this.decay = decay;
	}

	public int getRelease() {
		return release;
	}

	public void setRelease(int release) {
		this.release = release;
	}

	public int getVibRate() {
		return vibRate;
	}

	public void setVibRate(int vibRate) {
		this.vibRate = vibRate;
	}

	public int getVibDepth() {
		return vibDepth;
	}

	public void setVibDepth(int vibDepth) {
		this.vibDepth = vibDepth;
	}

	public int getVibDelay() {
		return vibDelay;
	}

	public void setVibDelay(int vibDelay) {
		this.vibDelay = vibDelay;
	}
}
