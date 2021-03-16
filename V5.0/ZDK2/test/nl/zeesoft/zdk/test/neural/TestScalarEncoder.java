package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.ScalarEncoder;
import nl.zeesoft.zdk.neural.Sdr;

public class TestScalarEncoder {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		ScalarEncoder enc = new ScalarEncoder();
		
		Sdr sdr1 = enc.getEncodedValue(-1);
		Sdr sdr2 = enc.getEncodedValue(0);
		assert sdr1.equals(sdr2);

		sdr1 = enc.getEncodedValue(true);
		assert sdr1.equals(sdr2);

		sdr1 = enc.getEncodedValue(0);
		sdr2 = enc.getEncodedValue(1);
		assert sdr1.toString().equals("256,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15");
		assert sdr2.toString().equals("256,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16");
		
		sdr1 = enc.getEncodedValue(200);
		sdr2 = enc.getEncodedValue(201);
		assert sdr1.equals(sdr2);
		assert sdr1.toString().equals("256,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255");
		
		StringBuilder sb = enc.testMinimalOverlap();
		assert sb.length() == 0;
		
		sb = enc.testOnBits();
		assert sb.length() == 0;
		
		enc.encodeLength = 100;
		sb = enc.testMinimalOverlap();
		assert sb.toString().equals("Invalid bucket value overlap for value: 1.0, overlap: 16, maximum: 15");
		
		enc.encodeLength = 256;
		enc.onBits = 1;
		sb = enc.testNoOverlap();
		assert sb.length() == 0;
		sb = enc.testMinimalOverlap();
		assert sb.toString().equals("Invalid bucket value overlap for value: 1.0, overlap: 0, minimum: 1");
		
		enc.onBits = 16;
		sb = enc.testOverlap(-1,15);
		assert sb.length() == 0;
		sb = enc.testOverlap(0,-1);
		assert sb.length() == 0;
		
		enc.periodic = true;
		enc.maxValue = 300;
		sdr1 = enc.getEncodedValue(400);
		assert sdr1.toString().equals("256,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94");
		sb = enc.testOnBits();
		assert sb.length() == 0;
	}
}
