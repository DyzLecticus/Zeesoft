package nl.zeesoft.zdbd.api.html.form;

import java.util.List;

import nl.zeesoft.zdbd.neural.Generator;
import nl.zeesoft.zdk.Str;

public class GeneratorList extends FormHtml {
	private List<Generator> generators 	= null;
	private boolean			generate	= false;
	
	public GeneratorList(List<Generator> generators, boolean regenerate, boolean generate) {
		this.generators = generators;
		this.generate = generate;
		String renderAs = FormProperty.BUTTON_INPUT;
		if (!generate) {
			renderAs = FormProperty.BUTTON_DISABLED;
		}
		String value = "Generate sequences";
		if (!regenerate) {
			boolean gen = true;
			for (Generator generator: generators) {
				if (generator.generatedPatternSequence!=null) {
					gen = false;
					break;
				}
			}
			regenerate = gen;
		}
		if (regenerate) {
			value += "*";
		}
		addProperty("generateAll", "Generators", value, renderAs, "generators.generateAll();");
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,properties.get(0).render());
		append(r,"<div class=\"column-right column-padding\">");
		append(r,"<input type=\"button\" class=\"show-hide\" id=\"showGeneratorList\" value=\"+\" onclick=\"generators.toggleShowList(this);\" />");
		append(r,"</div>");
		append(r,"</div>");
		append(r,renderGenerators(generators,generate));
		return r;
	}
	
	public static Str renderGenerators(List<Generator> generators, boolean generate) {
		Str r = new Str();
		int i = 0;
		append(r,"<div id=\"generatorList\" class=\"hidden\">");
		for (Generator generator: generators) {
			String upDisabled = "";
			String downDisabled = "";
			if (i==0) {
				upDisabled = " DISABLED";
			} else if (i==(generators.size()-1)) {
				downDisabled = " DISABLED";
			}
			
			append(r,"<div class=\"row");
			if (i % 2==0) {
				r.sb().append(" row-highlight");
			}
			r.sb().append("\">");
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
			r.sb().append("');\"");
			r.sb().append(upDisabled);
			r.sb().append(" />");
			append(r,"<input type=\"button\" value=\"Down\" onclick=\"generators.moveDown('");
			r.sb().append(generator.name);
			r.sb().append("');\"");
			r.sb().append(downDisabled);
			r.sb().append(" />");
			append(r,"</div>");
			
			append(r,"<div class=\"column-right column-padding\">");
			append(r,"<input type=\"button\" value=\"Delete\" onclick=\"generators.delete('");
			r.sb().append(generator.name);
			r.sb().append("');\" />");
			append(r,"</div>");
			append(r,"</div>");
			
			i++;
		}
		append(r,"</div>");
		return r;
	}
}
