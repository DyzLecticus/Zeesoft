package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.midi.convertors.InstrumentConvertors;

public class SoundPatchFactory {
	public static String	DEFAULT		= "Default";
	public static String	TRANCE		= "Trance";
	
	public static List<String> listSoundPatches() {
		List<String> r = new ArrayList<String>();
		r.add(DEFAULT);
		r.add(TRANCE);
		return r;
	}
	
	public static SoundPatch getNewSoundPatch(String name) {
		SoundPatch r = null;
		if (name.equals(DEFAULT)) {
			r = new SoundPatch();
		} else if (name.equals(TRANCE)) {
			r = new SoundPatch();
			r.synthConfig.setChannelInstrument(SynthConfig.BASS_CHANNEL_2, 85);
			r.synthConfig.setChannelInstrument(SynthConfig.STAB_CHANNEL, 83);
			r.synthConfig.setChannelInstrument(SynthConfig.ARP_CHANNEL_1, 81);
			r.synthConfig.setChannelInstrument(SynthConfig.ARP_CHANNEL_2, 84);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.KICK, 0, "midiNote", 39);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.SNARE, 0, "midiNote", 52);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.SNARE, 0, "velocity", 110);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.SNARE, 0, "accentVelocity", 120);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CLOSED_HIHAT, 0, "midiNote", 46);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.OPEN_HIHAT, 0, "midiNote", 47);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.RIDE, 0, "midiNote", 68);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.RIDE, 0, "velocity", 60);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.RIDE, 0, "accentVelocity", 70);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CRASH, 0, "midiNote", 71);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CRASH, 0, "velocity", 80);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.CRASH, 0, "accentVelocity", 90);
			
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION1, 0, "velocity", 80);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION1, 0, "accentVelocity", 90);
			
			// Clap
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "midiNote", 75);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "velocity", 90);
			r.convertor.setConvertorLayerProperty(InstrumentConvertors.PERCUSSION2, 0, "accentVelocity", 100);
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
		}
		return r;
	}
}
