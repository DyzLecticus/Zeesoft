package nl.zeesoft.zdk.code;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.StrUtil;

public class AnalyzerStats {
	public int		total		= 0;
	public int		totalValue	= 0;
	public float	avg			= 0;
	public float	stdDev		= 0;

	public void analyze(int total, int totalValue, List<Float> values) {
		this.total = total;
		this.totalValue = totalValue;
		avg = (float)totalValue / (float)values.size();
		stdDev = Util.getStandardDeviation(values);
	}
	
	public int getListMax(float factor, int absMax) {
		int r = (int)(avg + (stdDev * factor));
		if (r > absMax) {
			r = absMax;
		}
		return r;
	}
	
	public StringBuilder getAnalysisHeader(String items, String totalVal, String average) {
		StringBuilder r = new StringBuilder();
		StrUtil.appendLine(r, items + ": " + total);
		StrUtil.appendLine(r, totalVal + ": " + totalValue);
		StrUtil.appendLine(r, average + ": " + avg + " (standard deviation: " + stdDev + ")");
		return r;
	}
}
