package nl.zeesoft.zdk.midi;

public class Groove {
	protected float			beatsPerMinute	= 120;
	protected int			stepsPerBeat	= 4;
	protected int			beatsPerBar		= 4;
	protected int			bars			= 4;
	protected float			shuffle			= 0.15F;

	public synchronized void copyFrom(Groove other) {
		this.beatsPerMinute = other.beatsPerMinute;
		this.stepsPerBeat = other.stepsPerBeat;
		this.beatsPerBar = other.beatsPerBar;
		this.bars = other.bars;
		this.shuffle = other.shuffle;
	}
	
	public synchronized float getBeatsPerMinute() {
		return beatsPerMinute;
	}

	public synchronized void setBeatsPerMinute(float beatsPerMinute) {
		if (beatsPerMinute < 40F) {
			beatsPerMinute = 40F;
		}
		if (beatsPerMinute > 240F) {
			beatsPerMinute = 240F;
		}
		this.beatsPerMinute = beatsPerMinute;
	}

	public synchronized int getStepsPerBeat() {
		return stepsPerBeat;
	}
	
	public synchronized void setStepsPerBeat(int stepsPerBeat) {
		if (stepsPerBeat < 2) {
			stepsPerBeat = 2;
		}
		if (stepsPerBeat > 8) {
			stepsPerBeat = 8;
		}
		this.stepsPerBeat = stepsPerBeat;
	}
	
	public synchronized int getBeatsPerBar() {
		return beatsPerBar;
	}

	public synchronized void setBeatsPerBar(int beatsPerBar) {
		if (beatsPerBar < 2) {
			beatsPerBar = 2;
		}
		if (beatsPerBar > 8) {
			beatsPerBar = 8;
		}
		this.beatsPerBar = beatsPerBar;
	}

	public synchronized int getBars() {
		return bars;
	}

	public synchronized void setBars(int bars) {
		if (bars < 1) {
			bars = 1;
		}
		if (bars > 8) {
			bars = 8;
		}
		this.bars = bars;
	}
	
	public synchronized float getShuffle() {
		return shuffle;
	}

	public synchronized void setShuffle(float shuffle) {
		if (shuffle < 0.0F) {
			shuffle = 0.0F;
		}
		if (shuffle > 0.75F) {
			shuffle = 0.75F;
		}
		this.shuffle = shuffle;
	}
	
	public synchronized int getStepsPerBar() {
		return stepsPerBeat * beatsPerBar;
	}
	
	public synchronized int getTotalSteps() {
		return getStepsPerBar() * bars;
	}
	
	public synchronized float getShuffleForBeatStep(int step) {
		float r = 0.0F;
		if (step % 2 == 1) {
			r = shuffle;
		}
		return r;
	}
}
