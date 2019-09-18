package nl.zeesoft.zdk.test.impl.htm;

import java.util.Calendar;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.CombinedEncoder;
import nl.zeesoft.zdk.htm.enc.DateTimeEncoder;
import nl.zeesoft.zdk.htm.enc.DateTimeValueEncoder;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestDateTimeValueEncoder extends TestObject {
	public TestDateTimeValueEncoder(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDateTimeValueEncoder(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *DateTimeValueEncoder* to convert a range of dates/times and values into combined periodic sparse distributed representations.");
		System.out.println("The *DateTimeValueEncoder* is merely an example implementation of a *CombinedEncoder* used to test this library.");
		System.out.println("It uses random distributed scalar encoders to represent the values in order to show how these use state to maintain consistent representations.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the encoder");
		System.out.println("DateTimeValueEncoder enc = new DateTimeEncoder();");
		System.out.println("// Obtain the SDR for a certain value");
		System.out.println("SDR sdr = enc.getSDRForValue(System.currentTimeMillis(),2,6);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDateTimeValueEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(DateTimeValueEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(DateTimeEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(CombinedEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * How the generated SDRs represent several date/time and value combinations.");
		System.out.println(" * The StringBuilder representation of the encoder state.");
	}
	
	@Override
	protected void test(String[] args) {
		DateTimeValueEncoder enc = new DateTimeValueEncoder();
		assertEqual(enc.size(),200,"Encoder size does not match expectation");
		assertEqual(enc.bits(),40,"Encoder bits does not match expectation");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,2019);
		cal.set(Calendar.MONTH,0);
		cal.set(Calendar.DATE,1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		
		SDR sdr = null;
		
		int val1 = 0;
		int val2 = 0;
		for (int i = 0; i<=4; i++) {
			val1 = i;
			val2 = i*2;
			sdr = enc.getSDRForValue(cal.getTimeInMillis(),val1,val2);
			ZDate date = new ZDate();
			date.setDate(cal.getTime());
			System.out.println("SDR for " + date.getDateTimeString() + ", value1: " + val1 + ", value2: " + val2 + "; " + sdr.toBitString());
			if (i<4) {
				cal.set(Calendar.MONTH,cal.get(Calendar.MONTH) + 1);
				cal.set(Calendar.DATE,cal.get(Calendar.DATE) + 1);
				cal.set(Calendar.HOUR_OF_DAY,cal.get(Calendar.HOUR_OF_DAY) + 1);
			}
		}
		
		ZStringBuilder str = enc.toStringBuilder();
		DateTimeValueEncoder encCopy = new DateTimeValueEncoder();
		encCopy.fromStringBuilder(str);
		ZStringBuilder strCopy = encCopy.toStringBuilder();
		if (!assertEqual(strCopy.equals(str),true,"Encoder StringBuilder does not match expectation")) {
			System.err.println(str);
			System.err.println(strCopy);
		} else {
			System.out.println();
			System.out.println("Encoder StringBuilder:");
			System.out.println(str);
			SDR sdrCopy = encCopy.getSDRForValue(cal.getTimeInMillis(),val1,val2);
			if (!assertEqual(sdr.equals(sdrCopy),true,"SDR copy does not match original")) {
				System.err.println(sdr.toBitString());
				System.err.println(sdrCopy.toBitString());
			}
		}
	}
}
