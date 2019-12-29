package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.grid.ZGrid;
import nl.zeesoft.zdk.htm.grid.ZGridColumnEncoder;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderDateTime;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderProperty;
import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderValue;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestZGridColumnEncoders extends TestObject {
	public TestZGridColumnEncoders(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestZGridColumnEncoders(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows the configuration options of several *ZGridColumnEncoder* instances.  ");
		System.out.println("*ZGridColumnEncoder* instances are used to translate *ZGrid* request values into SDRs for further grid processing.  ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create a date/time grid encoder");
		System.out.println("ZGridEncoderDateTime dateTimeEncoder = new ZGridEncoderDateTime();");
		System.out.println("// Transform the encoder to JSON");
		System.out.println("JsFile json = dateTimeEncoder.toJson();");
		System.out.println("// Configure the encoder using JSON");
		System.out.println("dateTimeEncoder.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestZGridColumnEncoders.class));
		System.out.println(" * " + getTester().getLinkForClass(ZGridColumnEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(ZGrid.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println(" * " + getTester().getLinkForClass(ZGridEncoderDateTime.class));
		System.out.println(" * " + getTester().getLinkForClass(ZGridEncoderValue.class));
		System.out.println(" * " + getTester().getLinkForClass(ZGridEncoderProperty.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the JSON representations of several encoders  ");
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
		assertEqual(sdr.length(),512,"SDR (scale: 2) length does not match expectation");
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
		valueEncoder.setType(ZGridEncoderValue.TYPE_SCALED);
		err = valueEncoder.testScalarOverlap();
		assertEqual(err,new ZStringBuilder(),"Error does not match expectation (5)");
		valueEncoder.setType(ZGridEncoderValue.TYPE_DIMENSIONAL);
		err = valueEncoder.testScalarOverlap();
		assertEqual(err,new ZStringBuilder(),"Error does not match expectation (6)");
		
		if (testJsAble(valueEncoder,new ZGridEncoderValue(),"Encoder JSON does not match expectation (2)")) {
			System.out.println();
			System.out.println("Value encoder JSON:");
			System.out.println(valueEncoder.toJson().toStringBuilderReadFormat());
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
