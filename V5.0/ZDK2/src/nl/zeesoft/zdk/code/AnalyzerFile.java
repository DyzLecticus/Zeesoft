package nl.zeesoft.zdk.code;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerFile extends CodeFile {
	public List<FileMethod>		methods	= null;
	
	public AnalyzerFile(CodeFile codeFile) {
		super(codeFile);
		MethodParser parser = new MethodParser(path, code);
		this.methods = parser.methods;
	}
	
	public List<String> getErrors(int maxLinesPerFile, int maxLinesPerMethod) {
		List<String> r = new ArrayList<String>();
		if (lines > maxLinesPerFile) {
			r.add("File has too many lines of code; " + toString());
		}
		for (FileMethod method: methods) {
			if (method.lines > maxLinesPerMethod) {
				r.add("Method has too many lines of code; " + method);
			}
		}
		return r;
	}
	
	@Override
	public String toString() {
		return path + "; lines: " + lines + ", methods: " + methods.size();
	}
}
