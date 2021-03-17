package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.ScalarSdrEncoder;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.se.ScalarEncoder;

public class TestScalarEncoder {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		ScalarSdrEncoder enc = new ScalarSdrEncoder();
		
		Sdr sdr1 = enc.getEncodedValue(-1);
		Sdr sdr2 = enc.getEncodedValue(0);
		assert sdr1.equals(sdr2);
		assert ScalarSdrEncoder.checkOnBits(sdr1, 32, 10).length()>0;

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
		assert sdr1.toString().equals("256,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99");
		sb = enc.testOnBits();
		assert sb.length() == 0;
		
		enc = new ScalarSdrEncoder();
		enc.encodeLength = 16;
		enc.minValue = -8;
		enc.maxValue = 7;
		enc.onBits = 4;
		enc.periodic = true;
		assert enc.testMinimalOverlap().length()==0;
		
		enc = new ScalarSdrEncoder();
		enc.encodeLength = 16;
		enc.minValue = 8;
		enc.maxValue = 24;
		enc.onBits = 4;
		enc.periodic = true;
		assert enc.testMinimalOverlap().length()==0;
		
		ScalarEncoder se = new ScalarEncoder();
		assert se.getInputNames().size() == 1;
		assert se.getInputNames().get(0).equals("SensorValue");
		assert se.getOutputNames().size() == 1;
		assert se.getOutputNames().get(0).equals("EncodedSensor");
		assert se.toString().length() == 55;
		
		ProcessorIO io = new ProcessorIO();
		se.processIO(io);
		assert io.error.equals("ScalarEncoder requires an input value");
		
		io.error = "";
		io.inputValue = true;
		se.processIO(io);
		assert io.error.equals("ScalarEncoder requires an integer or float value");
		
		io.error = "";
		io.inputValue = 0;
		se.processIO(io);
		assert io.error.length() == 0;
		assert io.outputs.size() == 1;
		assert io.outputs.get(0).toString().equals("256,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15");

		io.outputs.clear();
		io.inputValue = 0F;
		se.processIO(io);
		assert io.error.length() == 0;
		assert io.outputs.size() == 1;
		assert io.outputs.get(0).toString().equals("256,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15");
	}
}
