package nl.zeesoft.zbe.brain;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;

public class GeneticCode {
	private ZStringEncoder 	code		= new ZStringEncoder();
	private List<Float>		factors		= new ArrayList<Float>();
	
	public GeneticCode() {
		
	}
	
	public GeneticCode(int length) {
		generate(length);
	}
	
	public GeneticCode(ZStringBuilder code) {
		setCode(code);
	}
	
	public void setCode(ZStringBuilder code) {
		this.code = new ZStringEncoder(code);
		refreshFactors();
	}
	
	public ZStringBuilder getCode() {
		return new ZStringBuilder(code);
	}

	public int length() {
		return code.length();
	}

	public void generate(int length) {
		if (length<100) {
			length = 100;
		}
		if (code==null || code.length()>0) {
			code = new ZStringEncoder();
		}
		code.append(code.generateNewKey(length));
		refreshFactors();
	}
	
	public void mutate(int num) {
		if (code!=null) {
			if (num>=(code.length() / 2)) {
				generate(code.length());
			} else if (num>0 && code.length()>0) {
				int div = code.length() / num;
				ZIntegerGenerator indexGen = null;
				ZIntegerGenerator valueGen = new ZIntegerGenerator(0,9);
				for (int n = 0; n < num; n++) {
					int min = n * div;
					int max = (n + 1) * div;
					if (max>code.length() - 1) {
						max = code.length() - 1;
					}
					indexGen = new ZIntegerGenerator(min,max);
					int i = indexGen.getNewInteger();
					String o = code.substring(i,i+1).toString();
					String r = o;
					while (r.equals(o)) {
						r = "" + valueGen.getNewInteger();
					}
					code.replace(i,i+1,r);
				}
			}
			refreshFactors();
		}
	}
	
	public int size() {
		int r = 0;
		if (code!=null) {
			r = code.length() / 3;
		}
		return r;
	}
	
	public int getValue(int index, int scale) {
		return Math.round(get(index) * (float)scale);
	}
	
	public float get(int index) {
		float r = -1;
		if (index>-1 && factors.size()>index) {
			r = factors.get(index);
		}
		return r;
	}
	
	private void refreshFactors() {
		factors.clear();
		for (int p = 0; p < size(); p++) {
			factors.add(getPropertyFactor(p));
		}
	}
	
	private float getPropertyFactor(int index) {
		float r = -1;
		if (index < size()) {
			int p = index * 3;
			if (index>0) {
				p = p - 1;
			}
			int end = p + 3;
			if (end>=code.length()) {
				end = code.length();
			}
			try {
				p = Integer.parseInt(code.substring(p,end).toString());
			} catch (NumberFormatException e) {
				// Ignore
			}
			if (code.length() % 100 == 0) {
				p = p % (code.length() - 1);
			} else {
				p = p % code.length();
			}
			end = p + 3;
			if (end>=code.length()) {
				end = code.length();
				p = end - 3;
			}
			String f = "0." + code.substring(p,end).toString();
			try {
				r = Float.parseFloat(f);
			} catch (NumberFormatException e) {
				// Ignore
			}
		}
		return r;
	}
}
