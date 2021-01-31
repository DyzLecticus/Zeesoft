package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdbd.midi.Arpeggiator;
import nl.zeesoft.zdk.Str;

public class ArpeggiatorEditor extends FormHtml {
	protected String	prevName	= "";
	protected String	nextName	= "";
	
	public ArpeggiatorEditor(Arpeggiator arpeggiator, String prevName, String nextName) {
		cancelLabel = "Done";
		onCancelClick = "generators.editDone();";
		
		addProperty("name", "Name", arpeggiator.name, FormProperty.TEXT_INPUT);
		addProperty("minDuration", "Minimum duration", arpeggiator.minDuration, FormProperty.NUMBER_INPUT);
		addProperty("maxDuration", "Maximum duration", arpeggiator.maxDuration, FormProperty.NUMBER_INPUT);
		addProperty("density", "Density", arpeggiator.density, FormProperty.ANY_INPUT);
		addProperty("maxOctave", "Maximum octave", arpeggiator.maxOctave, FormProperty.NUMBER_INPUT);
		addProperty("maxInterval", "Maximum interval", arpeggiator.maxInterval, FormProperty.NUMBER_INPUT);
		addProperty("maxSteps", "Maximum steps", arpeggiator.maxSteps, FormProperty.NUMBER_INPUT);
		for (FormProperty property: properties) {
			property.onChange = "arpeggiators.propertyChange(this);";
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
			append(r,"<input type=\"button\" value=\"<<\" onclick=\"arpeggiators.edit('");
			r.sb().append(prevName);
			r.sb().append("');\"/>");
			append(r,"<input type=\"button\" value=\">>\" onclick=\"arpeggiators.edit('");
			r.sb().append(nextName);
			r.sb().append("');\"/>");
			append(r,"</div>");
			append(r,"</div>");
		}
		return r;
	}
}
