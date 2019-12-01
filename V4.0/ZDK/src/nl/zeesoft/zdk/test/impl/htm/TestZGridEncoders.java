package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderDateTime;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderPosition;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderProperty;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderValue;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestZGridEncoders extends TestObject {
	public TestZGridEncoders(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestZGridEncoders(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how to use a *Memory* instance to learn temporal sequences of SDRs.  ");
		System.out.println();
		System.out.println("**Please note** that this implementation differs greatly from the Numenta HTM implementation because it does not model dendrites;  ");
		System.out.println("Memory cells are directly connected to each other and dendrite activation is not limited.  ");
		System.out.println("Further more, distal connections do not need to be randomly initialized when the memory is created.  ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the configuration");
		System.out.println("MemoryConfig config = new MemoryConfig(1024);");
		System.out.println("// Create the memory");
		System.out.println("Memory memory = new Memory(config);");
		System.out.println("// Obtain the output SDR for a certain input SDR");
		System.out.println("SDR sdr = memory.getSDRForInput(new SDR(),true);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockRegularSDRMap.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestGrid.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println(" * " + getTester().getLinkForClass(MemoryConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(Memory.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * How memory column bursting is reduced after leaning several sequences  ");
		System.out.println(" * Information about the memory after passing the SDR test set through it  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		ZStringBuilder err = null;
		
		// DateTime encoder
		ZGridEncoderDateTime dateTimeEncoder = new ZGridEncoderDateTime();
		assertEqual(dateTimeEncoder.testScalarOverlap(),new ZStringBuilder(),"Scalar overlap test results do not match expectation for scale 1");
		SDR sdr = dateTimeEncoder.getSDRForDateTime(System.currentTimeMillis());
		assertEqual(sdr.length(),256,"SDR (scale: 1) length does not match expectation");
		err = dateTimeEncoder.testScalarOverlap();
		assertEqual(err,new ZStringBuilder(),"Error does not match expectation (1)");
		
		dateTimeEncoder.setScale(2);
		assertEqual(dateTimeEncoder.testScalarOverlap(),new ZStringBuilder(),"Scalar overlap test results do not match expectation for scale 2");
		sdr = dateTimeEncoder.getSDRForDateTime(System.currentTimeMillis());
		assertEqual(sdr.length(),529,"SDR (scale: 2) length does not match expectation");
		err = dateTimeEncoder.testScalarOverlap();
		assertEqual(err,new ZStringBuilder(),"Error does not match expectation (2)");
		
		dateTimeEncoder.setScale(4);
		assertEqual(dateTimeEncoder.testScalarOverlap(),new ZStringBuilder(),"Scalar overlap test results do not match expectation for scale 4");
		sdr = dateTimeEncoder.getSDRForDateTime(System.currentTimeMillis());
		assertEqual(sdr.length(),1024,"SDR (scale: 4) length does not match expectation");
		err = dateTimeEncoder.testScalarOverlap();
		assertEqual(err,new ZStringBuilder(),"Error does not match expectation (3)");
		
		if (testJsAble(dateTimeEncoder,new ZGridEncoderDateTime(),"Encoder JSON does not match expectation (1)")) {
			System.out.println("Date/time encoder JSON:");
			System.out.println(dateTimeEncoder.toJson().toStringBuilderReadFormat());
		}
		
		// Value encoder
		ZGridEncoderValue valueEncoder = new ZGridEncoderValue();
		err = valueEncoder.testScalarOverlap();
		assertEqual(err,new ZStringBuilder(),"Error does not match expectation (4)");
		
		if (testJsAble(valueEncoder,new ZGridEncoderValue(),"Encoder JSON does not match expectation (2)")) {
			System.out.println();
			System.out.println("Value encoder JSON:");
			System.out.println(valueEncoder.toJson().toStringBuilderReadFormat());
		}
		
		// Position encoder
		ZGridEncoderPosition positionEncoder = new ZGridEncoderPosition();
		err = positionEncoder.testScalarOverlap();
		assertEqual(err,new ZStringBuilder(),"Error does not match expectation (5)");
		
		if (testJsAble(positionEncoder,new ZGridEncoderPosition(),"Encoder JSON does not match expectation (3)")) {
			System.out.println();
			System.out.println("Position encoder JSON:");
			System.out.println(positionEncoder.toJson().toStringBuilderReadFormat());
		}
		
		// Property encoder
		ZGridEncoderProperty propertyEncoder = new ZGridEncoderProperty();
		propertyEncoder.addProperty("Pizza");
		propertyEncoder.addProperty("Coffee");
		propertyEncoder.addProperty("Ice cream");
		propertyEncoder.addProperty("Chicken");
		propertyEncoder.addProperty("Sandwich");
		sdr = propertyEncoder.getSDRForValue(3);
		SDR sdrC = new SDR(256);
		for (int i = 16; i < 24; i++) {
			sdrC.setBit(i,true);
		}
		assertEqual(sdr.toBitString(),sdrC.toBitString(),"SDR does not match expectation (1)");
		
		if (testJsAble(propertyEncoder,new ZGridEncoderProperty(),"Encoder JSON does not match expectation (4)")) {
			System.out.println();
			System.out.println("Property encoder JSON:");
			System.out.println(propertyEncoder.toJson().toStringBuilderReadFormat());
		}
		
		ZStringBuilder str = propertyEncoder.toStringBuilder();
		propertyEncoder = new ZGridEncoderProperty();
		propertyEncoder.fromStringBuilder(str);
		assertEqual(propertyEncoder.getValueForProperty("Ice cream"),3,"Value not match expectation");
		sdr = propertyEncoder.getSDRForProperty("Ice cream");
		assertEqual(sdr.toBitString(),sdrC.toBitString(),"SDR does not match expectation (2)");
	}
}
