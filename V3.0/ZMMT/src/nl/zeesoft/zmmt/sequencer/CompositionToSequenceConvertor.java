package nl.zeesoft.zmmt.sequencer;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Note;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.synthesizer.EchoConfiguration;
import nl.zeesoft.zmmt.synthesizer.Instrument;
import nl.zeesoft.zmmt.synthesizer.InstrumentConfiguration;
import nl.zeesoft.zmmt.synthesizer.MidiNote;
import nl.zeesoft.zmmt.synthesizer.MidiNoteDelayed;

public class CompositionToSequenceConvertor {
	public static final int		TEMPO				= 0x51;
	public static final int		TEXT				= 0x01;
	public static final int		MARKER				= 0x06;
	
	public static final String	SEQUENCE_MARKER		= "SEQ:";
	public static final String	PATTERN_STEP_MARKER	= "PS:";
	
	private Messenger			messenger			= null;
	private Composition			composition			= null;
	
	public CompositionToSequenceConvertor(Composition composition) {
		this.composition = composition;
	}
	
	public CompositionToSequenceConvertor(Messenger messenger, Composition composition) {
		this.messenger = messenger;
		this.composition = composition;
	}

	public Sequence getPatternSequence(int patternNumber) {
		Sequence r = createSequence();
		createEventOnTrack(r.getTracks()[0],ShortMessage.NOTE_OFF,0,0,0,0);
		String seq = SEQUENCE_MARKER + "-1";
		byte[] data = seq.getBytes();
		createMetaEventOnTrack(r.getTracks()[0],MARKER,data,data.length,0);
		int nextTick = addPatternToSequence(r,0,patternNumber,true,true);
		// Align track endings
		for (int t = 0; t < Composition.TRACKS; t++) {
			createEventOnTrack(r.getTracks()[t],ShortMessage.NOTE_OFF,0,0,0,(nextTick - 1));
		}
		return r;
	}

	public int getPatternSequenceEndTick(int patternNumber) {
		int r = 0;
		Pattern p = composition.getPattern(patternNumber);
		if (p!=null) {
			r = ((composition.getStepsForPattern(p) * composition.getTicksPerStep()) - 1);
		}
		return r;
	}

	public Sequence getSequence(boolean addMarkers) {
		Sequence r = createSequence();
		createEventOnTrack(r.getTracks()[0],ShortMessage.NOTE_OFF,0,0,0,0);
		int nextTick = 0;
		int i = 0;
		if (composition.getSequence().size()>0) {
			for (Integer patternNumber: composition.getSequence()) {
				String seq = SEQUENCE_MARKER + i;
				byte[] data = seq.getBytes();
				createMetaEventOnTrack(r.getTracks()[0],MARKER,data,data.length,nextTick);
				nextTick = addPatternToSequence(r,nextTick,patternNumber,addMarkers,false);
				i++;
			}
		} else {
			String seq = SEQUENCE_MARKER + "-1";
			byte[] data = seq.getBytes();
			createMetaEventOnTrack(r.getTracks()[0],MARKER,data,data.length,0);
			String ps = PATTERN_STEP_MARKER + "-1:-1";
			data = ps.getBytes();
			createMetaEventOnTrack(r.getTracks()[0],MARKER,data,data.length,0);
			nextTick = nextTick + Composition.RESOLUTION;
		}
		// Align track endings
		for (int t = 0; t < Composition.TRACKS; t++) {
			createEventOnTrack(r.getTracks()[t],ShortMessage.NOTE_OFF,0,0,0,(nextTick - 1));
		}
		return r;
	}

	public int getSequenceEndTick() {
		int r = 0;
		for (Integer patternNumber: composition.getSequence()) {
			Pattern p = composition.getPattern(patternNumber);
			r = r + (composition.getStepsForPattern(p) * composition.getTicksPerStep());
		}
		if (r>0) {
			r = r - 1;
		}
		return r;
	}

	protected int addPatternToSequence(Sequence seq,int startTick,int patternNumber,boolean addMarkers,boolean wrapEcho) {
		int nextPatternStartTick = startTick;
		Pattern p = composition.getPattern(patternNumber);
		if (p!=null) {

			int patternSteps = composition.getStepsForPattern(p);
			int ticksPerStep = composition.getTicksPerStep();
			nextPatternStartTick = nextPatternStartTick + (patternSteps * ticksPerStep);

			EchoConfiguration echo = composition.getSynthesizerConfiguration().getEcho();

			for (int t = 1; t<=Composition.TRACKS; t++) {
				Track track = seq.getTracks()[(t - 1)];

				int currentTick = startTick;

				List<Note> echoNotes = new ArrayList<Note>();

				for (int s = 1; s<=patternSteps; s++) {
					if (t==1 && addMarkers) {
						String ps = PATTERN_STEP_MARKER + patternNumber + ":" + s;
						byte[] data = ps.getBytes();
						createMetaEventOnTrack(track,MARKER,data,data.length,currentTick);
					}
					for (Note note: p.getNotes()) {
						if (note.track==t && note.step==s) {
							if (note.instrument.equals(echo.getInstrument())) {
								echoNotes.add(note);
							}
							List<MidiNote> midiNotes = composition.getSynthesizerConfiguration().getMidiNotesForNote(note.instrument,note.note,note.accent,0);
							int endTick = currentTick + ((note.duration * ticksPerStep) - 1);
							for (MidiNote mn: midiNotes) {
								int velocity = (mn.velocity * note.velocityPercentage) / 100;
								createEventOnTrack(track,ShortMessage.NOTE_ON,mn.channel,mn.midiNote,velocity,currentTick);
								createEventOnTrack(track,ShortMessage.NOTE_OFF,mn.channel,mn.midiNote,0,endTick);
							}
						}
					}
					currentTick = currentTick + ticksPerStep;
				}

				// Echo
				for (Note note: echoNotes) {
					List<MidiNote> midiNotes = composition.getSynthesizerConfiguration().getMidiNotesForNote(note.instrument,note.note,note.accent,1);
					for (MidiNote mn: midiNotes) {
						if (mn instanceof MidiNoteDelayed) {
							MidiNoteDelayed mnd = (MidiNoteDelayed) mn;
							currentTick = startTick + ((note.step - 1) + mnd.delaySteps) * ticksPerStep;
							int endTick = startTick + (((note.step - 1) + mnd.delaySteps + note.duration) * ticksPerStep) - 1;
							if (wrapEcho) {
								if (currentTick>=nextPatternStartTick) {
									currentTick = currentTick - (patternSteps * ticksPerStep);
								}
								if (endTick>=nextPatternStartTick) {
									endTick = endTick - (patternSteps * ticksPerStep);
								}
							}
							int velocity = (mn.velocity * note.velocityPercentage) / 100;
							createEventOnTrack(track,ShortMessage.NOTE_ON,mn.channel,mn.midiNote,velocity,currentTick);
							createEventOnTrack(track,ShortMessage.NOTE_OFF,mn.channel,mn.midiNote,0,endTick);
						}
					}
				}
			}
			
		}
		return nextPatternStartTick;
	}
	
	protected Sequence createSequence() {
		Sequence r = null;
		try {
			r = new Sequence(Sequence.PPQ,Composition.RESOLUTION,Composition.TRACKS);
		} catch (InvalidMidiDataException e) {
			if (messenger!=null) {
				messenger.error(this,"Invalid MIDI data",e);
			} else {
				e.printStackTrace();
			}
		}
		if (r!=null) {
			addCompositionInfoToSequenceTick(r,0);
			addSynthesizerConfigurationToSequenceTick(r,0);
		}
		return r;
	}
	
	protected void addCompositionInfoToSequenceTick(Sequence seq,int tick) {
		Track track = seq.getTracks()[0];
		int tempo = (60000000 / composition.getBeatsPerMinute());
		byte[] b = new byte[3];
		int tmp = tempo >> 16;
		b[0] = (byte) tmp;
		tmp = tempo >> 8;
		b[1] = (byte) tmp;
		b[2] = (byte) tempo;
		createMetaEventOnTrack(track,TEMPO,b,b.length,tick);

		String txt = "Name: " + composition.getName();
		byte[] tb = txt.getBytes();
		createMetaEventOnTrack(track,TEXT,tb,tb.length,tick);
		
		txt = "Composer: " + composition.getComposer();
		tb = txt.getBytes();
		createMetaEventOnTrack(track,TEXT,tb,tb.length,tick);
	}

	protected void addSynthesizerConfigurationToSequenceTick(Sequence seq,int tick) {
		Track track = seq.getTracks()[0];
		EchoConfiguration echo = composition.getSynthesizerConfiguration().getEcho();
		InstrumentConfiguration echoInst = composition.getSynthesizerConfiguration().getInstrument(echo.getInstrument());
		if (echoInst!=null) {
			int layerMidiNum = echoInst.getLayer1MidiNum();
			int layerPressure = echoInst.getLayer1Pressure();
			if (echo.getLayer()==2) {
				layerMidiNum = echoInst.getLayer2MidiNum();
				layerPressure = echoInst.getLayer2Pressure();
			}
			if (layerMidiNum>=0) {
				for (int e = 0; e < 3; e++) {
					int channel = Instrument.getMidiChannelForInstrument(Instrument.ECHO,e);
					createEventOnTrack(track,ShortMessage.PROGRAM_CHANGE,channel,layerMidiNum,0,tick);
					createEventOnTrack(track,ShortMessage.CHANNEL_PRESSURE,channel,layerPressure,0,tick);
					if (e==0) {
						createEventOnTrack(track,ShortMessage.CONTROL_CHANGE,channel,91,echo.getReverb1(),tick);
					} else if (e==1) {
						createEventOnTrack(track,ShortMessage.CONTROL_CHANGE,channel,91,echo.getReverb2(),tick);
					} else if (e==2) {
						createEventOnTrack(track,ShortMessage.CONTROL_CHANGE,channel,91,echo.getReverb3(),tick);
					}
				}
			}
		}
		for (InstrumentConfiguration inst: composition.getSynthesizerConfiguration().getInstruments()) {
			if (!inst.getName().equals(Instrument.ECHO)) {
				int channel = Instrument.getMidiChannelForInstrument(inst.getName(),0);
				createEventOnTrack(track,ShortMessage.PROGRAM_CHANGE,channel,inst.getLayer1MidiNum(),0,tick);
				createEventOnTrack(track,ShortMessage.CHANNEL_PRESSURE,channel,inst.getLayer1Pressure(),0,tick);
				createEventOnTrack(track,ShortMessage.CONTROL_CHANGE,channel,91,inst.getLayer1Reverb(),tick);
				if (inst.getLayer2MidiNum()>=0) {
					channel = Instrument.getMidiChannelForInstrument(inst.getName(),1);
					createEventOnTrack(track,ShortMessage.PROGRAM_CHANGE,channel,inst.getLayer2MidiNum(),0,tick);
					createEventOnTrack(track,ShortMessage.CHANNEL_PRESSURE,channel,inst.getLayer2Pressure(),0,tick);
					createEventOnTrack(track,ShortMessage.CONTROL_CHANGE,channel,91,inst.getLayer2Reverb(),tick);
				}
			}
		}
	}
	
	protected void createEventOnTrack(Track track, int type, int channel, int num, int val, long tick) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(type,channel,num,val); 
			MidiEvent event = new MidiEvent(message,tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			if (messenger!=null) {
				messenger.error(this,"Invalid MIDI data",e);
			} else {
				e.printStackTrace();
			}
		}
	}
	
	protected void createMetaEventOnTrack(Track track, int type, byte[] data, int length, long tick) {
		MetaMessage message = new MetaMessage();
		try {
			message.setMessage(type,data,length);
			MidiEvent event = new MidiEvent(message,tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			if (messenger!=null) {
				messenger.error(this,"Invalid MIDI data",e);
			} else {
				e.printStackTrace();
			}
		}
	}
}
