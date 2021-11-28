package nl.zeesoft.zdk.blackbox;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Util;

public class GeneticCode {
	public StringBuilder code	= new StringBuilder();
	
	public void generate(int length) {
		while(code.length() < length) {
			code.append(Rand.getRandomInt(0, Integer.MAX_VALUE));
		}
		trimCode(length);
	}

	public GeneticCode copy() {
		GeneticCode r = new GeneticCode();
		r.code = new StringBuilder(code);
		return r;
	}
	
	public void mutate(int mutations) {
		int length = code.length();
		for (int i = 0; i < mutations; i++) {
			int s = Rand.getRandomInt(0, code.length() - 1);
			String c = ("" + Rand.getRandomInt(0, Integer.MAX_VALUE));
			int e = s + c.length();
			if (e > code.length()) {
				e = code.length();
			}
			code.replace(s, e, c);
		}
		trimCode(length);
	}
	
	public float compareTo(GeneticCode other) {
		int diff = 0;
		for (int i = 0; i < code.length(); i++) {
			if (i>=other.code.length()) {
				diff += code.length() - other.code.length();
				break;
			}
			String s = code.substring(i, i+1);
			String os = other.code.substring(i, i+1);
			if (!s.equals(os)) {
				diff++;
			}
		}
		return (float)diff / (float)code.length();
	}

	private void trimCode(int length) {
		if (code.length()>length) {
			code.delete(length, code.length());
		}
	}
	
	public int getValue(int start, int minValue, int maxValue) {
		start = start % code.length();
		int r = 0;
		int end = start + ("" + maxValue).length();
		int add = 0;
		if (end > code.length()) {
			add = end - code.length();
			end = code.length();
		}
		String v = code.substring(start, end);
		if (add>0) {
			v += code.substring(0, add);
		}
		int range = (maxValue - minValue) + 1;
		r = minValue + (Util.parseInt(v) % range);
		return r;
	}
}
