package nl.zeesoft.zdk.test.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import nl.zeesoft.zdk.Util;

public class AnalyzerFile {
	public String						path	= "";
	public StringBuilder				content = null;
	public int							lines	= 0;
	
	public List<AnalyzerFileMethod>		methods	= new ArrayList<AnalyzerFileMethod>();
	
	public AnalyzerFile(File file) {
		this.path = file.getPath();
		this.content = readFile(file);
		lines = content.toString().split("\\n").length;
		analyzeMethods();
	}
	
	@Override
	public String toString() {
		return path + "; lines: " + lines;
	}

	public StringBuilder readFile(File file) {
		StringBuilder r = new StringBuilder();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().replaceAll("\t", "").trim();
				if (isCode(line)) {
					Util.appendLine(r, line);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public boolean isCode(String line) {
		return
			line.length()>0 && 
			!line.startsWith("//") && 
			!line.startsWith("*") && 
			!line.startsWith("/*") &&
			!line.equals("*/")
			;
	}
	
	protected void analyzeMethods() {
		int braceLevel = 0;
		boolean inDoubleQuote = false;
		boolean inSingleQuote = false;
		String pc = "";
		AnalyzerFileMethod method = new AnalyzerFileMethod();
		method.path = path;
		for (int i = 0; i < content.length(); i++) {
			String c = content.substring(i,i+1);
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
			if (braceLevel==2 && method.prefix.length()==0) {
				for (int t = (i - 10); t >= 0; t--) {
					String start = content.substring(t,t+10);
					if (start.startsWith("private ") ||
						start.startsWith("protected ") ||
						start.startsWith("public ")
						) {
						method.prefix = content.substring(t, i).replaceAll("\\n", " ").trim();
						break;
					}
				}
			}
			if (braceLevel>=2) {
				method.content.append(c);
			}
			if (braceLevel==1 && c.equals("}") && method.prefix.length()>0) {
				method.lines = method.content.toString().split("\\n").length;
				methods.add(method);
				method = new AnalyzerFileMethod();
				method.path = path;
			}
			pc = c;
		}
	}
}
