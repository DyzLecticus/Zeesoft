package nl.zeesoft.zdbd.generate;

import nl.zeesoft.zdbd.pattern.PatternSequence;

public class GeneratorIO {
	// Network merger dirsortion controls
	public float			group1Distortion			= 0.1F; // 0 - 1
	public float			group2Distortion			= 0.1F; // 0 - 1
	
	// Randomized rythm generation controls
	public int				smallerChunk				= 1;
	public int				largerChunk					= 2;
	public boolean			randomChunkOffset			= true;
	
	// Mix controls
	public String[]			skipInstruments				= new String[0];
	public float			mixStart					= 0.2F; // 0 - 1
	public float			mixEnd						= 1.0F; // 0 - 1
	public float			maintainBeat				= 1.0F; // 0 - 1
	public boolean			maintainFeedback			= false;
	
	// Output
	public PatternSequence	generatedPatternSequence	= null;
	
	public GeneratorIO copy() {
		GeneratorIO r = new GeneratorIO();
		r.copyFrom(this);
		return r;
	}

	public void copyFrom(GeneratorIO io) {
		this.group1Distortion = io.group1Distortion;
		this.group2Distortion = io.group2Distortion;
		this.smallerChunk = io.smallerChunk;
		this.largerChunk = io.largerChunk;
		this.randomChunkOffset = io.randomChunkOffset;
		this.setSkipInstruments(io.skipInstruments);
		this.mixStart = io.mixStart;
		this.mixEnd = io.mixEnd;
		this.maintainBeat = io.maintainBeat;
		this.maintainFeedback = io.maintainFeedback;
		if (io.generatedPatternSequence!=null) {
			this.generatedPatternSequence = io.generatedPatternSequence.copy();
		} else {
			this.generatedPatternSequence = null;
		}
	}

	public void setSkipInstruments(String ... names) {
		skipInstruments = new String[names.length];
		for (int n = 0; n < names.length; n++) {
			skipInstruments[n] = names[n];
		}
	}
}
