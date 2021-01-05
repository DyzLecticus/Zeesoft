package nl.zeesoft.zdbd.api.html.form;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.Generator;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdk.Str;

public class GeneratorEditor extends FormHtml {
	public GeneratorEditor(Generator generator) {
		addProperty("name", "Name", generator.name, FormProperty.TEXT_INPUT);
		addProperty("group1Distortion", "Group 1 distortion", generator.group1Distortion, FormProperty.ANY_INPUT);
		addProperty("group2Distortion", "Group 2 distortion", generator.group2Distortion, FormProperty.ANY_INPUT);
		addProperty("randomChunkOffset", "Random chunk offset", generator.randomChunkOffset, FormProperty.CHECKBOX_INPUT);

		addProperty("mixStart", "Mix start", generator.mixStart, FormProperty.ANY_INPUT);
		addProperty("mixEnd", "Mix end", generator.mixEnd, FormProperty.ANY_INPUT);
		
		addProperty("maintainBeat", "Maintain beat", generator.maintainBeat, FormProperty.ANY_INPUT);
		addProperty("maintainFeedback", "Maintain feedback", generator.maintainFeedback, FormProperty.CHECKBOX_INPUT);

		List<String> skipList = new ArrayList<String>();
		for (int i = 0; i < generator.skipInstruments.length; i++) {
			skipList.add(generator.skipInstruments[i]);
		}
		for (PatternInstrument inst: InstrumentPattern.INSTRUMENTS) {
			boolean skip = skipList.contains(inst.name());
			addProperty("skip-" + inst.name(), "Skip " + inst.name().toLowerCase(), skip, FormProperty.CHECKBOX_INPUT);
		}
		for (FormProperty property: properties) {
			property.onChange = "generators.propertyChange(this);";
		}
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,super.render());
		append(r,renderDoneButton());
		return r;
	}
	
	public static Str renderDoneButton() {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-right column-padding\">");
		append(r,"<input type=\"button\" id=\"formOk\" value=\"Done\" onclick=\"generators.editDone();\" />");
		append(r,"</div>");
		append(r,"</div>");
		return r;
	}
}
