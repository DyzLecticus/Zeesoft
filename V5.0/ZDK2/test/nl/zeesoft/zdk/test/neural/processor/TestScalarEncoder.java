package nl.zeesoft.zdk.test.neural.processor;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.encoder.ScalarSdrEncoder;
import nl.zeesoft.zdk.neural.encoder.ScalarSdrEncoderTester;
import nl.zeesoft.zdk.neural.processor.InputOutputConfig;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.se.SeConfig;
import nl.zeesoft.zdk.neural.processor.se.ScalarEncoder;
import nl.zeesoft.zdk.str.StrUtil;

public class TestScalarEncoder {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		ScalarSdrEncoder enc = new ScalarSdrEncoder();
		ScalarSdrEncoderTester tester = new ScalarSdrEncoderTester();
		
		Sdr sdr1 = enc.getEncodedValue(-1);
		Sdr sdr2 = enc.getEncodedValue(0);
		assert sdr1.equals(sdr2);
		assert ScalarSdrEncoderTester.checkOnBits(sdr1, 32, 10).length()>0;

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
		
		StringBuilder sb = tester.testMinimalOverlap(enc);
		assert sb.length() == 0;
		
		sb = tester.testOnBits(enc);
		assert sb.length() == 0;
		
		enc.encodeLength = 100;
		sb = tester.testMinimalOverlap(enc);
		assert sb.toString().equals("Invalid bucket value overlap for value: 1.0, overlap: 16, maximum: 15");
		
		enc.encodeLength = 256;
		enc.onBits = 1;
		sb = tester.testNoOverlap(enc);
		assert sb.length() == 0;
		sb = tester.testMinimalOverlap(enc);
		assert sb.toString().equals("Invalid bucket value overlap for value: 1.0, overlap: 0, minimum: 1");
		
		enc.onBits = 16;
		sb = tester.testOverlap(enc, -1, 15);
		assert sb.length() == 0;
		sb = tester.testOverlap(enc, 0, -1);
		assert sb.length() == 0;
		
		enc.periodic = true;
		enc.maxValue = 300;
		sdr1 = enc.getEncodedValue(400);
		assert sdr1.toString().equals("256,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99");
		sb = tester.testOnBits(enc);
		assert sb.length() == 0;
		
		enc = new ScalarSdrEncoder();
		enc.encodeLength = 16;
		enc.minValue = -8;
		enc.maxValue = 7;
		enc.onBits = 4;
		enc.periodic = true;
		assert tester.testMinimalOverlap(enc).length()==0;
		
		enc = new ScalarSdrEncoder();
		enc.encodeLength = 16;
		enc.minValue = 8;
		enc.maxValue = 24;
		enc.onBits = 4;
		enc.periodic = true;
		assert tester.testMinimalOverlap(enc).length()==0;
		
		ScalarEncoder se = new ScalarEncoder();
		assert se.getInputOutputConfig()!=null;
		
		InputOutputConfig ioConfig = se.encoder.getInputOutputConfig();
		assert ioConfig.inputs.size() == 1;
		assert ioConfig.inputs.get(ScalarEncoder.SENSOR_VALUE_INPUT).name.equals("SensorValue");
		assert ioConfig.outputs.size() == 1;
		assert ioConfig.outputs.get(ScalarEncoder.ENCODED_SENSOR_OUTPUT).name.equals("EncodedSensor");
		assert ioConfig.toString().length() == 41;
		assert se.toString().length() == 55;
		
		ProcessorIO io = new ProcessorIO();
		se.processIO(io);
		assert io.error.equals("ScalarEncoder requires an input value");
		
		io.error = "";
		io.inputValue = true;
		se.processIO(io);
		assert io.error.equals("ScalarEncoder requires an integer or float input value");
		
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
		
		ScalarSdrEncoder copy = enc.copy();
		assert copy.encodeLength == enc.encodeLength;
		assert copy.onBits == enc.onBits;
		assert copy.minValue == enc.minValue;
		assert copy.maxValue == enc.maxValue;
		assert copy.resolution == enc.resolution;
		assert copy.periodic == enc.periodic;
		
		SeConfig scCopy = se.encoder.copy();
		assert scCopy.encodeLength == se.encoder.encodeLength;
		assert scCopy.onBits == se.encoder.onBits;
		assert scCopy.minValue == se.encoder.minValue;
		assert scCopy.maxValue == se.encoder.maxValue;
		assert scCopy.resolution == se.encoder.resolution;
		assert scCopy.periodic == se.encoder.periodic;
		
		Json json = JsonConstructor.fromObjectUseConvertors(se);
		StringBuilder str = json.toStringBuilder();
		Json json2 = new Json(str);
		ScalarEncoder se2 = (ScalarEncoder) ObjectConstructor.fromJson(json2);
		json2 = JsonConstructor.fromObjectUseConvertors(se2);
		assert StrUtil.equals(str, json2.toStringBuilder());
	}
}
