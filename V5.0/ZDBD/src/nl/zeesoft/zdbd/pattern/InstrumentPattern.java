package nl.zeesoft.zdbd.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.encoders.BassEncoder;
import nl.zeesoft.zdbd.neural.encoders.CymbalEncoder;
import nl.zeesoft.zdbd.neural.encoders.DrumEncoder;
import nl.zeesoft.zdbd.neural.encoders.EncoderFactory;
import nl.zeesoft.zdbd.neural.encoders.HihatEncoder;
import nl.zeesoft.zdbd.neural.encoders.NoteEncoder;
import nl.zeesoft.zdbd.neural.encoders.OctaveEncoder;
import nl.zeesoft.zdbd.neural.encoders.PercussionEncoder;
import nl.zeesoft.zdbd.pattern.instruments.Bass;
import nl.zeesoft.zdbd.pattern.instruments.Crash;
import nl.zeesoft.zdbd.pattern.instruments.Hihat;
import nl.zeesoft.zdbd.pattern.instruments.Kick;
import nl.zeesoft.zdbd.pattern.instruments.Note;
import nl.zeesoft.zdbd.pattern.instruments.Octave;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdbd.pattern.instruments.Percussion1;
import nl.zeesoft.zdbd.pattern.instruments.Percussion2;
import nl.zeesoft.zdbd.pattern.instruments.Ride;
import nl.zeesoft.zdbd.pattern.instruments.Snare;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;

public class InstrumentPattern {
	public static List<PatternInstrument>	INSTRUMENTS			= getInstruments();
	
	public int 								num					= 0;
	public List<PatternInstrument>			instruments			= getInstruments();
	
	public InstrumentPattern copy() {
		InstrumentPattern r = new InstrumentPattern();
		r.copyFrom(this);
		return r;
	}

	public void copyFrom(InstrumentPattern pat) {
		this.num = pat.num;
		this.instruments = getInstruments();
		int i = 0;
		for (PatternInstrument inst: pat.instruments) {
			this.instruments.get(i).copyFrom(inst);
			i++;
		}
	}
	
	public List<String> getInstrumentNames() {
		List<String> r = new ArrayList<String>();
		for (PatternInstrument inst: instruments) {
			r.add(inst.name());
		}
		return r;
	}
	
	public PatternInstrument getInstrument(String name) {
		PatternInstrument r = null;
		for (PatternInstrument inst: instruments) {
			if (inst.name().equals(name)) {
				r = inst;
				break;
			}
		}
		return r;
	}

	public void clear() {
		this.instruments = getInstruments();
	}
	
	public void setStepValue(String name, int step, int value) {
		if (value>=0 && step>=0 && step<64) {
			PatternInstrument inst = getInstrument(name);
			if (inst!=null) {
				inst.stepValues[step] = value;
			}
		}
	}

	public int getStepValue(String name, int step) {
		int r = 0;
		if (step>=0 && step<64) {
			PatternInstrument inst = getInstrument(name);
			if (inst!=null) {
				r = inst.stepValues[step];
			}
		}
		return r;
	}
	
	public void setKick(int step, int value) {
		setStepValue(Kick.NAME,step,value);
	}
	
	public void setSnare(int step, int value) {
		setStepValue(Snare.NAME,step,value);
	}
	
	public void setHihat(int step, boolean open, int value) {
		if (open) {
			value += 2;
		}
		setStepValue(Hihat.NAME,step,value);
	}
	
	public void setRide(int step, int value) {
		setStepValue(Ride.NAME,step,value);
	}
	
	public void setCrash(int step, int value) {
		setStepValue(Crash.NAME,step,value);
	}
	
	public void setPercussion1(int step, int value) {
		setStepValue(Percussion1.NAME,step,value);
	}
	
	public void setPercussion2(int step, int value) {
		setStepValue(Percussion2.NAME,step,value);
	}
	
	public void setBass(int step, int duration, boolean accent) {
		if (duration>=1 && duration<=8) {
			setStepValue(Bass.NAME,step,getValueForDuration(duration,accent));
		} else {
			setStepValue(Bass.NAME,step,PatternInstrument.OFF);
		}
	}
	
	public void setNote(int step, int octave, int note) {
		octave = octave % 3;
		note = note % 12;
		setStepValue(Octave.NAME,step,octave);
		setStepValue(Note.NAME,step,note);
	}
	
	public static boolean isAccent(int value) {
		boolean r = false;
		if (value>0) {
			r = value % 2 == 0;
		}
		return r;
	}
	
	public static boolean isOpenHihat(int value) {
		boolean r = false;
		if (value>=3) {
			r = true;
		}
		return r;
	}
	
	public static int getValueForDuration(int duration, boolean accent) {
		int r = 0;
		if (duration>=1 && duration<=8) {
			r = (duration * 2) - 1;
			if (accent) {
				r++;
			}
		}
		return r;
	}
	
	public static int getDurationForValue(int value) {
		int r = 0;
		if (value>0) {
			boolean odd = value % 2 == 1;
			if (odd) {
				value = value + 1;
			}
			r = value / 2;
		}
		return r;
	}
	
	public List<SDR> getSDRsForGroup(int group, int stepsPerPattern) {
		List<SDR> r = new ArrayList<SDR>();
		for (int s = 0; s < stepsPerPattern; s++) {
			if (group==1) {
				r.add(getSDRForGroup1Step(s));
			} else if (group==2) {
				r.add(getSDRForGroup2Step(s));
			}
		}
		return r;
	}

	public SDR getSDRForGroup1Step(int step) {
		DrumEncoder dEnc = EncoderFactory.drumEncoder;
		HihatEncoder hEnc = EncoderFactory.hihatEncoder;
		CymbalEncoder cEnc = EncoderFactory.cymbalEncoder;
		
		int l = dEnc.getEncodeLength() + dEnc.getEncodeLength() + hEnc.getEncodeLength() + cEnc.getEncodeLength();
		KeyValueSDR kvSdr = new KeyValueSDR(l , 1);
		
		int offset = 0;
		kvSdr.concat(dEnc.getEncodedValue(getStepValue(Kick.NAME,step)), offset);
		offset += dEnc.getEncodeLength();
		
		kvSdr.concat(dEnc.getEncodedValue(getStepValue(Snare.NAME,step)), offset);
		offset += dEnc.getEncodeLength();
		
		kvSdr.concat(hEnc.getEncodedValue(getStepValue(Hihat.NAME,step)), offset);
		offset += hEnc.getEncodeLength();
		
		int[] cymbalValue = {getStepValue(Ride.NAME,step),getStepValue(Crash.NAME,step)};
		kvSdr.concat(cEnc.getEncodedValue(cymbalValue), offset);

		for (PatternInstrument inst: instruments) {
			if (inst.group()==1) {
				kvSdr.put(inst.name(), inst.stepValues[step]);
			}
		}
		
		return kvSdr;
	}

	public SDR getSDRForGroup2Step(int step) {
		PercussionEncoder pEnc = EncoderFactory.percussionEncoder;
		OctaveEncoder oEnc = EncoderFactory.octaveEncoder;
		NoteEncoder nEnc = EncoderFactory.noteEncoder;
		BassEncoder bEnc = EncoderFactory.bassEncoder;
		
		int l = pEnc.getEncodeLength() + bEnc.getEncodeLength() + oEnc.getEncodeLength() + nEnc.getEncodeLength();
		KeyValueSDR kvSdr = new KeyValueSDR(l , 1);
		
		int offset = 0;
		int[] percussionValue = {getStepValue(Percussion1.NAME,step),getStepValue(Percussion2.NAME,step)};
		kvSdr.concat(pEnc.getEncodedValue(percussionValue), offset);
		offset += pEnc.getEncodeLength();

		kvSdr.concat(bEnc.getEncodedValue(getStepValue(Bass.NAME,step)), offset);
		offset += bEnc.getEncodeLength();

		kvSdr.concat(oEnc.getEncodedValue(getStepValue(Octave.NAME,step)), offset);
		offset += oEnc.getEncodeLength();
		
		kvSdr.concat(nEnc.getEncodedValue(getStepValue(Note.NAME,step)), offset);

		for (PatternInstrument inst: instruments) {
			if (inst.group()==2) {
				kvSdr.put(inst.name(), inst.stepValues[step]);
			}
		}
		
		return kvSdr;
	}

	public static int sizeX(int group) {
		return groupSDR(group).sizeX();
	}

	public static int sizeY(int group) {
		return groupSDR(group).sizeY();
	}
	
	private static SDR groupSDR(int group) {
		SDR r = null;
		InstrumentPattern p = new InstrumentPattern();
		if (group==1) {
			r = p.getSDRForGroup1Step(0);
		} else if (group==2) {
			r = p.getSDRForGroup2Step(0);
		}
		r.flatten();
		r.square();
		return r;
	}
	
	private static List<PatternInstrument> getInstruments() {
		List<PatternInstrument> r = new ArrayList<PatternInstrument>();
		r.add(new Kick(0));
		r.add(new Snare(1));
		r.add(new Hihat(2));
		r.add(new Ride(3));
		r.add(new Crash(4));
		r.add(new Percussion1(5));
		r.add(new Percussion2(6));
		r.add(new Bass(7));
		r.add(new Octave(8));
		r.add(new Note(9));
		return r;
	}
}
