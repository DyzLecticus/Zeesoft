package nl.zeesoft.zdk.test;

import java.util.List;

import nl.zeesoft.zdk.ArrUtil;
import nl.zeesoft.zdk.Logger;

public class TestArrUtil {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		assert new ArrUtil() != null;
		
		assert ArrUtil.isObjectArray(new String[1]) == true;
		assert ArrUtil.isObjectArray(new StringBuilder[1]) == true;
		assert ArrUtil.isObjectArray(new Integer[1]) == true;
		assert ArrUtil.isObjectArray(new Long[1]) == true;
		assert ArrUtil.isObjectArray(new Float[1]) == true;
		assert ArrUtil.isObjectArray(new Double[1]) == true;
		assert ArrUtil.isObjectArray(new Boolean[1]) == true;
		assert ArrUtil.isObjectArray(new Byte[1]) == true;
		assert ArrUtil.isObjectArray(new Short[1]) == true;
		
		String[] stringArray = {"A", "B"};
		assert !ArrUtil.isPrimitiveArray(stringArray);
		assert ArrUtil.isObjectArray(stringArray);
		assert ArrUtil.isOneDimensionalArray(stringArray);
		List<Object> values = ArrUtil.unpackArray(stringArray);
		assert values.size() == 2;
		assert values.get(0).equals("A");
		assert values.get(1).equals("B");
		
		StringBuilder[] sbArray = {new StringBuilder("A"), new StringBuilder("B")};
		assert !ArrUtil.isPrimitiveArray(sbArray);
		assert ArrUtil.isObjectArray(sbArray);
		assert ArrUtil.isOneDimensionalArray(sbArray);
		values = ArrUtil.unpackArray(sbArray);
		assert values.size() == 2;
		assert values.get(0).toString().equals("A");
		assert values.get(1).toString().equals("B");

		int[] intArray = {1, 2};
		assert ArrUtil.isPrimitiveArray(intArray);
		assert !ArrUtil.isObjectArray(intArray);
		assert ArrUtil.isOneDimensionalArray(intArray);
		values = ArrUtil.unpackArray(intArray);
		assert values.size() == 2;
		assert values.get(0).equals(1);
		assert values.get(1).equals(2);
		
		long[] longArray = {1L, 2L};
		assert ArrUtil.isPrimitiveArray(longArray);
		values = ArrUtil.unpackArray(longArray);
		assert values.size() == 2;
		assert values.get(0).equals(1L);
		assert values.get(1).equals(2L);
		
		float[] floatArray = {0.1F, 0.2F};
		assert ArrUtil.isPrimitiveArray(floatArray);
		values = ArrUtil.unpackArray(floatArray);
		assert values.size() == 2;
		assert values.get(0).equals(0.1F);
		assert values.get(1).equals(0.2F);
		
		double[] doubleArray = {0.1D, 0.2D};
		assert ArrUtil.isPrimitiveArray(doubleArray);
		values = ArrUtil.unpackArray(doubleArray);
		assert values.size() == 2;
		assert values.get(0).equals(0.1D);
		assert values.get(1).equals(0.2D);
		
		boolean[] booleanArray = {true, false};
		assert ArrUtil.isPrimitiveArray(booleanArray);
		values = ArrUtil.unpackArray(booleanArray);
		assert values.size() == 2;
		assert values.get(0).equals(true);
		assert values.get(1).equals(false);
		
		byte[] byteArray = {1, 2};
		assert ArrUtil.isPrimitiveArray(byteArray);
		values = ArrUtil.unpackArray(byteArray);
		assert values.size() == 2;
		assert values.get(0).equals(byteArray[0]);
		assert values.get(1).equals(byteArray[1]);
		
		short[] shortArray = {1, 2};
		assert ArrUtil.isPrimitiveArray(shortArray);
		values = ArrUtil.unpackArray(shortArray);
		assert values.size() == 2;
		assert values.get(0).equals(shortArray[0]);
		assert values.get(1).equals(shortArray[1]);
		
		ArrUtil[] array = new ArrUtil[2];
		array[0] = new ArrUtil();
		array[1] = new ArrUtil();
		assert !ArrUtil.isPrimitiveArray(array);
		assert !ArrUtil.isObjectArray(array);
		assert !ArrUtil.isOneDimensionalArray(array);
		values = ArrUtil.unpackArray(array);
		assert values.size() == 2;
		assert values.get(0) instanceof ArrUtil;
		assert values.get(1) instanceof ArrUtil;
	}
}
