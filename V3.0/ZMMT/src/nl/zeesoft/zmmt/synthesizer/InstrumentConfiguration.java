package nl.zeesoft.zmmt.synthesizer;

public class InstrumentConfiguration extends BaseConfiguration {
	private	InstrumentLayerConfiguration	layer1 = new InstrumentLayerConfiguration();
	private	InstrumentLayerConfiguration	layer2 = new InstrumentLayerConfiguration();
	
	public InstrumentConfiguration() {
		layer2.setMidiNum(-1);
	}
	
	public InstrumentConfiguration copy() {
		InstrumentConfiguration r = new InstrumentConfiguration();
		r.setName(getName());
		r.setLayer1(layer1.copy());
		r.setLayer2(layer1.copy());
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
