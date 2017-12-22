package nl.zeesoft.zid.dialog.io;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogExample;
import nl.zeesoft.zid.dialog.DialogVariable;
import nl.zeesoft.zid.dialog.DialogVariableExample;
import nl.zeesoft.zspr.pattern.PatternObject;

/**
 * DialogQnATsv provides methods to convert Dialog objects to and from TSV.
 */
public class DialogQnATsv {
	
	public ZStringBuilder toQnATsv(Dialog dialog) {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Question");
		r.append("\t");
		r.append("Answer");
		r.append("\t");
		r.append("Variable");
		r.append("\n");
		for (DialogExample example: dialog.getExamples()) {
			r.append(example.getInput());
			r.append("\t");
			r.append(example.getOutput());
			r.append("\n");
		}
		for (DialogVariable variable: dialog.getVariables()) {
			for (DialogVariableExample dialogVE: variable.getExamples()) {
				r.append(dialogVE.getQuestion());
				r.append("\t");
				r.append(dialogVE.getAnswer());
				r.append("\t");
				r.append(variable.getName());
				r.append("\n");
			}
			if (variable.getExamples().size()<=0) {
				r.append("\t");
				r.append("\t");
				r.append(variable.getName());
				r.append("\n");
			}
		}
		return r;
	}

	public Dialog fromQnATsv(ZStringBuilder tsv,String languageCode,String controllerClassName) {
		return fromQnATsv(tsv,"QnA",languageCode,controllerClassName);
	}

	public Dialog fromQnATsv(ZStringBuilder tsv,String name, String languageCode,String controllerClassName) {
		Dialog r = new Dialog("QnA",languageCode,controllerClassName);
		int l = 0;
		List<ZStringBuilder> lines = tsv.split("\n");
		for (ZStringBuilder line: lines) {
			if (l>1) {
				List<ZStringBuilder> cells = line.split("\t");
				if (cells.size()>=2) {
					String n = "";
					String i = cells.get(0).toString();
					String o = cells.get(1).toString();
					if (cells.size()>2) {
						n = cells.get(2).toString();
					}
					if (i.length()>0 && o.length()>0) {
						if (n.length()>0) {
							if (r.getVariable(n)==null) {
								r.addVariable(n,PatternObject.TYPE_ALPHABETIC);
							}
							r.addVariableExample(n,i,o);
						} else {
							r.addExample(i,o);
						}
					} else if (n.length()>0 && r.getVariable(n)==null) {
						r.addVariable(n,PatternObject.TYPE_ALPHABETIC);
					}
				}
			}
			l++;
		}
		return r;
	}
	
}
