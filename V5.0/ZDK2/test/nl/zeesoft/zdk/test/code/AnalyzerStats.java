package nl.zeesoft.zdk.test.code;

import java.util.List;

import nl.zeesoft.zdk.Util;

public class AnalyzerStats {
	public int		total		= 0;
	public int		totalValue	= 0;
	public float	avg			= 0;
	public float	stdDev		= 0;

	protected void analyze(int total, int totalValue, List<Float> values) {
		this.total = total;
		this.totalValue = totalValue;
		avg = (float)totalValue / (float)values.size();
		stdDev = Util.getStandardDeviation(values);
	}
	
	protected int getListMax(float factor, int absMax) {
		int r = (int)(avg + (stdDev * factor));
		if (r > absMax) {
			r = absMax;
		}
		return r;
	}
	
	protected StringBuilder getAnalysisHeader(String items, String totalVal, String average) {
		StringBuilder r = new StringBuilder();
		if (total>0) {
			Util.appendLine(r, items + ": " + total);
			Util.appendLine(r, totalVal + ": " + totalValue);
			Util.appendLine(r, average + ": " + avg + " (standard deviation: " + stdDev + ")");
		}
		return r;
	}
}
