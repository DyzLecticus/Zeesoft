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
			if (parseMethod(method, i, code)) {
				methods.add(method);
				method = new FileMethod();
			}
			pc = c;
		}
	}
	
	protected void handleStateCharacter(String c, String pc) {
		handleQuotes(c, pc);
		handleBraces(c, pc);
	}
	
	protected void handleQuotes(String c, String pc) {
		if (!pc.equals("\\")) {
			if (c.equals("\"")) {
				inDoubleQuote = !inDoubleQuote;
			} else if (c.equals("'") && !inDoubleQuote) {
				inSingleQuote = !inSingleQuote;
			}
		}
	}
	
	protected void handleBraces(String c, String pc) {
		if (!inSingleQuote && !inDoubleQuote) {
			if (c.equals("{")) {
				braceLevel++;
			} else if (c.equals("}")) {
				braceLevel--;
			}
		}
	}
	
	protected boolean parseMethod(FileMethod method, int i, StringBuilder code) {
		boolean r = false;
		String c = code.substring(i,i+1);
		if (braceLevel==2 && method.prefix.length()==0) {
			method.prefix = parseMethodPrefix(code, i);
		} else if (braceLevel>=2) {
			method.content.append(c);
		} else if (braceLevel==1 && c.equals("}") && !inSingleQuote && !inDoubleQuote) {
			method.lines = method.content.toString().split("\\n").length;
			r = true;
		}
		return r;
	}
	
	public static String parseMethodPrefix(StringBuilder code, int pos) {
		String prefix = "";
		for (int t = (pos - 10); t >= 0; t--) {
			String start = code.substring(t,t+10);
			if (isMethodStart(start)) {
				prefix = code.substring(t, pos).replaceAll("\\n", " ").trim();
				break;
			}
		}
		return prefix;
	}
	
	protected static boolean isMethodStart(String start) {
		boolean r = false;
		if (start.startsWith("private ") ||
			start.startsWith("protected ") ||
			start.startsWith("public ")
			) {
			r = true;
		}
		return r;
	}
}
