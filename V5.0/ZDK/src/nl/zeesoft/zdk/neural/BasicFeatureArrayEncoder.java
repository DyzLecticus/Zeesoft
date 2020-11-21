package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;

public class BasicFeatureArrayEncoder extends AbstractScalarEncoder {
	protected int 			features1 = 3;
	protected int 			features2 = 3;
	protected int 			features3 = 0;
	
	protected List<Str>		combinations	= new ArrayList<Str>();

	public BasicFeatureArrayEncoder() {
		onBits = 2;
		setDerivedValues();
	}

	public int getFeatures1() {
		return features1;
	}

	public void setFeatures1(int features1) {
		if (features1>1) {
			this.features1 = features1;
			setDerivedValues();
		}
	}

	public int getFeatures2() {
		return features2;
	}

	public void setFeatures2(int features2) {
		if (features2>1) {
			this.features2 = features2;
			setDerivedValues();
		}
	}

	public int getFeatures3() {
		return features3;
	}

	public void setFeatures3(int features3) {
		this.features3 = features3;
		setDerivedValues();
	}
	
	public void setFeatures(int ... features) {
		if (features.length>=2) {
			setFeatures1(features[0]);
			setFeatures2(features[1]);
		}
		if (features.length>=3) {
			setFeatures3(features[2]);
		}
	}
	
	public void setOnBits(int onBits) {
		this.onBits = onBits;
		setDerivedValues();
	}
	
	public int[] getValueForIndex(int index) {
		int[] r = null;
		if (index >= 0 && index < combinations.size()) {
			Str combo = combinations.get(index);
			List<Str> vals = combo.split(",");
			r = new int[vals.size()];
			for (int i = 0; i < r.length; i++) {
				r[i] = Integer.parseInt(vals.get(i).toString());
			}
		}
		return r;
	}
	
	public int getIndexForValue(Object value) {
		int r = -1;
		if (value instanceof int[]) {
			int[] combination = (int[]) value;
			if (combination.length>=2 && combination.length<=3) {
				Str combo = getStrForCombination(combination);
				if (combinations.contains(combo)) {
					r = combinations.indexOf(combo);
				}
			}
		}
		return r;
	}
	
	@Override
	public SDR getEncodedValue(Object value) {
		if (value instanceof int[]) {
			int index = getIndexForValue(value);
			if (index>=0) {
				value = index;
			} else {
				value = minValue;
			}
		}
		return super.getEncodedValue(value);
	}

	protected void setDerivedValues() {
		combinations.clear();
		int len = 2;
		if (features3>1) {
			len = 3;
		}
		int[] combination = new int[len];
		for (int i = 0; i < combination.length; i++) {
			combination[i] = 0;
		}
		for (int i1 = 0; i1 < features1; i1++) {
			for (int i2 = 0; i2 < features2; i2++) {
				if (features3>1) {
					for (int i3 = 0; i3 < features3; i3++) {
						combination[0] = i1;
						combination[1] = i2;
						combination[2] = i3;
						combinations.add(getStrForCombination(combination));
					}
				} else {
					combination[0] = i1;
					combination[1] = i2;
					combinations.add(getStrForCombination(combination));
				}
			}
		}
		encodeSizeX = onBits;
		encodeSizeY = combinations.size();
		minValue = 0;
		maxValue = combinations.size() - 1;
		resolution = 1;
	}
	
	protected Str getStrForCombination(int[] combination) {
		Str r = new Str();
		for (int i = 0; i < combination.length; i++) {
			if (r.length()>0) {
				r.sb().append(",");
			}
			r.sb().append(combination[i]);
		}
		return r;
	}
}
