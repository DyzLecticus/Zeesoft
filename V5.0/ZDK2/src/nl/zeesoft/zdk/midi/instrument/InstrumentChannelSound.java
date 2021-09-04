package nl.zeesoft.zdk.midi.instrument;

import nl.zeesoft.zdk.midi.pattern.ChordPatternStep;

public class InstrumentChannelSound {
	public int		channel			= 0;
	public String	name			= "";
	public int		baseOctave		= 3;
	public int		chordNote		= 0;
	public float	hold			= 0.75F;
	public int		velocity		= 100;
	public float	accentHold		= 0.75F;
	public int		accentVelocity	= 127;
	
	public InstrumentChannelSound() {
		
	}
	
	public InstrumentChannelSound(int channel, String name, int baseOctave, int chordNote, float hold, int velocity, float accentHold, int accentVelocity) {
		this.name = name;
		this.channel = channel;
		this.baseOctave = baseOctave;
		this.chordNote = chordNote;
		this.hold = hold;
		this.velocity = velocity;
		this.accentHold = accentHold;
		this.accentVelocity = accentVelocity;
	}
	
	public InstrumentChannelNote getNote(boolean accent, ChordPatternStep cps) {
		InstrumentChannelNote r = new InstrumentChannelNote();
		r.channel = channel;
		if (accent) {
			r.hold = accentHold;
			r.velocity = accentVelocity;
		} else {
			r.hold = hold;
			r.velocity = velocity;
		}
		r.midiNote = (baseOctave * 12) + cps.baseNote;
		if (chordNote>0) {
			r.midiNote += cps.interval[(chordNote - 1)];
		} 
		return r;
	}
}
