package nl.zeesoft.zdk.test.code;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Util;

public class Analyzer {
	public String				sourceDir	= "src/";
	public int					maxLines	= 200;
	
	public List<AnalyzerFile>	files		= new ArrayList<AnalyzerFile>();
	
	public void analyze() {
		File src = new File(sourceDir);
		if (src.exists() && src.canRead()) {
			readFiles(src, ".java");
		}
	}
	
	public List<String> getErrors() {
		List<String> r = new ArrayList<String>();
		File src = new File(sourceDir);
		if (src.exists() && src.canRead()) {
			for (AnalyzerFile file: files) {
				if (file.lines > 200) {
					r.add("File has too many lines of code; " + file);
				}
			}
		}
		return r;
	}
	
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		int total = 0;
		List<Float> values = new ArrayList<Float>();
		for (AnalyzerFile file: files) {
			total += file.lines;
			values.add((float)file.lines);
		}
		if (total > 0) {
			int avg = total / files.size();
			float stdDev = Util.getStandardDeviation(values);
			r.append(addHeader(total, avg, stdDev));
			boolean first = true;
			for (AnalyzerFile file: files) {
				if (file.lines > avg + (stdDev * 2)) {
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
				files.add(new AnalyzerFile(file));
			}
		}
		for (File file: parent.listFiles()) {
			if (file.canRead() && file.isDirectory()) {
				readFiles(file, endsWith);
			}
		}
	}
	
	protected StringBuilder addHeader(int total, int avg, float stdDev) {
		StringBuilder r = new StringBuilder();
		Util.appendLine(r, "Files: " + files.size());
		Util.appendLine(r, "Lines of code: " + total);
		Util.appendLine(r, "Average lines per file: " + avg);
		Util.appendLine(r, "Standard deviation: " + stdDev);
		return r;
	}
}
