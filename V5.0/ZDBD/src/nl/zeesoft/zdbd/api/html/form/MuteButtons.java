package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.midi.MixState;
import nl.zeesoft.zdbd.midi.SynthConfig;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdk.Str;

public class MuteButtons extends FormProperty {
	private MixState	state	= null;
	private boolean		current	= false;
	
	public MuteButtons(MixState state, boolean current) {
		this.state = state;
		this.current = current;
	}
	
	@Override
	public Str render() {
		return renderMuteButtons(state,current);
	}
	
	public static Str renderMuteButtons(MixState state, boolean current) {
		Str r = new Str();
		String label = "Mute";
		if (!current) {
			label += " next";
		}
		String idPrefix = label.toLowerCase();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\">");
		r.sb().append(label);
		r.sb().append("</label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,renderMuteChannelButton(idPrefix,SynthConfig.DRUM_CHANNEL,"DR",state));
		append(r,renderMuteChannelButton(idPrefix,SynthConfig.BASS_CHANNEL_1,"B1",state));
		append(r,renderMuteChannelButton(idPrefix,SynthConfig.BASS_CHANNEL_2,"B2",state));
		append(r,renderMuteChannelButton(idPrefix,SynthConfig.STAB_CHANNEL,"ST",state));
		append(r,renderMuteChannelButton(idPrefix,SynthConfig.ARP_CHANNEL_1,"A1",state));
		append(r,renderMuteChannelButton(idPrefix,SynthConfig.ARP_CHANNEL_2,"A2",state));
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
