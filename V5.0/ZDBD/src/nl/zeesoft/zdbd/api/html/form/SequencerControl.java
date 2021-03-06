package nl.zeesoft.zdbd.api.html.form;

import java.util.List;

import nl.zeesoft.zdbd.api.html.FormHtml;
import nl.zeesoft.zdbd.api.html.FormProperty;
import nl.zeesoft.zdbd.midi.MixState;
import nl.zeesoft.zdbd.midi.VolumeControl;
import nl.zeesoft.zdk.Str;

public class SequencerControl extends FormHtml {
	protected boolean	recording		= false;
	protected long		recordedTicks	= 0;
	
	protected String	midiRecording	= "";
	protected String	audioRecording	= "";
	
	public SequencerControl(int beatsPerMinute, float shufflePercentage,
		List<String> names, String currentSequence, String nextSequence,
		boolean hold, boolean selectRandom, boolean selectTrainingSequence, boolean regenerateOnPlay,
		MixState currentMix, MixState nextMix,
		VolumeControl volumeControl,
		List<String> arpeggiators, String nextArpeggiator,
		boolean recording, long recordedTicks,
		String midiRecording, String audioRecording
		) {
		if (!names.contains(currentSequence)) {
			names.add(0,currentSequence);
		}
		addProperty("beatsPerMinute", "Beats per minute", beatsPerMinute, FormProperty.NUMBER_INPUT);
		addProperty("shufflePercentage", "Shuffle", shufflePercentage, FormProperty.ANY_INPUT);
		addProperty("currentSequence", "Play sequence", names, FormProperty.SELECT, currentSequence);
		properties.add(new MuteButtons(currentMix,true));
		
		int[] range = {0,100};
		addProperty("volumeGlobal", "Global volume", (int)(volumeControl.getGlobal() * 100), FormProperty.RANGE_INPUT, range);
		addProperty("volumeDrums", "Drum volume", (int)(volumeControl.getDrums() * 100), FormProperty.RANGE_INPUT, range);
		addProperty("volumeBass", "Bass volume", (int)(volumeControl.getBass() * 100), FormProperty.RANGE_INPUT, range);
		addProperty("volumeStab", "Stab volume", (int)(volumeControl.getStab() * 100), FormProperty.RANGE_INPUT, range);
		addProperty("volumeArpeggiator", "Arpeggiator volume", (int)(volumeControl.getArpeggiator() * 100), FormProperty.RANGE_INPUT, range);
		
		addProperty("nextSequence", "Next sequence", names, FormProperty.SELECT, nextSequence);
		addProperty("nextArpeggiator", "Next arpeggiator", arpeggiators, FormProperty.SELECT, nextArpeggiator);
		properties.add(new MuteButtons(nextMix,false));
		addProperty("hold", "Hold", hold, FormProperty.CHECKBOX_INPUT);
		addProperty("selectRandom", "Randomize", selectRandom, FormProperty.CHECKBOX_INPUT);
		addProperty("selectTrainingSequence", "Training sequence", selectTrainingSequence, FormProperty.CHECKBOX_INPUT);
		addProperty("regenerateOnPlay", "Regenerate", regenerateOnPlay, FormProperty.CHECKBOX_INPUT);
		for (FormProperty property: properties) {
			property.onChange = "sequencer.propertyChange(this);";
		}
		this.recording = recording;
		this.recordedTicks = recordedTicks;
		this.midiRecording = midiRecording;
		this.audioRecording = audioRecording;
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,renderPlayStopButtons());
		append(r,super.render());
		append(r,renderRecorder(recording,recordedTicks,midiRecording,audioRecording));
		return r;
	}
	
	public static Str renderPlayStopButtons() {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\">Sequencer</label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<input type=\"button\" value=\"Start\" onclick=\"sequencer.startTheme();\" />");
		append(r,"<input type=\"button\" value=\"Stop\" onclick=\"sequencer.stop();\" />");
		append(r,"</div>");
		append(r,"</div>");
		return r;
	}
	
	public static Str renderRecorder(boolean recording, long recordedTicks, String midiRecording, String audioRecording) {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\">Recorder</label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<input type=\"button\" id=\"startRecording\" value=\"Start\" onclick=\"sequencer.startRecording();\"");
		if (recording) {
			r.sb().append(" DISABLED");
		}
		r.sb().append(" />");
		append(r,"<input type=\"button\" id=\"stopRecording\" value=\"Stop\" onclick=\"sequencer.stopRecording();\"");
		if (!recording) {
			r.sb().append(" DISABLED");
		}
		r.sb().append(" />");
		append(r,"<label id=\"recordedTicks\">");
		r.sb().append(recordedTicks);
		r.sb().append("</label>");
		append(r,"</div>");
		append(r,"</div>");
		
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\">Export</label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<input type=\"button\" id=\"exportMidi\" value=\"MIDI\" onclick=\"sequencer.exportMidi();\"");
		if (recordedTicks==0) {
			r.sb().append(" DISABLED");
		}
		r.sb().append(" />");
		append(r,"<input type=\"button\" id=\"exportAudio\" value=\"Audio\" onclick=\"sequencer.exportAudio();\"");
		if (recordedTicks==0) {
			r.sb().append(" DISABLED");
		}
		r.sb().append(" />");
		append(r,"</div>");
		append(r,"</div>");

		append(r,renderRecording(midiRecording));
		append(r,renderRecording(audioRecording));
		return r;
	}
	
	public static Str renderRecording(String recording) {
		Str r = new Str();
		if (recording.length()>0) {
			append(r,"<div class=\"row\">");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<label class=\"column-label\"></label>");
			append(r,"</div>");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<a href=\"Recordings/");
			r.sb().append(recording);
			r.sb().append("\" />");
			r.sb().append(recording);
			r.sb().append("</a>");
			append(r,"</div>");
			append(r,"</div>");
		}
		return r;
	}
}
