package nl.zeesoft.zdbd.api.html.form;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.neural.Generator;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdk.Str;

public class GeneratorEditor extends FormHtml {
	protected String	prevName	= "";
	protected String	nextName	= "";
	
	public GeneratorEditor(Generator generator, String prevName, String nextName) {
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

		List<String> maintainList = new ArrayList<String>();
		for (int i = 0; i < generator.maintainInstruments.length; i++) {
			maintainList.add(generator.maintainInstruments[i]);
		}
		for (PatternInstrument inst: InstrumentPattern.INSTRUMENTS) {
			boolean maintain = maintainList.contains(inst.name());
			addProperty("maintain-" + inst.name(), "Maintain " + inst.name().toLowerCase(), maintain, FormProperty.CHECKBOX_INPUT);
		}
		for (FormProperty property: properties) {
			property.onChange = "generators.propertyChange(this);";
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
			append(r,"<input type=\"button\" value=\"<<\" onclick=\"generators.edit('");
			r.sb().append(prevName);
			r.sb().append("');\"/>");
			append(r,"<input type=\"button\" value=\">>\" onclick=\"generators.edit('");
			r.sb().append(nextName);
			r.sb().append("');\"/>");
			append(r,"</div>");
			append(r,"</div>");
		}
		return r;
	}
}
