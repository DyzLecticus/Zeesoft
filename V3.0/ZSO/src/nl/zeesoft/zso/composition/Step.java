package nl.zeesoft.zso.composition;

public class Step {
	private String	positionName	= "";
	private	int		bar				= 1;
	private int		number			= 1;
	private long	startMs			= 0;
	private long	durationMs		= 0;
	
	public Step(String positionName,int bar,int number) {
		this.positionName = positionName;
		this.bar = bar;
		this.number = number;
	}

	public Step(String positionName,int bar,int number,long startMs,long durationMs) {
		this.positionName = positionName;
		this.bar = bar;
		this.number = number;
		this.startMs = startMs;
		this.durationMs = durationMs;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public int getBar() {
		return bar;
	}

	public void setBar(int bar) {
		this.bar = bar;
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
