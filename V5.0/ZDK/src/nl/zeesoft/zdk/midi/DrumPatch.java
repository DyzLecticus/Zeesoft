package nl.zeesoft.zdk.midi;

public class DrumPatch extends Patch {
	public DrumPatch() {
		
	}
	
	public DrumPatch(String name) {
		super(name);
		DrumInst di = new DrumInst();
		setDrumInstrument(di);
		di.initialize();
	}
	
	public void setDrumInstrument(DrumInst drumInstrument) {
		instruments.clear();
		instruments.add(drumInstrument);
	}
	
	public DrumInst getDrumInstrument() {
		return (DrumInst) instruments.get(0);
	}
}
