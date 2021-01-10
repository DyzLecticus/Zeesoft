package nl.zeesoft.zdbd.midi;

public class MixState {
	public String		name			= "";
	public Boolean[]	muteChannels	= new Boolean[16];
	public Boolean[]	muteDrums		= new Boolean[7];
	
	public MixState() {
		for (int i = 0; i < muteChannels.length; i++) {
			muteChannels[i] = false;
		}
		for (int i = 0; i < muteDrums.length; i++) {
			muteDrums[i] = false;
		}
	}
	
	public MixState copy() {
		MixState r = new MixState();
		r.copyFrom(this);
		return r;
	}
	
	public void copyFrom(MixState state) {
		this.name = state.name;
		for (int i = 0; i < this.muteChannels.length; i++) {
			this.muteChannels[i] = state.muteChannels[i];
		}
		for (int i = 0; i < this.muteDrums.length; i++) {
			this.muteDrums[i] = state.muteDrums[i];
		}
	}
}
