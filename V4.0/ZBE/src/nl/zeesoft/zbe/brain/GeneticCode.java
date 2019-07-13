package nl.zeesoft.zbe.brain;

import java.util.HashSet;
import java.util.Set;

import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringEncoder;

public class GeneticCode {
	public ZStringEncoder code = new ZStringEncoder();
	
	public void initialize(int length) {
		if (length<100) {
			length = 100;
		}
		if (code==null || code.length()>0) {
			code = new ZStringEncoder();
		}
		code.append(code.generateNewKey(length));
	}
	
	public void mutate(int num) {
		if (code!=null) {
			if (num>=code.length()) {
				initialize(code.length());
			} else if (num>0 && code.length()>0) {
				ZIntegerGenerator indexGen = new ZIntegerGenerator(0,(code.length() - 1));
				ZIntegerGenerator valueGen = new ZIntegerGenerator(0,9);
				Set<Integer> mutPos = new HashSet<Integer>(); 
				for (int n = 0; n < num; n++) {
					int i = -1;
					while (i<0 || mutPos.contains(i)) {
						i = indexGen.getNewInteger();
					}
					mutPos.add(i);
					String o = code.substring(i,i+1).toString();
					String r = o;
					while (r.equals(o)) {
						r = "" + valueGen.getNewInteger();
					}
					code.replace(i,i+1,r);
				}
			}
		}
	}
	
	public int getMaxProperties() {
		int r = 0;
		if (code!=null) {
			r = (code.length() / 3) - 1;
		}
		return r;
	}
	
	public int getPropertyValue(int num, int scale) {
		return Math.round(getPropertyFactor(num) * (float)scale);
	}
	
	public float getPropertyFactor(int num) {
		float r = -1;
		if (num <= getMaxProperties()) {
			int p = num * 3;
			int end = p + 3;
			if (end>=code.length()) {
				end = code.length();
			}
			try {
				p = Integer.parseInt(code.substring(p,end).toString());
			} catch (NumberFormatException e) {
				// Ignore
			}
			p = p % code.length();
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
