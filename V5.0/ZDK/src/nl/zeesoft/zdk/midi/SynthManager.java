package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class SynthManager {
	private static final int			DRUM_CHANNEL	= 9;
	
	private Lock						lock			= new Lock();
	private Logger						logger			= null;
	private Synth						synth			= null;
	private LfoManager					lfoManager		= null;
	
	private Inst[]						instruments 	= new Inst[16];
	private List<Patch>					patches			= new ArrayList<Patch>();
	
	protected SynthManager(Logger logger, Synth synth, LfoManager lfoManager) {
		this.logger = logger;
		this.synth = synth;
		this.lfoManager = lfoManager;
		lock.setLogger(this, logger);
	}
	
	public Str addPatch(Patch patch) {
		lock.lock(this);
		Str r = addPatchNoLock(patch);
		lock.unlock(this);
		return r;
	}
	
	public Str addPatches(List<Patch> patches) {
		Str r = new Str();
		lock.lock(this);
		for (Patch patch: patches) {
			Str error = addPatchNoLock(patch);
			if (error.length()>0) {
				if (r.length()>0) {
					r.sb().append("\n");
				}
				r.sb().append(error);
			}
		}
		lock.unlock(this);
		return r;
	}
	
	public List<String> listPatches() {
		lock.lock(this);
		List<String> r = listPatchesNoLock(false);
		lock.unlock(this);
		return r;
	}
	
	public List<String> listLoadedPatches() {
		lock.lock(this);
		List<String> r = listPatchesNoLock(true);
		lock.unlock(this);
		return r;
	}
	
	public Patch getPatch(String name) {
		lock.lock(this);
		Patch r = getPatchNoLock(name);
		if (r!=null) {
			r = r.copy();
		}
		lock.unlock(this);
		return r;
	}
	
	public void renamePatch(String name, String newName) {
		lock.lock(this);
		Patch r = getPatchNoLock(name);
		if (r!=null) {
			r.name = newName;
		}
		lock.unlock(this);
	}
	
	public Patch removePatch(String name) {
		lock.lock(this);
		Patch r = getPatchNoLock(name);
		if (r!=null) {
			if (r.isLoaded()) {
				unloadPatch(name);
			}
			patches.remove(r);
		}
		lock.unlock(this);
		return r;
	}
	
	public List<Integer> getAvailableInstrumentChannels() {
		lock.lock(this);
		List<Integer> r = getAvailableInstrumentChannelsNoLock();
		lock.unlock(this);
		return r;
	}
	
	public Str loadPatch(String name) {
		lock.lock(this);
		Str r = loadPatchNoLock(name);
		lock.unlock(this);
		return r;
	}
	
	public Str unloadPatch(String name) {
		lock.lock(this);
		Str r = unloadPatchNoLock(name);
		lock.unlock(this);
		return r;
	}
	
	public void unloadAllPatches() {
		lock.lock(this);
		for (Patch patch: patches) {
			unloadPatchNoLock(patch.name);
		}
		lock.unlock(this);
	}

	public Str addInstrumentToPatch(String name,Inst instrument) {
		lock.lock(this);
		Str r = addInstrumentToPatchNoLock(name,instrument);
		lock.unlock(this);
		return r;
	}

	public Str removeInstrumentFromPatch(String name,int channel) {
		lock.lock(this);
		Str r = removeInstrumentFromPatchNoLock(name,channel);
		lock.unlock(this);
		return r;
	}

	public void setInstrument(int channel,Inst instrument) {
		lock.lock(this);
		setInstrumentNoLock(channel, instrument);
		lock.unlock(this);
	}

	public void setInstrumentPropertyValue(int channel,String property, int value) {
		lock.lock(this);
		if (instruments[channel]!=null) {
			instruments[channel].setPropertyValue(property, value);
			lfoManager.setInstrumentPropertyValue(channel, property, value);
		}
		lock.unlock(this);
	}
	
	public Str updatePatchPatterns(String name, Pattern[] patterns) {
		lock.lock(this);
		Str r = updatePatchPatternsNoLock(name,patterns);
		lock.unlock(this);
		return r;
	}
	
	protected List<MidiNote> getNotes(String name,String... notes) {
		lock.lock(this);
		List<MidiNote> r = getNotesNoLock(name,notes);
		lock.unlock(this);
		return r;
	}
	
	protected List<MidiNote> getNotes(int channel,String... notes) {
		lock.lock(this);
		List<MidiNote> r = getNotesNoLock(channel,notes);
		lock.unlock(this);
		return r;
	}
	
	private Str addPatchNoLock(Patch patch) {
		Str error = new Str();
		patch = patch.copy();
		Patch existing = getPatchNoLock(patch.name);
		if (existing==null) {
			patches.add(patch);
		} else {
			error.sb().append("Patch already exists with name: ");
			error.sb().append(patch.name);
		}
		return error;
	}
	
	public List<String> listPatchesNoLock(boolean loadedOnly) {
		List<String> r = new ArrayList<String>();
		for (Patch patch: patches) {
			if (!loadedOnly || patch.isLoaded()) {
				r.add(patch.name);
			}
		}
		return r;
	}
	
	private Str loadPatchNoLock(String name) {
		Str error = new Str();
		Patch patch = getPatchNoLock(name);
		if (patch==null) {
			error.sb().append("Patch not found: ");
			error.sb().append(name);
		} else {
			List<Integer> availableChannels = getAvailableInstrumentChannelsNoLock();
			if (patch.instruments.size() > availableChannels.size() && !(patch instanceof DrumPatch)) {
				error.sb().append("Not enough channels available to load patch: ");
				error.sb().append(patch.name);
				error.sb().append(", ");
				error.sb().append(patch.instruments.size());
				error.sb().append(" > ");
				error.sb().append(availableChannels.size());
			} else if (patch.isLoaded()) {
				error.sb().append("Patch already loaded: ");
				error.sb().append(patch.name);
			} else {
				int[] channels = new int[patch.instruments.size()];
				int i = 0;
				if (patch instanceof DrumPatch) {
					for (Patch p: patches) {
						if (p instanceof DrumPatch && patch.isLoaded()) {
							unloadPatchNoLock(p.name);
						}
					}
				}
				for (Inst inst: patch.instruments) {
					if (patch instanceof DrumPatch && inst instanceof DrumInst) {
						channels[i] = DRUM_CHANNEL;
					} else {
						channels[i] = availableChannels.remove(0);
					}
					inst.channel = channels[i];
					setInstrumentNoLock(channels[i],inst);
					i++;
				}
			}
		}
		return error;
	}
	
	private Str unloadPatchNoLock(String name) {
		Str error = new Str();
		Patch patch = getPatchNoLock(name);
		if (patch==null) {
			error.sb().append("Patch not found: ");
			error.sb().append(name);
		} else if (!patch.isLoaded()) {
			error.sb().append("Patch not loaded: ");
			error.sb().append(name);
		} else {
			for (Inst inst: patch.instruments) {
				instruments[inst.channel] = null;
				inst.channel = -1;
			}
		}
		return error;
	}

	private Str addInstrumentToPatchNoLock(String name,Inst instrument) {
		Str error = new Str();
		Patch patch = getPatchNoLock(name);
		if (patch==null) {
			error.sb().append("Patch not found: ");
			error.sb().append(name);
		} else {
			List<Integer> availableChannels = getAvailableInstrumentChannelsNoLock();
			if (availableChannels.size()==0) {
				error.sb().append("Not enough channels available to add instrument to patch: ");
				error.sb().append(name);
			} else {
				instrument = instrument.copy();
				int channel = availableChannels.remove(0);
				patch.instruments.add(instrument);
				setInstrumentNoLock(channel, instrument);
			}
		}
		return error;
	}
	
	private Str removeInstrumentFromPatchNoLock(String name,int channel) {
		Str error = new Str();
		Patch patch = getPatchNoLock(name);
		if (patch==null) {
			error.sb().append("Patch not found: ");
			error.sb().append(name);
		} else {
			boolean removed = false;
			List<Inst> list = new ArrayList<Inst>(patch.instruments);
			for (Inst inst: list) {
				if (inst.channel==channel) {
					removed = patch.instruments.remove(inst);
					instruments[inst.channel] = null;
					inst.channel = -1;
					break;
				}
			}
			if (!removed) {
				error.sb().append("Patch instrument not loaded on channel: ");
				error.sb().append(channel);
			}
		}
		return error;
	}
	
	private void setInstrumentNoLock(int channel,Inst instrument) {
		instrument = instrument.copy();
		if (channel>=0 && channel<16 && (channel!=DRUM_CHANNEL || instrument instanceof DrumInst)) {
			instruments[channel] = instrument;
			instrument.channel = channel;
			synth.setInstrument(channel, instrument);
			lfoManager.setInstrument(channel, instrument.copy());
		} else {
			Str error = new Str();
			error.sb().append("Invalid instrument channel: ");
			error.sb().append(channel);
			logger.error(this, error);
		}
	}

	private Str updatePatchPatternsNoLock(String name, Pattern[] patterns) {
		Str error = new Str();
		Patch patch = getPatchNoLock(name);
		if (patch==null) {
			error.sb().append("Patch not found: ");
			error.sb().append(name);
		} else {
			for (int i = 0; i < patterns.length; i++) {
				patch.patterns[i] = patterns[i].copy();
			}
		}
		return error;
	}
	
	private List<Integer> getAvailableInstrumentChannelsNoLock() {
		List<Integer> r = new ArrayList<Integer>();
		for (int i = 0; i < 16; i++) {
			if (i!=DRUM_CHANNEL) {
				if (instruments[i]==null) {
					r.add(i);
				}
			}
		}
		return r;
	}
	
	private Patch getPatchNoLock(String name) {
		Patch r = null;
		for (Patch patch: patches) {
			if (patch.name.equals(name)) {
				r = patch;
				break;
			}
		}
		return r;
	}
	
	private List<MidiNote> getNotesNoLock(String patchName,String... notes) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		Patch patch = getPatchNoLock(patchName);
		if (patch!=null) {
			for (Inst inst: patch.instruments) {
				List<MidiNote> mns = getNotesNoLock(inst.channel,notes);
				for (MidiNote note: mns) {
					note.delaySteps = inst.patchDelaySteps;
					r.add(note);
				}
			}
		}
		return r;
	}
	
	private List<MidiNote> getNotesNoLock(int channel,String... notes) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		Inst inst = instruments[channel];
		if (inst!=null) {
			r = inst.getNotes(notes);
			for (MidiNote note: r) {
				note.channel = channel;
			}
		}
		return r;
	}
}
