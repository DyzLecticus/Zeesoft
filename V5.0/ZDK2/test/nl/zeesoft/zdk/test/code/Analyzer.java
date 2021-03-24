package nl.zeesoft.zdk.test.code;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Util;

public class Analyzer {
	public String				sourceDir				= "src/";
	public int					maxLinesPerFile			= 200;
	public int					maxLinesPerMethod		= 30;
	
	public List<AnalyzerFile>	files					= new ArrayList<AnalyzerFile>();
	
	public int					totalLines				= 0;
	public float				avgLinesPerFile			= 0;
	public float				linesPerFileStdDev		= 0;
	
	public int					totalMethods			= 0;
	public float				avgLinesPerMethod		= 0;
	public float				linesPerMethodStdDev	= 0;
	
	
	public void analyze() {
		File src = new File(sourceDir);
		if (src.exists() && src.canRead()) {
			readFiles(src, ".java");
		}
		analyzeFiles();
		analyzeMethods();
	}
	
	public List<String> getErrors() {
		List<String> r = new ArrayList<String>();
		File src = new File(sourceDir);
		if (src.exists() && src.canRead()) {
			for (AnalyzerFile file: files) {
				if (file.lines > maxLinesPerFile) {
					r.add("File has too many lines of code; " + file);
				}
				for (FileMethod method: file.methods) {
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
		Util.appendLine(r, getFileAnalysis().toString());
		StringBuilder ma = getMethodAnalysis();
		if (ma.length()>0) {
			r.append("\n");
			Util.appendLine(r, ma.toString());
		}
		return r.toString();
	}

	protected void analyzeFiles() {
		List<Float> values = new ArrayList<Float>();
		for (AnalyzerFile file: files) {
			totalLines += file.lines;
			values.add((float)file.lines);
		}
		if (totalLines > 0) {
			avgLinesPerFile = (float)totalLines / (float)files.size();
			linesPerFileStdDev = Util.getStandardDeviation(values);
		}
	}

	protected void analyzeMethods() {
		List<Float> values = new ArrayList<Float>();
		int total = 0;
		for (AnalyzerFile file: files) {
			for (FileMethod method: file.methods) {
				totalMethods ++;
				total += method.lines;
				values.add((float)method.lines);
			}
		}
		if (totalMethods>0) {
			avgLinesPerMethod = (float)total / (float)values.size();
			linesPerMethodStdDev = Util.getStandardDeviation(values);
		}
	}
	
	protected int getListMaxLinesPerFile() {
		int r = (int)(avgLinesPerFile + (linesPerFileStdDev * 2F));
		if (r > maxLinesPerFile) {
			r = maxLinesPerFile;
		}
		return r;
	}
	
	protected int getListMaxLinesPerMethod() {
		int r = (int)(avgLinesPerMethod + (linesPerMethodStdDev * 3F));
		if (r > maxLinesPerMethod) {
			r = maxLinesPerMethod;
		}
		return r;
	}
	
	protected void readFiles(File parent, String endsWith) {
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
	
	protected StringBuilder getFileAnalysis() {
		StringBuilder r = new StringBuilder();
		if (files.size() > 0) {
			Util.appendLine(r, "Files: " + files.size());
			Util.appendLine(r, "Lines of code: " + totalLines);
			Util.appendLine(r,
				"Average lines per file: " + avgLinesPerFile +
				" (standard deviation: " + linesPerFileStdDev + ")"
				);
			int max = getListMaxLinesPerFile();
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
		}
		return r;
	}
	
	protected StringBuilder getMethodAnalysis() {
		StringBuilder r = new StringBuilder();
		if (totalMethods>0) {
			Util.appendLine(r, "Methods: " + totalMethods);
			Util.appendLine(r,
				"Average lines per method: " + avgLinesPerMethod +
				" (standard deviation: " + linesPerMethodStdDev + ")"
				);
			int max = getListMaxLinesPerMethod();
			boolean first = true;
			for (AnalyzerFile file: files) {
				for (FileMethod method: file.methods) {
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
