package nl.zeesoft.zdbd.api.html.form;

import java.util.List;

import nl.zeesoft.zdk.Str;

public class SequencerControl extends FormHtml{	
	public SequencerControl(int beatsPerMinute, List<String> names, String currentSequence, String nextSequence,
		boolean hold, boolean selectRandom, boolean selectTrainingSequence, boolean regenerateOnPlay) {
		if (!names.contains(currentSequence)) {
			names.add(currentSequence);
		}
		addProperty("beatsPerMinute", "Beats per minute", beatsPerMinute, FormProperty.NUMBER_INPUT);
		addProperty("currentSequence", "Play sequence", names, FormProperty.SELECT, currentSequence);
		addProperty("nextSequence", "Next sequence", names, FormProperty.SELECT, nextSequence);
		addProperty("hold", "Hold", hold, FormProperty.CHECKBOX_INPUT);
		addProperty("selectRandom", "Randomize", selectRandom, FormProperty.CHECKBOX_INPUT);
		addProperty("selectTrainingSequence", "Training sequence", selectTrainingSequence, FormProperty.CHECKBOX_INPUT);
		addProperty("regenerateOnPlay", "Regenerate", regenerateOnPlay, FormProperty.CHECKBOX_INPUT);
		for (FormProperty property: properties) {
			property.onChange = "sequencer.propertyChange(this);";
		}
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,renderPlayStopButtons());
		append(r,super.render());
		return r;
	}
	
	public static Str renderPlayStopButtons() {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<input type=\"button\" value=\"Play sequence\" onclick=\"sequencer.playSequence();\" />");
		append(r,"<input type=\"button\" value=\"Play theme\" onclick=\"sequencer.playTheme();\" />");
		append(r,"<input type=\"button\" value=\"Stop\" onclick=\"sequencer.stop();\" />");
		append(r,"</div>");
		append(r,"</div>");
		return r;
	}
}
