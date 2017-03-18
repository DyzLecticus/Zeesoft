package nl.zeesoft.zso.composition;

public class PositionStep {
	private int		number			= 0;
	private long	startMs			= 0;
	private long	durationMs		= 0;
	
	public PositionStep(int number) {
		this.number = number;
	}

	public PositionStep(int number,long startMs,long durationMs) {
		this.number = number;
		this.startMs = startMs;
		this.durationMs = durationMs;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public long getStartMs() {
		return startMs;
	}
	
	public void setStartMs(long startMs) {
		this.startMs = startMs;
	}
	
	public long getDurationMs() {
		return durationMs;
	}
	
	public void setDurationMs(long durationMs) {
		this.durationMs = durationMs;
	}
}
