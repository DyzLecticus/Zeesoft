package nl.zeesoft.zdk.code;

import nl.zeesoft.zdk.Util;

public class CodeFile {
	public String				path	= "";
	public StringBuilder		content = null;
	public StringBuilder		code	= null;
	public int					lines	= 0;
	
	public CodeFile(CodeFile codeFile) {
		this.path = codeFile.path;
		this.content = new StringBuilder(codeFile.content);
		this.code = new StringBuilder(codeFile.code);
		this.lines = codeFile.lines;
	}
	
	public CodeFile(String path, StringBuilder content) {
		this.path = path;
		this.content = content;
		this.code = parseCodeFromContent();
		lines = code.toString().split("\\n").length;
	}

	protected StringBuilder parseCodeFromContent() {
		StringBuilder r = new StringBuilder();
		String[] lines = content.toString().split("\\n");
		for (String line: lines) {
			line = line.replaceAll("\t", "").trim();
			if (isCode(line)) {
				Util.appendLine(r, line);
			}
		}
		return r;
	}
	
	protected boolean isCode(String line) {
		return
			line.length()>0 && 
			!line.startsWith("//") && 
			!line.startsWith("*") && 
			!line.startsWith("/*")
			;
	}
}
