package nl.zeesoft.zdbd.api.html.form;

import java.util.List;

import nl.zeesoft.zdbd.neural.Generator;
import nl.zeesoft.zdk.Str;

public class GeneratorList extends FormHtml {
	protected List<Generator> generators 	= null;
	
	public GeneratorList(List<Generator> generators) {
		this.generators = generators;
	}
	
	@Override
	public Str render() {
		Str r = new Str();
		append(r,renderGenerators(generators));
		append(r,renderAddGeneratorButton());
		return r;
	}
	
	public static Str renderGenerators(List<Generator> generators) {
		Str r = new Str();
		int i = 0;
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
			append(r,"<input type=\"button\" value=\"Edit");
			r.sb().append("\" onclick=\"generators.edit('");
			r.sb().append(generator.name);
			r.sb().append("');\"/>");
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
		return r;
	}
	
	public static Str renderAddGeneratorButton() {
		Str r = new Str();
		append(r,"<div class=\"row\">");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<label class=\"column-label\"></label>");
		append(r,"</div>");
		append(r,"<div class=\"column-left column-padding\">");
		append(r,"<input type=\"button\" value=\"Add\" onclick=\"generators.add();\" />");
		append(r,"</div>");
		append(r,"</div>");
		return r;
	}
}
