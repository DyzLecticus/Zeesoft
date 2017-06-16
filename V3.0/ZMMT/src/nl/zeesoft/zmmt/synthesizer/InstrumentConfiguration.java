package nl.zeesoft.zmmt.synthesizer;

public class InstrumentConfiguration extends BaseConfiguration {
	private boolean							muted			= false;
	private int								volume			= 110;
	private int 							pan				= 64;
	private int								holdPercentage	= 75;
	private	InstrumentLayerConfiguration	layer1			= new InstrumentLayerConfiguration();
	private	InstrumentLayerConfiguration	layer2			= new InstrumentLayerConfiguration();
	
	public InstrumentConfiguration() {
		layer2.setMidiNum(-1);
	}
	
	public InstrumentConfiguration copy() {
		InstrumentConfiguration r = new InstrumentConfiguration();
		r.setName(getName());
		r.setMuted(muted);
		r.setPan(pan);
		r.setVolume(volume);
		r.setHoldPercentage(holdPercentage);
		r.setLayer1(layer1.copy());
		r.setLayer2(layer2.copy());
		return r;
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}	

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getHoldPercentage() {
		return holdPercentage;
	}

	public void setHoldPercentage(int holdPercentage) {
		this.holdPercentage = holdPercentage;
	}
	
	public int getPan() {
		return pan;
	}

	public void setPan(int pan) {
		this.pan = pan;
	}

	public InstrumentLayerConfiguration getLayer(int layer) {
		InstrumentLayerConfiguration r = null;
		if (layer==0) {
			r = layer1;
		} else if (layer==1) {
			r = layer2;
		}
		return r;
	}


	public InstrumentLayerConfiguration getLayer1() {
		return layer1;
	}

	public void setLayer1(InstrumentLayerConfiguration layer1) {
		this.layer1 = layer1;
	}

	public InstrumentLayerConfiguration getLayer2() {
		return layer2;
	}

	public void setLayer2(InstrumentLayerConfiguration layer2) {
		this.layer2 = layer2;
	}
}
