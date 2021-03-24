package nl.zeesoft.zdk.code;

import java.util.ArrayList;
import java.util.List;

public class MethodParser {
	protected int			braceLevel		= 0;
	protected boolean		inDoubleQuote	= false;
	protected boolean		inSingleQuote	= false;
	
	public List<FileMethod>	methods			= new ArrayList<FileMethod>();
	
	public MethodParser(String path, StringBuilder code) {
		parseMethodsFromCode(code);
		for (FileMethod method: methods) {
			method.path = path;
		}
	}
	
	protected void parseMethodsFromCode(StringBuilder code) {
		String pc = "";
		FileMethod method = new FileMethod();
		for (int i = 0; i < code.length(); i++) {
			String c = code.substring(i,i+1);
			handleStateCharacter(c, pc);
			if (braceLevel==2 && method.prefix.length()==0) {
				parseMethodPrefix(code, i, method);
			}
			if (braceLevel>=2) {
				method.content.append(c);
			}
			if (braceLevel==1 && c.equals("}") && method.prefix.length()>0) {
				method.lines = method.content.toString().split("\\n").length;
				methods.add(method);
				method = new FileMethod();
			}
			pc = c;
		}
	}
	
	protected void handleStateCharacter(String c, String pc) {
		if (!pc.equals("\\")) {
			if (c.equals("\"")) {
				inDoubleQuote = !inDoubleQuote;
			} else if (c.equals("'") && !inDoubleQuote) {
				inSingleQuote = !inSingleQuote;
			}
		}
		if (!inSingleQuote && !inDoubleQuote) {
			if (c.equals("{")) {
				braceLevel++;
			} else if (c.equals("}")) {
				braceLevel--;
			}
		}
	}
	
	protected void parseMethodPrefix(StringBuilder code, int pos, FileMethod method) {
		for (int t = (pos - 10); t >= 0; t--) {
			String start = code.substring(t,t+10);
			if (start.startsWith("private ") ||
				start.startsWith("protected ") ||
				start.startsWith("public ")
				) {
				method.prefix = code.substring(t, pos).replaceAll("\\n", " ").trim();
				break;
			}
		}

	}
}
