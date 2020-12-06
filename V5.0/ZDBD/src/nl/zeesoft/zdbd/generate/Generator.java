package nl.zeesoft.zdbd.generate;

import nl.zeesoft.zdbd.pattern.PatternSequence;

public class Generator {
	public String			name						= "";
	
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
	
	public Generator copy() {
		Generator r = new Generator();
		r.copyFrom(this);
		return r;
	}

	public void copyFrom(Generator gen) {
		this.name = gen.name;
		this.group1Distortion = gen.group1Distortion;
		this.group2Distortion = gen.group2Distortion;
		this.smallerChunk = gen.smallerChunk;
		this.largerChunk = gen.largerChunk;
		this.randomChunkOffset = gen.randomChunkOffset;
		this.setSkipInstruments(gen.skipInstruments);
		this.mixStart = gen.mixStart;
		this.mixEnd = gen.mixEnd;
		this.maintainBeat = gen.maintainBeat;
		this.maintainFeedback = gen.maintainFeedback;
		if (gen.generatedPatternSequence!=null) {
			this.generatedPatternSequence = gen.generatedPatternSequence.copy();
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
