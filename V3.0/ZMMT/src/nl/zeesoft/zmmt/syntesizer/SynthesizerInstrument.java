package nl.zeesoft.zmmt.syntesizer;

public class SynthesizerInstrument {
	private String		instrument	= "";
	private int			channelNum	= 0;
	private	int			midiNum		= 0;

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
}
