package nl.zeesoft.zdk;

import java.util.List;

public class MathUtil {
	public static float getHypotenuse(float len1, float len2) {
		return (float) Math.sqrt((len1 * len1) + (len2 * len2));
	}
	
	public static float getStandardDeviation(List<Float> values) {
		float r = 0.0F;
		if (values.size()>1) {
			float sum = 0.0F;
			int size = values.size();
			for (Float value: values) {
				sum += value;
			}
			float mean = sum / size;
			float dev = 0.0F;
			for (Float value: values) {
				dev += Math.pow(value - mean, 2);
			}
			r = (float) Math.sqrt(dev/(size - 1));
		}
		return r;
	}
	
	public static float getRootMeanSquaredError(List<Float> errors) {
		float r = 0.0F;
		if (errors.size()>0) {
		    float square = 0;
		    float mean = 0;
			for (Float error: errors) {
				square += Math.pow(error, 2);
			}
		    mean = (square / (float)errors.size());
		    r = (float)Math.sqrt(mean);
		}
		return r;
	}
	
	public static float getMeanAveragePercentageError(List<Float> values, List<Float> errors) {
		float r = 0.0F;
		if (values.size()>0) {
			int i = 0;
			float total = 0.0F; 
			for (Float value: values) {
				if (value!=0.0F) {
					total += (errors.get(i) / value);
				}
				i++;
			}
			r = total / (float) values.size();
		}
		return r;
	}
}
