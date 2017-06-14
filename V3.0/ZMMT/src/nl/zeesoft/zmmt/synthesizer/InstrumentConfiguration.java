package nl.zeesoft.zmmt.synthesizer;

public class InstrumentConfiguration extends BaseConfiguration {
	private int								volume	= 110;
	private int 							pan		= 64;
	private	InstrumentLayerConfiguration	layer1	= new InstrumentLayerConfiguration();
	private	InstrumentLayerConfiguration	layer2	= new InstrumentLayerConfiguration();
	
	public InstrumentConfiguration() {
		layer2.setMidiNum(-1);
	}
	
	public InstrumentConfiguration copy() {
		InstrumentConfiguration r = new InstrumentConfiguration();
		r.setName(getName());
		r.setPan(pan);
		r.setVolume(volume);
		r.setLayer1(layer1.copy());
		r.setLayer2(layer2.copy());
		return r;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public int getPan() {
		return pan;
	}

	public void setPan(int pan) {
		this.pan = pan;
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
