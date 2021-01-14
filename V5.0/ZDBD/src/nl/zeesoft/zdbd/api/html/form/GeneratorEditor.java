package nl.zeesoft.zdbd.api.html.form;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.Generator;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;

public class GeneratorEditor extends FormHtml {
	public GeneratorEditor(Generator generator) {
		cancelLabel = "Done";
		onCancelClick = "generators.editDone();";
		
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
}
