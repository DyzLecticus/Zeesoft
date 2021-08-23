package nl.zeesoft.zdk.test.neural.processor;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.encoder.datetime.DateTimeSdrEncoder;
import nl.zeesoft.zdk.neural.encoder.datetime.DateTimeSdrEncoderTester;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.de.DateTimeEncoder;
import nl.zeesoft.zdk.str.StrUtil;

public class TestDateTimeEncoder {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		DateTimeSdrEncoder encoder = new DateTimeSdrEncoder();
		DateTimeSdrEncoderTester tester = new DateTimeSdrEncoderTester();
		StringBuilder str = tester.testMinimalOverlap(encoder);
		assert str.length() == 0;
		
		encoder.setOnBitsPerEncoder(4);
		str = tester.testMinimalOverlap(encoder);
		assert str.length() == 135;
		
		encoder.setOnBitsPerEncoder(12);
		encoder.setScale(120, 1);
		str = tester.testMinimalOverlap(encoder);
		assert str.length() == 0;
		
		str = JsonConstructor.fromObject(encoder).toStringBuilderReadFormat();
		
		DateTimeSdrEncoder encoder2 = encoder.copy();
		assert StrUtil.equals(JsonConstructor.fromObject(encoder2).toStringBuilderReadFormat(),str);
		
		encoder2 = (DateTimeSdrEncoder) ObjectConstructor.fromJson(new Json(str));

		assert encoder.getEncodedValue("").onBits.size()==0;
		encoder.setEncode("Pizza",DateTimeSdrEncoder.HOUR);
		assert encoder.getEncodedValue(0L).onBits.size() == encoder.getOnBits();
		assert encoder.getEncodedValue(0L).length == encoder.getEncodeLength();

		DateTimeEncoder enc = new DateTimeEncoder();
		encoder = (DateTimeSdrEncoder) enc.encoder;
		encoder.setOnBitsPerEncoder(12);
		encoder.setScale(120, 1);
		
		ProcessorIO io = new ProcessorIO();
		enc.processIO(io);
		assert io.error.length()>0;
		
		io = new ProcessorIO();
		io.inputValue = "Pizza";
		enc.processIO(io);
		assert io.error.length()>0;
		
		io = new ProcessorIO();
		io.inputValue = 0L;
		enc.processIO(io);
		assert io.error.length()==0;
		
		Sdr sdr = io.outputs.get(0);
		Sdr sdr2 = encoder2.getEncodedValue(0L);
		
		assert sdr2.length == encoder2.getEncodeLength();
		assert sdr2.onBits.size() == encoder2.getOnBits();
		assert sdr2.getOverlap(sdr) == encoder.getOnBits();

		io = new ProcessorIO();
		io.inputValue = new Date();
		enc.processIO(io);
		assert io.error.length()==0;
		sdr = io.outputs.get(0);
		assert sdr.length == encoder.getEncodeLength();
		assert sdr.onBits.size() == encoder.getOnBits();
	}
}
