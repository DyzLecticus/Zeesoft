package nl.zeesoft.zdk.test.code;

public class AnalyzerFile {
	public String			path	= "";
	public StringBuilder	content = null;
	public int				lines	= 0;
	
	public AnalyzerFile(String path, StringBuilder content) {
		this.path = path;
		this.content = content;
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
}
