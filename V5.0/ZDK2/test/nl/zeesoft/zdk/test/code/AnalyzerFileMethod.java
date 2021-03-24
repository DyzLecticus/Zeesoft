package nl.zeesoft.zdk.test.code;

public class AnalyzerFileMethod {
	public String			path	= "";
	public String			prefix	= "";
	public StringBuilder	content	= new StringBuilder();
	public int				lines	= 0;
	
	@Override
	public String toString() {
		return path + "; " + prefix + "; lines: " + lines;
	}
}
