package nl.zeesoft.zdk.test.impl;

import java.util.Calendar;
import java.util.TimeZone;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.CombinedEncoder;
import nl.zeesoft.zdk.htm.enc.DateTimeEncoder;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestDateTimeEncoder extends TestObject {
	public TestDateTimeEncoder(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDateTimeEncoder(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *DateTimeEncoder* to convert a range of dates/times into combined periodic sparse distributed representations.");
		System.out.println("The *DateTimeEncoder* is merely an example implementation of a *CombinedEncoder* used to test this library.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the encoder");
		System.out.println("DateTimeEncoder enc = new DateTimeEncoder();");
		System.out.println("// Obtain the SDR for a certain value");
		System.out.println("SDR sdr = enc.getSDRForValue(System.currentTimeMillis());");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDateTimeEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(DateTimeEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(CombinedEncoder.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows how the generated SDRs represent several date/times.");
	}
	
	@Override
	protected void test(String[] args) {
		DateTimeEncoder enc = new DateTimeEncoder();
		assertEqual(enc.size(),120,"Encoder size does not match expectation");
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.YEAR,2019);
		cal.set(Calendar.MONTH,0);
		cal.set(Calendar.DATE,1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		
		SDR sdr = null;
		
		System.out.println("Changing months;");
		for (int m = 0; m<=11; m++) {
			sdr = enc.getSDRForValue(cal.getTimeInMillis());
			ZDate date = new ZDate();
			date.setDate(cal.getTime());
			System.out.println("SDR for " + date.getDateTimeString() + "; " + sdr.toBitString());
			cal.set(Calendar.MONTH,cal.get(Calendar.MONTH) + 1);
		}
		assertEqual(sdr.size(),enc.size(),"SDR size does not match expectation");
		assertEqual(sdr.onBits(),enc.bits(),"SDR onBits does not match expectation");
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("120,0,1,2,3,4,5,6,7,92,93,94,95,48,49,50,51,96,97,98,99,100,101,102,103"),"SDR(1) does not match expectation");
		
		System.out.println();
		System.out.println("Changing days of week;");
		for (int d = 0; d<=6; d++) {
			sdr = enc.getSDRForValue(cal.getTimeInMillis());
			ZDate date = new ZDate();
			date.setDate(cal.getTime());
			System.out.println("SDR for " + date.getDateTimeString() + "; " + sdr.toBitString());
			cal.set(Calendar.DATE,cal.get(Calendar.DATE) + 1);
		}
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("120,0,1,2,3,4,5,6,7,48,49,50,51,52,53,54,55,102,103,104,105,106,107,108,109"),"SDR(2) does not match expectation");
		
		System.out.println();
		System.out.println("Changing hours of day;");
		for (int h = 0; h<=23; h++) {
			sdr = enc.getSDRForValue(cal.getTimeInMillis());
			ZDate date = new ZDate();
			date.setDate(cal.getTime());
			System.out.println("SDR for " + date.getDateTimeString() + "; " + sdr.toBitString());
			cal.set(Calendar.HOUR_OF_DAY,cal.get(Calendar.HOUR_OF_DAY) + 1);
		}
		assertEqual(sdr.toStringBuilder(),new ZStringBuilder("120,46,47,0,1,2,3,4,5,49,50,51,52,53,54,55,56,109,110,111,112,113,114,115,116"),"SDR(3) does not match expectation");
	}
}
