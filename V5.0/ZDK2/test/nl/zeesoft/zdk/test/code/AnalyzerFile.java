package nl.zeesoft.zdk.test.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import nl.zeesoft.zdk.Util;

public class AnalyzerFile {
	public String			path	= "";
	public StringBuilder	content = null;
	public int				lines	= 0;
	
	public AnalyzerFile(File file) {
		this.path = file.getPath();
		this.content = readFile(file);
		lines = content.toString().split("\\n").length;
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		r.append(path);
		r.append("; lines: ");
		r.append(lines);
		return r.toString();
	}

	public StringBuilder readFile(File file) {
		StringBuilder r = new StringBuilder();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line = line.replaceAll("\t", "").trim();
				if (line.length()>0) {
					Util.appendLine(r, line);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return r;
	}
}
