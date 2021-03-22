package nl.zeesoft.zdk.test.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import nl.zeesoft.zdk.Util;

public class Analyzer {
	public String				sourceDir	= "src/";
	public List<AnalyzerFile>	files		= new ArrayList<AnalyzerFile>();
	
	public void analyze() {
		File src = new File(sourceDir);
		if (src.exists() && src.canRead()) {
			readFiles(src, ".java");
		}
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		int total = 0;
		for (AnalyzerFile file: files) {
			total += file.lines;
		}
		if (total > 0) {
			int avg = total / files.size(); 
			Util.appendLine(r, "");
			Util.appendLine(r, "Files: " + files.size());
			Util.appendLine(r, "Lines of code: " + total);
			Util.appendLine(r, "Average per file: " + avg);

			boolean first = true;
			for (AnalyzerFile file: files) {
				if (file.lines > avg * 2) {
					if (first) {
						Util.appendLine(r, "Largest files;");
						first = false;
					}
					Util.appendLine(r, "- " + file.toString());
				}
			}
		}
		return r.toString();
	}
	
	public void readFiles(File parent, String endsWith) {
		for (File file: parent.listFiles()) {
			if (file.canRead() && file.getName().endsWith(endsWith)) {
				files.add(new AnalyzerFile(file.getPath(), readFile(file)));
			}
		}
		for (File file: parent.listFiles()) {
			if (file.canRead() && file.isDirectory()) {
				readFiles(file, endsWith);
			}
		}
	}

	public StringBuilder readFile(File file) {
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
}
