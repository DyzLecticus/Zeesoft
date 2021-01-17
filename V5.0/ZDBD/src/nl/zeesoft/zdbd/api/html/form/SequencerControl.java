package nl.zeesoft.zdbd.api.html.form;

import java.util.List;

import nl.zeesoft.zdbd.midi.MixState;
import nl.zeesoft.zdk.Str;

public class SequencerControl extends FormHtml {
	protected boolean	recording		= false;
	protected long		recordedTicks	= 0;
	
	public SequencerControl(int beatsPerMinute, float shufflePercentage, boolean recording, long recordedTicks,
		List<String> names, String currentSequence, String nextSequence,
		boolean hold, boolean selectRandom, boolean selectTrainingSequence, boolean regenerateOnPlay,
		MixState currentMix, MixState nextMix
		) {
		if (!names.contains(currentSequence)) {
			names.add(0,currentSequence);
		}
		addProperty("beatsPerMinute", "Beats per minute", beatsPerMinute, FormProperty.NUMBER_INPUT);
		addProperty("shufflePercentage", "Shuffle", shufflePercentage, FormProperty.ANY_INPUT);
		addProperty("currentSequence", "Play sequence", names, FormProperty.SELECT, currentSequence);
		properties.add(new MuteButtons(currentMix,true));
		addProperty("nextSequence", "Next sequence", names, FormProperty.SELECT, nextSequence);
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
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,renderPlayStopButtons());
		append(r,super.render());
		append(r,renderRecorder(recording,recordedTicks));
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
	
	public static Str renderRecorder(boolean recording, long recordedTicks) {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\">Record</label>");
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
		return r;
	}
}
