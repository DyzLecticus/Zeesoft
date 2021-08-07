package nl.zeesoft.zdk.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.MathUtil;

public class TestMathUtil {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		assert new MathUtil() != null;
		
		assert MathUtil.getHypotenuse(3, 4) == 5;
		
		List<Float> values = new ArrayList<Float>();
		List<Float> errors = new ArrayList<Float>();
		assert MathUtil.getStandardDeviation(values) == 0F;
		assert MathUtil.getRootMeanSquaredError(values) == 0F;
		assert MathUtil.getMeanAveragePercentageError(values, errors) == 0F;
		values.add(1F);
		assert MathUtil.getStandardDeviation(values) == 0F;
		assert MathUtil.getRootMeanSquaredError(values) == 1F;
		values.add(1F);
		assert MathUtil.getStandardDeviation(values) == 0F;
		assert MathUtil.getRootMeanSquaredError(values) == 1F;
		values.add(2F);
		assert MathUtil.getStandardDeviation(values) == 0.57735026F;
		assert MathUtil.getRootMeanSquaredError(values) == 1.4142135F;

		values.add(0F);
		errors.add(0.1F);
		errors.add(0.2F);
		errors.add(0.2F);
		errors.add(0.3F);
		errors.add(0.0F);
		assert MathUtil.getMeanAveragePercentageError(values, errors) == 0.1F;
	}
}
