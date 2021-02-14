package nl.zeesoft.zdbd.midi.lfo;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.midi.SynthConfig;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.thread.Lock;

public class ChannelLFO {
	private Lock		lock		= new Lock();
	
	private Rythm		rythm		= new Rythm();
	
	private boolean		active		= true;
	private int			channel		= SynthConfig.BASS_CHANNEL_2;
	private int			control		= SynthConfig.FILTER;
	private String		type		= LFO.SINE;
	private int			cycleSteps	= 10;
	private float		change		= -0.35F; // -1.0F - 1.0F
	
	private long		currTick	= 0;
	
	public ChannelLFO() {
		
	}
	
	public ChannelLFO(int channel) {
		this.channel = channel;
	}
	
	public ChannelLFO(int channel, int control, String type, int cycleSteps, float change) {
		this.channel = channel;
		this.control = control;
		this.type = type;
		if (cycleSteps>0) {
			this.cycleSteps = cycleSteps;
		}
		if (change>=-1F && change<=1) {
			this.change = change;
		}
	}
	
	public ChannelLFO copy() {
		ChannelLFO r = new ChannelLFO();
		lock.lock(this);
		r.rythm.copyFrom(this.rythm);
		r.active = this.active;
		r.channel = this.channel;
		r.control = this.control;
		r.type = this.type;
		r.cycleSteps = this.cycleSteps;
		r.change = this.change;
		lock.unlock(this);
		return r;
	}
	
	public void setRythm(Rythm rythm) {
		lock.lock(this);
		this.rythm = rythm;
		lock.unlock(this);
	}
	
	public boolean isActive() {
		lock.lock(this);
		boolean r = active;
		lock.unlock(this);
		return r;
	}

	public void setActive(boolean active) {
		lock.lock(this);
		this.active = active;
		lock.unlock(this);
	}

	public int getChannel() {
		lock.lock(this);
		int r = channel;
		lock.unlock(this);
		return r;
	}
	
	public void setChannel(int channel) {
		lock.lock(this);
		this.channel = channel;
		lock.unlock(this);
	}
	
	public int getControl() {
		lock.lock(this);
		int r = control;
		lock.unlock(this);
		return r;
	}
	
	public void setControl(int control) {
		lock.lock(this);
		this.control = control;
		lock.unlock(this);
	}
	
	public String getType() {
		lock.lock(this);
		String r = type;
		lock.unlock(this);
		return r;
	}
	
	public void setType(String type) {
		lock.lock(this);
		this.type = type;
		lock.unlock(this);
	}
	
	public int getCycleSteps() {
		lock.lock(this);
		int r = cycleSteps;
		lock.unlock(this);
		return r;
	}
	
	public void setCycleSteps(int cycleSteps) {
		lock.lock(this);
		if (cycleSteps>0) {
			this.cycleSteps = cycleSteps;
		}
		lock.unlock(this);
	}
	
	public float getChange() {
		lock.lock(this);
		float r = change;
		lock.unlock(this);
		return r;
	}
	
	public void setChange(float change) {
		lock.lock(this);
		if (change>=-1F && change<=1) {
			this.change = change;
		}
		lock.unlock(this);
	}
	
	public List<Float> getChangesForTicks(long ticks) {
		List<Float> r = new ArrayList<Float>();
		lock.lock(this);
		long cTick = currTick;
		List<Float> cycleValues = LFO.getTickValuesForCycleSteps(rythm, type, cycleSteps);
		for (long t = 0; t < ticks; t++) {
			float mod = cycleValues.get((int)cTick);
			r.add(mod * change);
			cTick++;
			if (cTick>=cycleValues.size()) {
				cTick = 0;
			}
		}
		lock.unlock(this);
		return r;
	}
	
	public List<Float> commitTicks(long ticks) {
		List<Float> r = new ArrayList<Float>();
		lock.lock(this);
		currTick += ticks;
		currTick = currTick % LFO.getTotalTicksForCycleSteps(rythm, cycleSteps);
		lock.unlock(this);
		return r;
	}
}
