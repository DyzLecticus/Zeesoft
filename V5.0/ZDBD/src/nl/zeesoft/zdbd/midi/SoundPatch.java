package nl.zeesoft.zdbd.midi;

import javax.sound.midi.Sequence;

import nl.zeesoft.zdbd.midi.convertors.PatternSequenceConvertor;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.thread.RunCode;

public class SoundPatch {
	public SynthConfig					synthConfig	= new SynthConfig();
	public PatternSequenceConvertor		convertor	= new PatternSequenceConvertor();
	
	public void copyFrom(SoundPatch pat) {
		this.synthConfig = pat.synthConfig;
		this.convertor = pat.convertor;
	}
	
	public Sequence generateMidiSequence(PatternSequence sequence) {
		return convertor.generateSequenceForPatternSequence(sequence);
	}
	
	public RunCode getFromFileRunCode(String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				fromFile(path);
				return true;
			}
		};
	}
	
	public void fromFile(String path) {
		SoundPatch patch = (SoundPatch) PersistableCollection.fromFile(path);
		if (patch!=null) {
			copyFrom(patch);
		}
	}
	
	public RunCode getToFileRunCode(String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				toFile(path);
				return true;
			}
		};
	}
	
	public void toFile(String path) {
		PersistableCollection.toFile(this, path);
	}
}
