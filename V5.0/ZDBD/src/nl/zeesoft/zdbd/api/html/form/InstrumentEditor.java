package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.midi.SynthChannelConfig;
import nl.zeesoft.zdk.Str;

public class InstrumentEditor extends FormHtml {
	protected String	prevName	= "";
	protected String	nextName	= "";
	
	public InstrumentEditor(String name, SynthChannelConfig layer1, SynthChannelConfig layer2, String prevName, String nextName) {
		cancelLabel = "Done";
		onCancelClick = "soundpatch.editDone();";
		
		addProperty("instrumentName", "Instrument", name, FormProperty.TEXT);
		addSynthChannelConfigProperties(layer1,0);
		if (layer2!=null) {
			addSynthChannelConfigProperties(layer2,1);
		}
		
		for (FormProperty property: properties) {
			property.onChange = "soundpatch.propertyChange(this);";
		}
		
		this.prevName = prevName;
		this.nextName = nextName;
	}

	@Override
	public Str render() {
		Str r = new Str();
		append(r,renderPrevNextButtons(prevName,nextName));
		append(r,super.render());
		return r;
	}
	
	public static Str renderPrevNextButtons(String prevName, String nextName) {
		Str r = new Str();
		if (prevName.length()>0 || nextName.length()>0) {
			append(r,"<div class=\"row\">");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"<<\" onclick=\"soundpatch.edit('");
			r.sb().append(prevName);
			r.sb().append("');\"/>");
			append(r,"<input type=\"button\" value=\">>\" onclick=\"soundpatch.edit('");
			r.sb().append(nextName);
			r.sb().append("');\"/>");
			append(r,"</div>");
			append(r,"</div>");
		}
		return r;
	}
	
	protected void addSynthChannelConfigProperties(SynthChannelConfig layer, int num) {
		String suffix = "-" + num;
		addProperty("layer" + suffix, "Layer", "" + (num + 1), FormProperty.TEXT);
		addProperty("instrument" + suffix, "Instrument", layer.instrument, FormProperty.NUMBER_INPUT);
		addProperty("volume" + suffix, "Volume", layer.volume, FormProperty.NUMBER_INPUT);
		addProperty("attack" + suffix, "Attack", layer.attack, FormProperty.NUMBER_INPUT);
		addProperty("decay" + suffix, "Decay", layer.decay, FormProperty.NUMBER_INPUT);
		addProperty("pan" + suffix, "Pan", layer.pan, FormProperty.NUMBER_INPUT);
		addProperty("modulation" + suffix, "Modulation", layer.modulation, FormProperty.NUMBER_INPUT);
		addProperty("chorus" + suffix, "Chorus", layer.chorus, FormProperty.NUMBER_INPUT);
		addProperty("filter" + suffix, "Filter", layer.filter, FormProperty.NUMBER_INPUT);
		addProperty("resonance" + suffix, "Resonance", layer.resonance, FormProperty.NUMBER_INPUT);
		addProperty("reverb" + suffix, "Reverb", layer.reverb, FormProperty.NUMBER_INPUT);
		addProperty("vib_rate" + suffix, "Vibrato rate", layer.vib_rate, FormProperty.NUMBER_INPUT);
		addProperty("vib_depth" + suffix, "Vibrato depth", layer.vib_depth, FormProperty.NUMBER_INPUT);
		addProperty("vib_delay" + suffix, "Vibrato delay", layer.vib_delay, FormProperty.NUMBER_INPUT);
	}
}
