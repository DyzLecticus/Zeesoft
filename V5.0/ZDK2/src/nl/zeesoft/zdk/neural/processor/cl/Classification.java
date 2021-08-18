package nl.zeesoft.zdk.neural.processor.cl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.MathUtil;
import nl.zeesoft.zdk.Util;

public class Classification {
	public int						step				= 0;
	public List<ValueLikelyhood>	valueLikelyhoods	= new ArrayList<ValueLikelyhood>();
	public Object					value				= null;
	public ValueLikelyhood			prediction			= null;
	public ValueLikelyhood			averagePrediction	= null;

	public List<ValueLikelyhood> getMostLikelyValues(int top) {
		List<ValueLikelyhood> r = new ArrayList<ValueLikelyhood>();
		for (int i = 0; i < top; i++) {
			if (i<valueLikelyhoods.size()) {
				r.add(valueLikelyhoods.get(i));
			} else {
				break;
			}
		}
		return r;
	}
	
	public void determinePredictedValue() {
		if (valueLikelyhoods.size()==1) {
			prediction = valueLikelyhoods.get(0);
		} else if (valueLikelyhoods.size()>1) {
			if (valueLikelyhoods.get(0).likelyhood > valueLikelyhoods.get(1).likelyhood) {
				prediction = valueLikelyhoods.get(0);
			}
		}
	}
	
	public void determineAveragePredictedValue(int top) {
		List<ValueLikelyhood> list = getMostLikelyValues(top);
		if (list.size()>0) {
			float totalValue = 0F;
			float totalLikelyhood = 0F;
			for (ValueLikelyhood vl: list) {
				totalValue += Util.getFloatValue(vl.value);
				totalLikelyhood += vl.likelyhood;
			}
			averagePrediction = new ValueLikelyhood(totalValue / (float)list.size(), totalLikelyhood);
		}
	}
	
	public float getStandardDeviation() {
		List<Float> values = new ArrayList<Float>();
		for (ValueLikelyhood vl: valueLikelyhoods) {
			values.add(vl.likelyhood);
		}
		return MathUtil.getStandardDeviation(values);
	}
}

