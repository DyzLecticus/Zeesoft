package nl.zeesoft.zdk.code;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Util;

public class Analyzer {
	public int					maxLinesPerFile			= 200;
	public int					maxLinesPerMethod		= 30;
	
	public List<AnalyzerFile>	files					= new ArrayList<AnalyzerFile>();
	
	public AnalyzerStats		fileStats				= new AnalyzerStats();
	public AnalyzerStats		methodStats				= new AnalyzerStats();
	
	public Analyzer(List<CodeFile> codeFiles) {
		for (CodeFile file: codeFiles) {
			files.add(new AnalyzerFile(file));
		}
	}
	
	public void analyze() {
		analyzeFiles();
		analyzeMethods();
	}
	
	public List<String> getErrors() {
		List<String> r = new ArrayList<String>();
		for (AnalyzerFile file: files) {
			r.addAll(file.getErrors(maxLinesPerFile, maxLinesPerMethod));
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
		int total = 0;
		for (AnalyzerFile file: files) {
			total += file.lines;
			values.add((float)file.lines);
		}
		fileStats.analyze(files.size(), total, values);
	}

	protected void analyzeMethods() {
		List<Float> values = new ArrayList<Float>();
		int total = 0;
		int totalMethods = 0;
		for (AnalyzerFile file: files) {
			for (FileMethod method: file.methods) {
				totalMethods ++;
				total += method.lines;
				values.add((float)method.lines);
			}
		}
		methodStats.analyze(totalMethods, total, values);
	}
	
	protected StringBuilder getFileAnalysis() {
		StringBuilder r = new StringBuilder();
		if (files.size() > 0) {
			r.append(fileStats.getAnalysisHeader("Files", "Lines of code", "Average lines per file"));
			int max = fileStats.getListMax(2F, maxLinesPerFile);
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
		if (methodStats.total>0) {
			r.append(methodStats.getAnalysisHeader("Methods", "Lines of code", "Average lines per method"));
			int max = methodStats.getListMax(3F, maxLinesPerMethod);
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
