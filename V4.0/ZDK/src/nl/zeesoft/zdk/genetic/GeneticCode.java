package nl.zeesoft.zdk.genetic;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.functions.ZRandomize;

/**
 * GeneticCode provides a crude numeric representation of DNA.
 * 
 * The length of the genetic code determines the number unique of properties it can represent (length / 3).
 * By using elements as index indicators for actual values it can connect several properties together causing a single mutation to impact multiple property values.
 */
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
	
	/**
	 * Sets the code.
	 * 
	 * @param code The code to set
	 */
	public void setCode(ZStringBuilder code) {
		this.code = new ZStringEncoder(code);
		refreshFactors();
	}
	
	/**
	 * Returns a copy of the code.
	 * 
	 * @return A copy of the code
	 */
	public ZStringBuilder getCode() {
		return new ZStringBuilder(code);
	}

	/**
	 * Returns a compressed copy of the code.
	 * 
	 * @return A compressed copy of the code
	 */
	public ZStringBuilder toCompressedCode() {
		ZStringEncoder copy = new ZStringEncoder(code);
		copy.compress();
		return copy;
	}
	
	/**
	 * Sets the code based on a compressed version of the actual code.
	 * 
	 * @param code A compressed version of the actual code.
	 */
	public void fromCompressedCode(ZStringBuilder code) {
		ZStringEncoder decomp = new ZStringEncoder(code);
		decomp.decompress();
		setCode(decomp);
	}
	
	/**
	 * Returns the length of the actual code string.
	 * 
	 * @return The length of the actual code string
	 */
	public int length() {
		return code.length();
	}

	/**
	 * Replaces the code with a new code of the specified length.
	 * 
	 * The number of properties that a genetic code can represent is (length / 3)
	 * 
	 * @param length The length of the new code
	 */
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

	/**
	 * Mutates the code using the specified rate (0.0 - 1.0)
	 * 
	 * @param rate The mutation rate
	 */
	public void mutate(float rate) {
		mutate((int)(rate * (float)code.length()));
	}

	/**
	 * Mutates a specific number of random genes.
	 * 
	 * @param num The total number of genes to mutate.
	 */
	public void mutate(int num) {
		if (code!=null) {
			if (num>=(code.length() / 2)) {
				generate(code.length());
			} else if (num>0 && code.length()>0) {
				int div = code.length() / num;
				for (int n = 0; n < num; n++) {
					int min = n * div;
					int max = (n + 1) * div;
					if (max>code.length() - 1) {
						max = code.length() - 1;
					}
					int i = ZRandomize.getRandomInt(min,max);
					String o = code.substring(i,i+1).toString();
					String r = o;
					while (r.equals(o)) {
						r = "" + ZRandomize.getRandomInt(0,9);
					}
					code.replace(i,i+1,r);
				}
			}
			refreshFactors();
		}
	}
	
	/**
	 * Returns the number of unique properties the current code can represent.
	 * 
	 * @return The number of unique properties the current code can represent
	 */
	public int size() {
		int r = 0;
		if (code!=null) {
			r = code.length() / 3;
		}
		return r;
	}

	/**
	 * Returns a specific property value.
	 * 
	 * @param index The index of the property value (0 - size())
	 * @return A specific property value
	 */
	public float get(int index) {
		float r = -1;
		if (index>-1 && factors.size()>index) {
			r = factors.get(index);
		}
		return r;
	}
	
	/**
	 * Returns a specific property value as an integer scaled to a certain value.
	 * 
	 * @param index The index of the property value (0 - size())
	 * @param scale The scale to use (1 - Integer.MAX)
	 * @return A specific property value
	 */
	public int getInteger(int index, int scale) {
		return Math.round(get(index) * (float)scale);
	}

	/**
	 * Returns a specific property value as an integer scaled to a certain value between two values (inclusive).
	 * 
	 * @param index The index of the property value (0 - size())
	 * @param min The minimum value
	 * @param max The maximum value
	 * @return A specific property value
	 */
	public int getIntegerMinMax(int index, int min, int max) {
		return min + getInteger(index,max - min);
	}
	
	/**
	 * Returns a specific property value as a boolean (value less than 0.5F = true) .
	 * 
	 * @param index The index of the property value (0 - size())
	 * @return A specific property value
	 */
	public boolean getBoolean(int index) {
		return get(index) < 0.5F;
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
				p = index + Integer.parseInt(code.substring(p,end).toString());
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
