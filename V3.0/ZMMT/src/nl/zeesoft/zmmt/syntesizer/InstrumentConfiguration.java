package nl.zeesoft.zmmt.syntesizer;

public class InstrumentConfiguration extends VelocityConfiguration {
	private String		name				= "";
	private int			channelNum			= 0;
	private	int			midiNum				= 0;
	private int			baseOctave			= 3;
	private int			polyphony			= 4;

	private	int			layerMidiNum		= -1;
	private int			layerBaseOctave		= 3;
	
	public int getChannelNum() {
		return channelNum;
	}
	
	public void setChannelNum(int channelNum) {
		this.channelNum = channelNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getLayerMidiNum() {
		return layerMidiNum;
	}

	public void setLayerMidiNum(int layerMidiNum) {
		this.layerMidiNum = layerMidiNum;
	}

	public int getLayerBaseOctave() {
		return layerBaseOctave;
	}

	public void setLayerBaseOctave(int layerBaseOctave) {
		this.layerBaseOctave = layerBaseOctave;
	}
}
