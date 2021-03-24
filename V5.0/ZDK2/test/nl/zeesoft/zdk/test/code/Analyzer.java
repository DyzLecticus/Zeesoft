package nl.zeesoft.zdk.test.code;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Util;

public class Analyzer {
	public String				sourceDir			= "src/";
	public int					maxLinesPerFile		= 200;
	public int					maxLinesPerMethod	= 30;
	
	public List<AnalyzerFile>	files				= new ArrayList<AnalyzerFile>();
	
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
				if (file.lines > maxLinesPerFile) {
					r.add("File has too many lines of code; " + file);
				}
				for (AnalyzerFileMethod method: file.methods) {
					if (method.lines > maxLinesPerMethod) {
						r.add("Method has too many lines of code; " + method);
					}
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
			float avg = (float)total / (float)files.size();
			float stdDev = Util.getStandardDeviation(values);
			
			r.append(getHeader(total, avg, stdDev));
			
			int max = (int)(avg + (stdDev * 2F));
			if (max > maxLinesPerFile) {
				max = maxLinesPerFile;
			}
			
			boolean first = true;
			for (AnalyzerFile file: files) {
				if (file.lines > max) {
					if (first) {
						Util.appendLine(r, "Largest files;");
						first = false;
					}
					Util.appendLine(r, "- " + file.toString());
				}
			}
			Util.appendLine(r,"");
			Util.appendLine(r,getMethodAnalysis().toString());
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
	
	protected StringBuilder getHeader(int total, float avg, float stdDev) {
		StringBuilder r = new StringBuilder();
		Util.appendLine(r, "Files: " + files.size());
		Util.appendLine(r, "Lines of code: " + total);
		Util.appendLine(r, "Average lines per file: " + avg + " (standard deviation: " + stdDev + ")");
		return r;
	}
	
	protected StringBuilder getMethodAnalysis() {
		StringBuilder r = new StringBuilder();
		int total = 0;
		List<Float> values = new ArrayList<Float>();
		for (AnalyzerFile file: files) {
			for (AnalyzerFileMethod method: file.methods) {
				total += method.lines;
				values.add((float)method.lines);
			}
		}
		if (total>0) {
			float avg = (float)total / (float)values.size();
			float stdDev = Util.getStandardDeviation(values);
			
			Util.appendLine(r, "Methods: " + values.size());
			Util.appendLine(r, "Average lines per method: " + avg + " (standard deviation: " + stdDev + ")");
			
			int max = (int) (avg + (stdDev * 3F));
			if (max > maxLinesPerMethod) {
				max = maxLinesPerMethod;
			}
			
			boolean first = true;
			for (AnalyzerFile file: files) {
				for (AnalyzerFileMethod method: file.methods) {
					if (method.lines > max) {
						if (first) {
							Util.appendLine(r, "Largest methods;");
							first = false;
						}
						Util.appendLine(r, "- " + method.toString());
					}
				}
			}
		}
		return r;
	}
}
