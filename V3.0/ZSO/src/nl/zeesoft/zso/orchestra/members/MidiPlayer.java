package nl.zeesoft.zso.orchestra.members;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.members.Player;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWork;
import nl.zeesoft.zso.orchestra.SampleOrchestra;

public class MidiPlayer extends Player {
	private String					error			= "";
	private Synthesizer				synth			= null;
	private MidiChannel				bassChannel		= null;		
	private MidiChannel				pianoChannel	= null;		
	private MidiChannel				harpChannel		= null;		
	private MidiChannel				stringsChannel	= null;		
	
	private List<MidiPlayerWorker>	workers			= new ArrayList<MidiPlayerWorker>();
	
	public MidiPlayer(Messenger msgr, Orchestra orch, String positionName, int positionBackupNumber) {
		super(msgr, orch, positionName, positionBackupNumber);

		Instrument[] instruments = null;
		int bassNum = -1;
		int pianoNum = -1;
		int harpNum = -1;
		int stringsNum = -1;
		
		try {
			synth = MidiSystem.getSynthesizer();
		} catch (MidiUnavailableException e) {
			msgr.error(this,"Failed to obtain a synthesizer",e);
		}
		if (synth!=null) {
			try {
				synth.open();
			} catch (MidiUnavailableException e) {
				synth = null;
				msgr.error(this,"Failed to open synthesizer",e);
			}
		}
		if (synth!=null) {
			instruments = synth.getLoadedInstruments();
			for (int i = 0; i < instruments.length; i++) {
				Instrument in = instruments[i];
				if (in.getName().toUpperCase().startsWith("ACOUSTIC BS")) {
					bassNum = i;
				} else if (bassNum<0 && in.getName().toUpperCase().contains("BASS")) {
					bassNum = i;
				} else if (in.getName().toUpperCase().startsWith("PIANO")) {
					pianoNum = i;
				} else if (pianoNum<0 && in.getName().toUpperCase().contains("PIANO")) {
					pianoNum = i;
				} else if (in.getName().toUpperCase().startsWith("HARP")) {
					harpNum = i;
				} else if (harpNum<0 && in.getName().toUpperCase().contains("HARP")) {
					harpNum = i;
				} else if (in.getName().toUpperCase().startsWith("STRINGS")) {
					stringsNum = i;
				} else if (stringsNum<0 && in.getName().toUpperCase().contains("STRINGS")) {
					stringsNum = i;
				}
			}
			if (bassNum>=0) {
				bassChannel = synth.getChannels()[4];
				bassChannel.programChange(0,bassNum);
				System.out.println(this + ": Bass: " + synth.getLoadedInstruments()[bassNum].getName());
			}
			if (pianoNum>=0) {
				pianoChannel = synth.getChannels()[5];
				//pianoChannel.programChange(0,pianoNum);
				System.out.println(this + ": Piano: " + synth.getLoadedInstruments()[pianoNum].getName());
				//testChannel(5);
			}
			if (harpNum>=0) {
				harpChannel = synth.getChannels()[6];
				harpChannel.programChange(0,harpNum);
			}
			if (stringsNum>=0) {
				stringsChannel = synth.getChannels()[7];
				stringsChannel.programChange(0,stringsNum);
			}
		} else {
			error = "Failed to open synthesizer";
		}
	}

	@Override
	public void stop(Worker ignoreWorker) {
		lockMe(this);
		for (MidiPlayerWorker worker: workers) {
			worker.stop();
		}
		workers.clear();
		unlockMe(this);
		super.stop(ignoreWorker);
		if (synth!=null && synth.isOpen()) {
			synth.close();
		}
	}

	@Override
	protected ProtocolWork getNewWorkProtocol() {
		return new MidiPlayerProtocol();
	}

	public String playInstrumentNotes(String instrument, List<Integer> notes, int velocity,long durationMs) {
		MidiChannel chan = getMidiChannelForInstrument(instrument);
		if (chan!=null) {
			System.out.println(this + ": Play " + instrument + ", notes: " + notes.size());
			MidiPlayerWorker worker = new MidiPlayerWorker(getMessenger(),getUnion(),this);
			lockMe(this);
			workers.add(worker);
			unlockMe(this);
			worker.play(instrument, notes, velocity, durationMs);
		} else {
			System.out.println(this + ": no channel to play " + instrument + ", notes: " + notes.size());
			System.err.println(this + ": no channel to play " + instrument + ", notes: " + notes.size());
		}
    	return error;
    }

	protected void playInstrumentNotes(String instrument, List<Integer> notes, int velocity) {
		MidiChannel chan = getMidiChannelForInstrument(instrument);
		if (chan!=null) {
			for (Integer note: notes) {
				System.out.println(this + ": Play " + instrument + ", note: " + note + ", channel: " + chan.getProgram());
				chan.noteOn(note,velocity);
			}
		}
	}

	protected void stopInstrumentNotes(String instrument, List<Integer> notes) {
		MidiChannel chan = getMidiChannelForInstrument(instrument);
		if (chan!=null) {
			for (Integer note: notes) {
				chan.noteOff(note);
			}
		}
	}
    
	protected MidiChannel getMidiChannelForInstrument(String instrument) {
		MidiChannel r = null;
		if (instrument.equals(SampleOrchestra.SYNTH_BASS)) {
			r = bassChannel;
		} else if (instrument.equals(SampleOrchestra.SYNTH_PIANO)) {
			r = pianoChannel;
		} else if (instrument.equals(SampleOrchestra.SYNTH_HARP)) {
			r = harpChannel;
		} else if (instrument.equals(SampleOrchestra.SYNTH_STRINGS)) {
			r = stringsChannel;
		}
		return r;
	}
	
	protected void removeWorker(MidiPlayerWorker worker) {
		lockMe(this);
		workers.remove(worker);
		unlockMe(this);
	}
	
	protected void testChannel(int channel) {
		synth.getChannels()[channel].noteOn(60, 100);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synth.getChannels()[channel].noteOff(60);

		synth.getChannels()[channel].noteOn(48, 100);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synth.getChannels()[channel].noteOff(48);

		synth.getChannels()[channel].noteOn(36, 100);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synth.getChannels()[channel].noteOff(36);

		synth.getChannels()[channel].noteOn(24, 100);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synth.getChannels()[channel].noteOff(24);

		synth.getChannels()[channel].noteOn(12, 100);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synth.getChannels()[channel].noteOff(12);
	}
}
