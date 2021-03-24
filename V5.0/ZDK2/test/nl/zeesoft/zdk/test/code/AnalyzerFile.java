package nl.zeesoft.zdk.test.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import nl.zeesoft.zdk.Util;

public class AnalyzerFile {
	public String				path	= "";
	public StringBuilder		content = null;
	public StringBuilder		code	= null;
	public int					lines	= 0;
	public List<FileMethod>		methods	= null;
	
	public AnalyzerFile(File file) {
		this.path = file.getPath();
		this.content = readFile(file);
		this.code = parseCodeFromContent();
		lines = code.toString().split("\\n").length;
		MethodParser parser = new MethodParser(path, code);
		this.methods = parser.methods;
	}
	
	@Override
	public String toString() {
		return path + "; lines: " + lines + ", methods: " + methods.size();
	}

	protected StringBuilder readFile(File file) {
		StringBuilder r = new StringBuilder();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				Util.appendLine(r, scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return r;
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
			!line.startsWith("/*") &&
			!line.equals("*/")
			;
	}
}
