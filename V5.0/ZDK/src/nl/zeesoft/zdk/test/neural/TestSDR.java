package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.BasicFeatureArrayEncoder;
import nl.zeesoft.zdk.neural.BasicFeatureEncoder;
import nl.zeesoft.zdk.neural.BasicScalarEncoder;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.SDRHistory;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestSDR extends TestObject {
	public TestSDR(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSDR(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and compare sparse distributed representations using *SDR* instances.");
		System.out.println("It also shows how to use a *BasicScalarEncoder* to encode integer (or float) values into *SDR* instances.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the SDR");
		System.out.println("SDR sdrA = new SDR(10, 10);");
		System.out.println("// Turn on the first and last bits");
		System.out.println("sdrA.setBit(0,true);");
		System.out.println("sdrA.setBit(99,true);");
		System.out.println("// Create another SDR");
		System.out.println("SDR sdrB = new SDR(10, 10);");
		System.out.println("// Turn on the first and middle bits");
		System.out.println("sdrB.setBit(0,true);");
		System.out.println("sdrB.setBit(50,true);");
		System.out.println("// Get the number of overlapping bits");
		System.out.println("int overlap = sdrA.getOverlap(sdrB);");
		System.out.println();
		System.out.println("// Create the scalar encoder");
		System.out.println("BasicScalarEncoder encoder = new BasicScalarEncoder();");
		System.out.println("SDR sdr = encoder.getEncodedValue(0)");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSDR.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println(" * " + getTester().getLinkForClass(BasicScalarEncoder.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows an example SDR in its compressed and visualized form.");
	}

	@Override
	protected void test(String[] args) {
		// SDR
		SDR sdr = new SDR(4, 4);
		sdr.setBit(0, true);
		sdr.setBit(7, true);
		sdr.setBit(8, true);
		sdr.setBit(15, true);
		assertEqual(sdr.onBits(), 4, "Number of on bits does not match expectation");
		assertEqual(sdr.getBit(3, 1), true, "Bit state does not match expectation");
		
		Str str = sdr.toStr();
		
		System.out.println("SDR: " + str);
		
		System.out.println();
		System.out.println("Visualized;");
		System.out.println(sdr.toVisualStr());
		
		sdr.flatten();
		assertEqual(sdr.sizeX(), 16, "Flattened sizeX does not match expectation");
		assertEqual(sdr.sizeY(), 1, "Flattened sizeY does not match expectation");
		
		sdr.square();
		assertEqual(sdr.sizeX(), 4, "Squared sizeX does not match expectation");
		assertEqual(sdr.sizeY(), 4, "Squared sizeX does not match expectation");
		
		SDR sdr2 = new SDR();
		sdr2.fromStr(str);
		
		assertEqual(sdr2.toStr(),str,"SDR 2 Str does not match expectation");
		
		// History
		SDRHistory hist = new SDRHistory(4, 4, 4);
		
		hist.addSDR(sdr);
		
		sdr2 = new SDR(4, 4);
		sdr2.setBit(0, true);
		
		hist.addSDR(sdr2);
		
		assertEqual(hist.getAverage(0),1F,"Average for bit 0 does not match expectation");
		assertEqual(hist.getAverage(15),0.5F,"Average for bit 15 does not match expectation");
		assertEqual(hist.getTotalAverage(),0.15625F,"Total average does not match expectation");
		
		Str histStr = hist.toStr();
		
		SDRHistory hist2 = new SDRHistory();
		hist2.fromStr(histStr);
		
		assertEqual(hist2.toStr(),histStr, "History Str does not match expectation");
		
		// Scalar encoder
		BasicScalarEncoder encoder = new BasicScalarEncoder();
		encoder.setEncodeDimensions(4, 4);
		encoder.setOnBits(4);
		encoder.setMaxValue(12);
		
		assertEqual(encoder.getEncodedValue(8).toStr(),new Str("4;4;8,9,10,11"),"Encoded SDR does not match expectation");
		assertEqual(encoder.testMinimalOverlap(),new Str(),"Minimal overlap error does not match expectation");
		assertEqual(encoder.testOnBits(),new Str(),"On bits error does not match expectation");
		
		encoder.setMaxValue(3);
		assertEqual(encoder.testNoOverlap(),new Str(),"No overlap error does not match expectation (1)");
		
		encoder.setMaxValue(4);
		assertEqual(encoder.testNoOverlap(),new Str("Invalid bucket value overlap for value: 1.0, overlap: 1, maximum: 0"),"No overlap error does not match expectation (2)");

		encoder.setMaxValue(12);
		
		BasicScalarEncoder encoder2 = new BasicScalarEncoder();
		encoder2.setEncodeDimensions(4, 4);
		encoder2.setOnBits(4);
		encoder2.setMinValue(1);
		encoder2.setMaxValue(13);
		
		for (int i = 0; i <= encoder.getMaxValue(); i++) {
			SDR sdrA = encoder.getEncodedValue(i);
			SDR sdrB = encoder2.getEncodedValue(i+1);
			if (!assertEqual(sdrB.toStr(),sdrA.toStr(),"Encoded SDR does not match expectation (" + (i+1) + ")")) {
				break;
			}
		}
		
		// Feature encoder
		BasicFeatureEncoder featEnc = new BasicFeatureEncoder();
		featEnc.setFeatures(4);
		SDR featSDR = featEnc.getEncodedValue(3);
		assertEqual(featSDR.toStr(), new Str("2;4;6,7"), "Feature SDR does not match expectation");
		
		// Feature array encoder
		BasicFeatureArrayEncoder featArrayEnc = new BasicFeatureArrayEncoder();
		featArrayEnc.setFeatureEncoder3(featEnc);
		featArrayEnc.setOnBits(1);
		int[] val1 = {0,1,2};
		featSDR = featArrayEnc.getEncodedValue(val1);
		assertEqual(featSDR.toStr(), new Str("1;36;6"), "Feature array SDR does not match expectation");
		int[] val2 = {2,2,3};
		featSDR = featArrayEnc.getEncodedValue(val2);
		assertEqual(featSDR.toStr(), new Str("1;36;35"), "Feature array SDR does not match expectation");
		int[] val3 = featArrayEnc.getValueForIndex(35);
		assertEqual(val3.length,3,"Array length does not match expectation");
		assertEqual(val3[0],2,"Array value 0 does not match expectation");
		assertEqual(val3[1],2,"Array value 1 does not match expectation");
		assertEqual(val3[2],3,"Array value 2 does not match expectation");
		
		KeyValueSDR kvSDR = new KeyValueSDR(sdr);
		kvSDR.put("test1", 2);
		kvSDR.put("test2", 0.4F);
		Str kvStr = kvSDR.toStr();
		KeyValueSDR kvSDR2 = new KeyValueSDR();
		kvSDR2.fromStr(kvStr);
		Str kvStr2 = kvSDR2.toStr();
		assertEqual(kvStr2,kvStr,"Key value Str not match expectation");
		assertEqual(kvSDR2.getValueKeys().size(),2,"Number of value keys not match expectation");
	}
}
