package nl.zeesoft.zdbd.api.html.form;

import java.util.List;

import nl.zeesoft.zdbd.neural.Generator;
import nl.zeesoft.zdk.Str;

public class GeneratorOverview extends FormHtml {
	private List<Generator> generators 	= null;
	private boolean			generate	= false;
	
	public GeneratorOverview(List<Generator> generators, boolean generate) {
		this.generators = generators;
		this.generate = generate;
		String renderAs = FormProperty.BUTTON_INPUT;
		if (!generate) {
			renderAs = FormProperty.BUTTON_DISABLED;
		}
		addProperty("generateAll", "Generators", "Generate sequences", renderAs, "generators.generateAll()");
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,super.render());
		append(r,renderGenerators(generators,generate));
		return r;
	}
	
	public static Str renderGenerators(List<Generator> generators, boolean generate) {
		Str r = new Str();
		for (Generator generator: generators) {
			append(r,"<div class=\"row\">");
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<label class=\"column-label\">");
			r.sb().append(generator.name);
			r.sb().append("</label>");
			append(r,"</div>");
			
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"Generate");
			if (generator.generatedPatternSequence==null) {
				r.sb().append("*");
			}
			r.sb().append("\" onclick=\"generators.generateSequence('");
			r.sb().append(generator.name);
			r.sb().append("');\"");
			if (!generate) {
				r.sb().append(" DISABLED");
			}
			r.sb().append(" />");
			append(r,"</div>");
			
			append(r,"<div class=\"column-left column-padding\">");
			append(r,"<input type=\"button\" value=\"Up\" onclick=\"generators.moveUp('");
			r.sb().append(generator.name);
			r.sb().append("');\" />");
			append(r,"<input type=\"button\" value=\"Down\" onclick=\"generators.moveUp('");
			r.sb().append(generator.name);
			r.sb().append("');\" />");
			append(r,"</div>");
			
			append(r,"<div class=\"column-right column-padding\">");
			append(r,"<input type=\"button\" value=\"Delete\" onclick=\"generators.delete('");
			r.sb().append(generator.name);
			r.sb().append("');\" />");
			append(r,"</div>");
			append(r,"</div>");
		}
		return r;
	}
}
