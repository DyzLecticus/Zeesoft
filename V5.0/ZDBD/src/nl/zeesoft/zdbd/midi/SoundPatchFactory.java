package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.midi.convertors.InstrumentConvertors;

public class SoundPatchFactory {
	public static String	BREAKS		= "Breaks";
	public static String	TRANCE		= "Trance";
	public static String	PSY			= "Psy";
	
	public static List<String> listSoundPatches() {
		List<String> r = new ArrayList<String>();
		r.add(BREAKS);
		r.add(TRANCE);
		r.add(PSY);
		return r;
	}
	
	public static SoundPatch getNewSoundPatch(String name) {
		SoundPatch r = null;
		if (name.equals(BREAKS)) {
			r = new SoundPatch();
		} else if (name.equals(TRANCE)) {
			r = new SoundPatch();
			r.synthConfig.setChannelInstrument(SynthConfig.BASS_CHANNEL_2, 85);
			r.synthConfig.setChannelInstrument(SynthConfig.STAB_CHANNEL, 83);
			r.synthConfig.setChannelInstrument(SynthConfig.ARP_CHANNEL_1, 81);
			r.synthConfig.setChannelInstrument(SynthConfig.ARP_CHANNEL_2, 84);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.KICK, 0, "midiNote", 39);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.KICK, 0, "velocity", 100);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.KICK, 0, "accentVelocity", 110);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.SNARE, 0, "midiNote", 52);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.SNARE, 0, "velocity", 110);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.SNARE, 0, "accentVelocity", 120);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CLOSED_HIHAT, 0, "midiNote", 46);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CLOSED_HIHAT, 0, "velocity", 80);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CLOSED_HIHAT, 0, "accentVelocity", 90);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.OPEN_HIHAT, 0, "midiNote", 47);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.OPEN_HIHAT, 0, "velocity", 90);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.OPEN_HIHAT, 0, "accentVelocity", 100);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.RIDE, 0, "midiNote", 68);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.RIDE, 0, "velocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.RIDE, 0, "accentVelocity", 80);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CRASH, 0, "midiNote", 71);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CRASH, 0, "velocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CRASH, 0, "accentVelocity", 80);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION1, 0, "velocity", 80);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION1, 0, "accentVelocity", 90);
			
			// Clap
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "midiNote", 75);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "velocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "accentVelocity", 80);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "hold", 0.3F);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "accentHold", 0.5F);
			
			// Bass layer 2
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.BASS, 1, "velocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.BASS, 1, "accentVelocity", 80);

			// Arpeggiators
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.ARPEGGIATOR, 0, "velocity", 60);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.ARPEGGIATOR, 0, "accentVelocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.ARPEGGIATOR, 1, "velocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.ARPEGGIATOR, 1, "accentVelocity", 80);
		} else if (name.equals(PSY)) {
			r = new SoundPatch();
			r.synthConfig.setChannelInstrument(SynthConfig.BASS_CHANNEL_2, 84);
			r.synthConfig.setChannelInstrument(SynthConfig.STAB_CHANNEL, 83);
			r.synthConfig.setChannelInstrument(SynthConfig.ARP_CHANNEL_1, 81);
			r.synthConfig.setChannelInstrument(SynthConfig.ARP_CHANNEL_2, 85);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.KICK, 0, "midiNote", 42);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.KICK, 0, "velocity", 110);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.KICK, 0, "accentVelocity", 120);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.SNARE, 0, "midiNote", 54);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.SNARE, 0, "velocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.SNARE, 0, "accentVelocity", 80);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.SNARE, 0, "hold", 0.5F);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.SNARE, 0, "accentHold", 0.75F);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CLOSED_HIHAT, 0, "midiNote", 46);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CLOSED_HIHAT, 0, "velocity", 60);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CLOSED_HIHAT, 0, "accentVelocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CLOSED_HIHAT, 0, "hold", 0.15F);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CLOSED_HIHAT, 0, "accentHold", 0.2F);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.OPEN_HIHAT, 0, "midiNote", 47);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.OPEN_HIHAT, 0, "velocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.OPEN_HIHAT, 0, "accentVelocity", 80);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.OPEN_HIHAT, 0, "hold", 0.4F);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.OPEN_HIHAT, 0, "accentHold", 0.6F);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.RIDE, 0, "midiNote", 69);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.RIDE, 0, "velocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.RIDE, 0, "accentVelocity", 80);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.RIDE, 0, "hold", 1.9F);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.RIDE, 0, "accentHold", 2.9F);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CRASH, 0, "midiNote", 71);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CRASH, 0, "velocity", 60);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CRASH, 0, "accentVelocity", 70);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "midiNote", 75);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION1, 0, "velocity", 80);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION1, 0, "accentVelocity", 90);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION1, 0, "midiNote", 76);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION1, 0, "velocity", 60);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION1, 0, "accentVelocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION1, 0, "hold", 0.15F);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION1, 0, "accentHold", 0.3F);
			
			// Clap
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "midiNote", 75);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "velocity", 60);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "accentVelocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "hold", 0.3F);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "accentHold", 0.5F);
			
			// Bass layer 2
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.BASS, 1, "baseOctave", 3);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.BASS, 1, "velocity", 50);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.BASS, 1, "accentVelocity", 60);
			
			// Stab
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.STAB, 1, "velocity", 30);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.STAB, 1, "accentVelocity", 40);

			// Arpeggiators
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.ARPEGGIATOR, 0, "velocity", 60);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.ARPEGGIATOR, 0, "accentVelocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.ARPEGGIATOR, 0, "filter", 48);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.ARPEGGIATOR, 1, "velocity", 70);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.ARPEGGIATOR, 1, "accentVelocity", 80);
			
			r.synthConfig.setLFOProperty(0, "cycleSteps", 40);
			
			r.synthConfig.setLFOProperty(2, "cycleSteps", 48);
			r.synthConfig.setLFOProperty(3, "cycleSteps", 56);
			r.synthConfig.setLFOProperty(3, "change", -0.2F);
			
			r.synthConfig.setLFOProperty(6, "active", true);
			r.synthConfig.setLFOProperty(6, "channel", SynthConfig.STAB_CHANNEL);
			r.synthConfig.setLFOProperty(6, "cycleSteps", 24);
		}
		return r;
	}
}
