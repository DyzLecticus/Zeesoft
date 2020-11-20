package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;

public class BasicFeatureArrayEncoder extends BasicScalarEncoder {
	protected BasicFeatureEncoder	featureEncoder1	= new BasicFeatureEncoder();
	protected BasicFeatureEncoder	featureEncoder2	= new BasicFeatureEncoder();
	protected BasicFeatureEncoder	featureEncoder3	= null;
	
	protected List<Str>				combinations	= new ArrayList<Str>();

	public BasicFeatureArrayEncoder() {
		setDerivedArrayValues();
	}
	
	public void setFeatureEncoders(
		BasicFeatureEncoder featureEncoder1,
		BasicFeatureEncoder featureEncoder2
		) {
		setFeatureEncoders(featureEncoder1,featureEncoder2,null);
	}
	
	public void setFeatureEncoders(
		BasicFeatureEncoder featureEncoder1,
		BasicFeatureEncoder featureEncoder2,
		BasicFeatureEncoder featureEncoder3
		) {
		this.featureEncoder1 = featureEncoder1;
		this.featureEncoder2 = featureEncoder2;
		this.featureEncoder3 = featureEncoder3;
		setDerivedArrayValues();
	}
	
	public void setFeatureEncoder3(BasicFeatureEncoder featureEncoder3) {
		setFeatureEncoders(featureEncoder1,featureEncoder2,featureEncoder3);
	}
	
	public BasicFeatureEncoder getFeatureEncoder1() {
		return featureEncoder1;
	}

	public BasicFeatureEncoder getFeatureEncoder2() {
		return featureEncoder2;
	}

	public BasicFeatureEncoder getFeatureEncoder3() {
		return featureEncoder3;
	}

	public void changedFeatureEncoders() {
		setDerivedArrayValues();
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
		int r = 0;
		if (value instanceof int[]) {
			int[] combination = (int[]) value;
			if (combination.length<=3) {
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
			value = getIndexForValue(value);
		}
		return super.getEncodedValue(value);
	}
	
	@Override
	public void setEncodeDimensions(int sizeX, int sizeY) {
		setDerivedArrayValues();
	}
	
	@Override
	public void setOnBits(int onBits) {
		super.setOnBits(onBits);
		setDerivedArrayValues();
	}

	@Override
	public void setMinValue(float minValue) {
		setDerivedArrayValues();
	}

	@Override
	public void setMaxValue(float maxValue) {
		setDerivedArrayValues();
	}

	@Override
	public void setResolution(float resolution) {
		setDerivedArrayValues();
	}

	protected void setDerivedArrayValues() {
		combinations.clear();
		int len = 2;
		if (featureEncoder3!=null) {
			len = 3;
		}
		int[] combination = new int[len];
		for (int i = 0; i < combination.length; i++) {
			combination[i] = 0;
		}
		for (int i1 = 0; i1 < featureEncoder1.getFeatures(); i1++) {
			for (int i2 = 0; i2 < featureEncoder2.getFeatures(); i2++) {
				if (featureEncoder3!=null) {
					for (int i3 = 0; i3 < featureEncoder3.getFeatures(); i3++) {
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
