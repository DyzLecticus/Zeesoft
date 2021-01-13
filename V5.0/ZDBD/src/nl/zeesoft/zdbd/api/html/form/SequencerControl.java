package nl.zeesoft.zdbd.api.html.form;

import java.util.List;

import nl.zeesoft.zdbd.midi.MixState;
import nl.zeesoft.zdbd.midi.SynthConfig;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdk.Str;

public class SequencerControl extends FormHtml {
	protected MixState	currentMix	= null;
	protected MixState	nextMix		= null;
	
	public SequencerControl(int beatsPerMinute, float shufflePercentage, List<String> names, String currentSequence, String nextSequence,
		boolean hold, boolean selectRandom, boolean selectTrainingSequence, boolean regenerateOnPlay,
		MixState currentMix, MixState nextMix
		) {
		if (!names.contains(currentSequence)) {
			names.add(0,currentSequence);
		}
		addProperty("beatsPerMinute", "Beats per minute", beatsPerMinute, FormProperty.NUMBER_INPUT);
		addProperty("shufflePercentage", "Shuffle", shufflePercentage, FormProperty.ANY_INPUT);
		addProperty("currentSequence", "Play sequence", names, FormProperty.SELECT, currentSequence);
		addProperty("nextSequence", "Next sequence", names, FormProperty.SELECT, nextSequence);
		addProperty("hold", "Hold", hold, FormProperty.CHECKBOX_INPUT);
		addProperty("selectRandom", "Randomize", selectRandom, FormProperty.CHECKBOX_INPUT);
		addProperty("selectTrainingSequence", "Training sequence", selectTrainingSequence, FormProperty.CHECKBOX_INPUT);
		addProperty("regenerateOnPlay", "Regenerate", regenerateOnPlay, FormProperty.CHECKBOX_INPUT);
		for (FormProperty property: properties) {
			property.onChange = "sequencer.propertyChange(this);";
		}
		this.currentMix = currentMix;
		this.nextMix = nextMix;
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,renderPlayStopButtons());
		append(r,super.render());
		append(r,renderMuteButtons(currentMix,true));
		append(r,renderMuteButtons(nextMix,false));
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
	
	public static Str renderMuteButtons(MixState state, boolean current) {
		Str r = new Str();
		String label = "Mute";
		if (!current) {
			label = "Next";
		}
		String idPrefix = label.toLowerCase();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\">");
		r.sb().append(label);
		r.sb().append("</label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,renderMuteChannelButton(idPrefix,SynthConfig.DRUM_CHANNEL,"Drums",state));
		append(r,renderMuteChannelButton(idPrefix,SynthConfig.BASS_CHANNEL_1,"Bass1",state));
		append(r,renderMuteChannelButton(idPrefix,SynthConfig.BASS_CHANNEL_2,"Bass2",state));
		append(r,"</div>");
		append(r,"</div>");
		
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\"></label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		int i = 0;
		for (PatternInstrument inst: InstrumentPattern.INSTRUMENTS) {
			if (i<state.muteDrums.length) {
				append(r,renderMuteDrumButton(idPrefix,inst.name(),state,i));
				i++;
			} else {
				break;
			}
		}
		append(r,"</div>");
		append(r,"</div>");
		return r;
	}
	
	public static Str renderMuteChannelButton(String idPrefix, int channel, String value, MixState state) {
		Str r = new Str();
		String id = idPrefix + "-channel-" + channel;
		r.sb().append("<input id=\"");
		r.sb().append(id);
		if (state.muteChannels[channel]) {
			r.sb().append("\" class=\"red");
		}
		r.sb().append("\" value=\"");
		r.sb().append(value);
		r.sb().append("\" type=\"button\" onclick=\"sequencer.toggleMute(this);\" />");
		return r;
	}
	
	public static Str renderMuteDrumButton(String idPrefix, String name, MixState state, int index) {
		Str r = new Str();
		String id = idPrefix + "-drum-" + name;
		String value = name.substring(0,1);
		if (value.equals("P")) {
			value = name.substring(name.length()-1);
		}
		r.sb().append("<input id=\"");
		r.sb().append(id);
		r.sb().append("\" value=\"");
		r.sb().append(value);
		if (state.muteDrums[index]) {
			r.sb().append("\" class=\"red");
		}
		r.sb().append("\" type=\"button\" onclick=\"sequencer.toggleMute(this);\" />");
		return r;
	}
}
