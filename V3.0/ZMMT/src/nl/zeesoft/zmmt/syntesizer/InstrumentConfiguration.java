package nl.zeesoft.zmmt.syntesizer;

public class InstrumentConfiguration extends VelocityConfiguration {
	private String		instrument	= "";
	private int			channelNum	= 0;
	private	int			midiNum		= 0;
	private int			baseOctave	= 3;
	private int			polyphony	= 4;

	public int getChannelNum() {
		return channelNum;
	}
	
	public void setChannelNum(int channelNum) {
		this.channelNum = channelNum;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
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
	
	public int getPolyphony() {
		return polyphony;
	}
	
	public void setPolyphony(int polyphony) {
		this.polyphony = polyphony;
	}
}
