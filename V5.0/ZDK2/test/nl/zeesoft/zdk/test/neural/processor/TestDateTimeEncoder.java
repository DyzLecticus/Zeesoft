package nl.zeesoft.zdk.test.neural.processor;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zdk.Console;
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
	private static TestDateTimeEncoder	self	= new TestDateTimeEncoder();
	
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

		/*
		assert false;
		
		// Date encoder
		DateSdrEncoder dEnc = new DateSdrEncoder();
		dEnc.setOnBitsPerEncoder(4);
		DateSdrEncoder dEnc2 = dEnc.copy();
		assert dEnc2.getEncodeLength() == dEnc.getEncodeLength();
		assert dEnc2.getOnBits() == dEnc.getOnBits();
		assert dEnc2.getEncodedValue(cal.getTime()).equals(dEnc.getEncodedValue(cal.getTime()));
		
		// Date & time encoder
		DateTimeSdrEncoder enc = new DateTimeSdrEncoder();
		enc.setOnBitsPerEncoder(4);
		
		Logger.debug(self, "Month cycle");
		for (int i = 0; i < 12; i++) {
			cal.set(Calendar.MONTH, i);
			Sdr sdr = enc.getEncodedValue(cal.getTime());
			logSdr(sdr);
			assert sdr.length == enc.getEncodeLength();
			assert sdr.onBits.size() == enc.getOnBits();
			assert (float)sdr.onBits.size() / (float)sdr.length == 0.06185567F;
		}
		cal.set(Calendar.MONTH, 0);

		Logger.debug(self, "Date cycle");
		for (int i = 1; i < 32; i++) {
			cal.set(Calendar.DATE, i);
			Sdr sdr = enc.getEncodedValue(cal.getTime().getTime());
			logSdr(sdr);
			assert sdr.length == enc.getEncodeLength();
			assert sdr.onBits.size() == enc.getOnBits();
			assert (float)sdr.onBits.size() / (float)sdr.length == 0.06185567F;
		}
		cal.set(Calendar.DATE, 1);

		Logger.debug(self, "Hour cycle");
		for (int i = 0; i < 24; i++) {
			cal.set(Calendar.HOUR_OF_DAY, i);
			Sdr sdr = enc.getEncodedValue(cal.getTime().getTime());
			logSdr(sdr);
			assert sdr.length == enc.getEncodeLength();
			assert sdr.onBits.size() == enc.getOnBits();
			assert (float)sdr.onBits.size() / (float)sdr.length == 0.06185567F;
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);

		enc.setOnBitsPerEncoder(2);
		Sdr sdr = enc.getEncodedValue(cal.getTime());
		assert sdr.length == enc.getEncodeLength();
		assert sdr.onBits.size() == enc.getOnBits();
		assert (float)sdr.onBits.size() / (float)sdr.length == 0.06185567F;

		enc.includeSecond = false;
		sdr = enc.getEncodedValue(cal.getTime());
		assert sdr.length == enc.getEncodeLength();
		assert sdr.onBits.size() == enc.getOnBits();
		assert (float)sdr.onBits.size() / (float)sdr.length == 0.07462686F;
		
		DateTimeSdrEncoder copy = enc.copy();
		assert copy.getEncodeLength() == enc.getEncodeLength(); 
		assert copy.getOnBits() == enc.getOnBits(); 
		enc.setOnBitsPerEncoder(4);
		Sdr sdr2 = copy.getEncodedValue(cal.getTime());
		assert sdr2.equals(sdr);
		
		assert enc.getEncodedValue("").onBits.size() == 0;
		enc.includeMonth = false;
		enc.includeDate = false;
		enc.includeWeekday = false;
		enc.includeHour = false;
		enc.includeMinute = false;
		assert enc.getEncodedValue(cal.getTime()).length == 1;
		assert enc.getEncodedValue(cal.getTime()).onBits.size() == 0;
		
		DateTimeEncoder de = new DateTimeEncoder();
		((DeConfig)de.encoder).setEncode(DateTimeSdrEncoder2.SEASON, DateTimeSdrEncoder2.DAY_OF_MONTH, DateTimeSdrEncoder2.DAY_OF_WEEK, DateTimeSdrEncoder2.HOUR, DateTimeSdrEncoder2.MINUTE, DateTimeSdrEncoder2.SECOND);
		((DeConfig)de.encoder).setOnBitsPerEncoder(8);
		
		assert de.getInputOutputConfig()!=null;
		
		InputOutputConfig ioConfig = ((DeConfig)de.encoder).getInputOutputConfig();
		assert ioConfig.inputs.size() == 1;
		assert ioConfig.inputs.get(DateTimeEncoder.SENSOR_VALUE_INPUT).name.equals("SensorValue");
		assert ioConfig.outputs.size() == 1;
		assert ioConfig.outputs.get(DateTimeEncoder.ENCODED_SENSOR_OUTPUT).name.equals("EncodedSensor");
		assert ioConfig.toString().length() == 39;
		assert de.toString().length() == 55;
		
		ProcessorIO io = new ProcessorIO();
		de.processIO(io);
		assert io.error.equals("DateTimeEncoder requires an input value");
		
		io.error = "";
		io.inputValue = true;
		de.processIO(io);
		assert io.error.equals("DateTimeEncoder requires a long or date input value");
		
		io.error = "";
		io.inputValue = cal.getTime();
		de.processIO(io);
		assert io.error.length() == 0;
		
		Json json = JsonConstructor.fromObjectUseConvertors(de);
		StringBuilder str = json.toStringBuilder();
		Json json2 = new Json(str);
		DateTimeEncoder de2 = (DateTimeEncoder) ObjectConstructor.fromJson(json2);
		json2 = JsonConstructor.fromObjectUseConvertors(de2);
		assert StrUtil.equals(str, json2.toStringBuilder());
		
		sdr = de.encoder.getEncodedValue(cal.getTime());
		assert sdr.onBits.size() == 40;
		sdr2 = de2.encoder.getEncodedValue(cal.getTime());
		assert sdr2.equals(sdr);
		
		enc = ((DeConfig)de.encoder).copy();
		de.encoder = null;
		assert de.getInputOutputConfig() != null;
		de.encoder = (DeConfig) enc;
		*/
	}
	
	private static void logSdr(Sdr sdr) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < sdr.length; i++) {
			if (sdr.onBits.contains(i)) {
				str.append("1");
			} else {
				str.append("0");
			}
		}
		Logger.debug(self, str);
	}
}
