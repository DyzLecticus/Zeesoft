package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class PatchConfig {
	private Lock						lock			= new Lock();
	private Logger						logger			= null;
	private Synth						synth			= null;
	private LfoManager					lfoManager		= null;
	
	private Inst[]						instruments 	= new Inst[16];
	private List<InstGroup>				groups			= new ArrayList<InstGroup>();
	
	protected PatchConfig(Logger logger, Synth synth, LfoManager lfoManager) {
		this.logger = logger;
		this.synth = synth;
		this.lfoManager = lfoManager;
		lock.setLogger(this, logger);
	}
	
	public Str loadPatch(Patch patch) {
		lock.lock(this);
		Str r = loadPatchNoLock(patch);
		lock.unlock(this);
		return r;
	}
	
	public List<String> listPatches() {
		lock.lock(this);
		List<String> r = new ArrayList<String>();
		for (InstGroup group: groups) {
			r.add(group.name);
		}
		lock.unlock(this);
		return r;
	}
	
	public Patch getPatch(String name) {
		lock.lock(this);
		Patch r = getPatchNoLock(name);
		lock.unlock(this);
		return r;
	}
	
	public Str unloadPatch(String name) {
		lock.lock(this);
		Str r = unloadPatchNoLock(name);
		lock.unlock(this);
		return r;
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
	
	public List<Integer> getAvailableInstrumentChannels() {
		lock.lock(this);
		List<Integer> r = getAvailableInstrumentChannelsNoLock();
		lock.unlock(this);
		return r;
	}
	
	protected List<MidiNote> getNotes(String groupName,String... notes) {
		lock.lock(this);
		List<MidiNote> r = getNotesNoLock(groupName,notes);
		lock.unlock(this);
		return r;
	}
	
	protected List<MidiNote> getNotes(int channel,String... notes) {
		lock.lock(this);
		List<MidiNote> r = getNotesNoLock(channel,notes);
		lock.unlock(this);
		return r;
	}
	
	private List<Integer> getAvailableInstrumentChannelsNoLock() {
		List<Integer> r = new ArrayList<Integer>();
		for (int i = 0; i < 16; i++) {
			if (i!=10) {
				if (instruments[i]==null) {
					r.add(i);
				}
			}
		}
		return r;
	}
	
	private Str loadPatchNoLock(Patch patch) {
		Str error = new Str();
		List<Integer> availableChannels = getAvailableInstrumentChannelsNoLock();
		if (patch.instruments.size() > availableChannels.size()) {
			error.sb().append("Not enough channels available to load patch: ");
			error.sb().append(patch.name);
			error.sb().append(", ");
			error.sb().append(patch.instruments.size());
			error.sb().append(" > ");
			error.sb().append(availableChannels.size());
			logger.error(this, error);
		} else if (getGroupNoLock(patch.name)!=null) {
			error.sb().append("Patch already loaded: ");
			error.sb().append(patch.name);
			logger.error(this, error);
		} else {
			int[] channels = new int[patch.instruments.size()];
			int i = 0;
			for (Inst inst: patch.instruments) {
				channels[i] = availableChannels.remove(0);
				setInstrumentNoLock(channels[i],inst);
				i++;
			}
			groupInstrumentsNoLock(patch.name,channels);
		}
		return error;
	}
	
	private Patch getPatchNoLock(String name) {
		Patch r = null;
		InstGroup group = getGroupNoLock(name);
		if (group==null) {
			Str error = new Str();
			error.sb().append("Patch not found: ");
			error.sb().append(name);
			logger.error(this, error);
		} else {
			r = new Patch();
			r.name = name;
			for (int i = 0; i < group.channels.length; i++) {
				Inst inst = instruments[group.channels[i]];
				if (inst!=null) {
					r.instruments.add(inst.copy());
				}
			}
		}
		return r;
	}
	
	private Str unloadPatchNoLock(String name) {
		Str error = new Str();
		InstGroup group = getGroupNoLock(name);
		if (group==null) {
			error.sb().append("Patch not found: ");
			error.sb().append(name);
			logger.error(this, error);
		} else {
			ungroupInstrumentsNoLock(name);
			for (int i = 0; i < group.channels.length; i++) {
				instruments[group.channels[i]] = null;
			}
		}
		return error;
	}

	private Str addInstrumentToPatchNoLock(String name,Inst instrument) {
		Str error = new Str();
		InstGroup group = getGroupNoLock(name);
		if (group==null) {
			error.sb().append("Patch not found: ");
			error.sb().append(name);
			logger.error(this, error);
		} else {
			List<Integer> availableChannels = getAvailableInstrumentChannelsNoLock();
			if (availableChannels.size()==0) {
				error.sb().append("Not enough channels available to add instrument to patch: ");
				error.sb().append(name);
				logger.error(this, error);
			} else {
				ungroupInstrumentsNoLock(name);
				int[] channels = new int[group.channels.length+1];
				for (int i = 0; i < group.channels.length; i++) {
					channels[i] = group.channels[i];
				}
				int channel = availableChannels.remove(0);
				channels[channels.length-1] = channel;
				setInstrumentNoLock(channel, instrument);
				groupInstrumentsNoLock(name,channels);
			}
		}
		return error;
	}
	
	private Str removeInstrumentFromPatchNoLock(String name,int channel) {
		Str error = new Str();
		InstGroup group = getGroupNoLock(name);
		if (group==null) {
			error.sb().append("Patch not found: ");
			error.sb().append(name);
			logger.error(this, error);
		} else {
			ungroupInstrumentsNoLock(name);
			int[] channels = new int[group.channels.length-1];
			int s = 0;
			for (int i = 0; i < group.channels.length; i++) {
				if (group.channels[i]!=channel) {
					channels[s] = group.channels[i];
					s++;
				}
			}
			groupInstrumentsNoLock(name,channels);
			instruments[channel] = null;
		}
		return error;
	}
	
	private void setInstrumentNoLock(int channel,Inst instrument) {
		instrument = instrument.copy();
		if (channel>=0 && channel<16 && channel!=10) {
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
		
	private void groupInstrumentsNoLock(String groupName, int[] channels) {
		InstGroup ig = getGroupNoLock(groupName);
		if (ig==null) {
			boolean add = true;
			for (int i = 0; i < channels.length; i++) {
				if (channels[i]>=0 && channels[i]<16 && channels[i]!=10) {
					ig = getGroupNoLock(channels[i]);
					if (ig!=null) {
						Str error = new Str();
						error.sb().append("Group already exists with channel: ");
						error.sb().append(channels[i]);
						logger.error(this, error);
						add = false;
						break;
					}
				} else {
					Str error = new Str();
					error.sb().append("Invalid instrument channel: ");
					error.sb().append(channels[i]);
					logger.error(this, error);
					add = false;
					break;
				}
			}
			if (add) {
				InstGroup group = new InstGroup();
				group.name = groupName;
				group.channels = channels;
				groups.add(group);
			}
		} else {
			Str error = new Str();
			error.sb().append("Group already exists with name: ");
			error.sb().append(groupName);
			logger.error(this, error);
		}
	}
	
	private void ungroupInstrumentsNoLock(String groupName) {
		InstGroup ig = getGroupNoLock(groupName);
		if (ig!=null) {
			groups.remove(ig);
		}
	}
	
	private List<MidiNote> getNotesNoLock(String groupName,String... notes) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		InstGroup ig = getGroupNoLock(groupName);
		if (ig!=null) {
			for (int i = 0; i < ig.channels.length; i++) {
				int channel = ig.channels[i];
				Inst inst = instruments[channel];
				if (inst!=null) {
					List<MidiNote> mns = getNotesNoLock(channel,notes);
					for (MidiNote note: mns) {
						note.delaySteps = inst.patchDelaySteps;
						r.add(note);
					}
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
	
	private InstGroup getGroupNoLock(String groupName) {
		InstGroup r = null;
		for (InstGroup group: groups) {
			if (group.name.equals(groupName)) {
				r = group;
				break;
			}
		}
		return r;
	}
	
	private InstGroup getGroupNoLock(int channel) {
		InstGroup r = null;
		for (InstGroup group: groups) {
			for (int i = 0; i < group.channels.length; i++) {
				if (group.channels[i]==channel) {
					r = group;
					break;
				}
			}
		}
		return r;
	}
}
