package nl.zeesoft.zdk.neural.encoder.datetime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.json.Finalizable;
import nl.zeesoft.zdk.json.JsonTransient;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.encoder.AbstractEncoder;

public class DateTimeSdrEncoder extends AbstractEncoder implements Finalizable {
	public static String				SEASON					= "Season";
	public static String				DAY_OF_MONTH			= "DayOfMonth";
	public static String				DAY_OF_WEEK				= "DayOfWeek";
	public static String				HOUR					= "Hour";
	public static String				MINUTE					= "Minute";
	public static String				SECOND					= "Second";
	
	private static String[]				types					= {SEASON, DAY_OF_MONTH, DAY_OF_WEEK, HOUR, MINUTE, SECOND};
	
	public SeasonSdrEncoder				seasonEncoder			= new SeasonSdrEncoder();
	public DayOfMonthSdrEncoder			dayOfMonthEncoder		= new DayOfMonthSdrEncoder();
	public DayOfWeekSdrEncoder			dayOfWeekEncoder		= new DayOfWeekSdrEncoder();
	public HourSdrEncoder				hourEncoder				= new HourSdrEncoder();
	public MinuteSdrEncoder				minuteEncoder			= new MinuteSdrEncoder();
	public SecondSdrEncoder				secondEncoder			= new SecondSdrEncoder();
	
	@JsonTransient
	protected SeasonSdrEncoder[]		allEncoders				= {seasonEncoder, dayOfMonthEncoder, dayOfWeekEncoder, hourEncoder, minuteEncoder, secondEncoder};
	
	public String[]						encode					= new String[0];
	
	public DateTimeSdrEncoder() {
		setEncode(SEASON, DAY_OF_MONTH, DAY_OF_WEEK, HOUR, MINUTE, SECOND);
		setScale(62, 2);
	}
	
	@Override
	public void finalizeObject() {
		resetAllEncoders();
	}
	
	public DateTimeSdrEncoder copy() {
		DateTimeSdrEncoder r = new DateTimeSdrEncoder();
		r.copyFrom(this);
		return r;
	}

	public void setEncode(String... encode) {
		this.encode = encode;
	}
	
	public int getEncodeLength() {
		int r = 0;
		for (SeasonSdrEncoder encoder: getEncoders()) {
			r += encoder.encodeLength;
		}
		return r;
	}
	
	public int getOnBits() {
		int r = 0;
		for (SeasonSdrEncoder encoder: getEncoders()) {
			r += encoder.onBits;
		}
		return r;
	}
	
	public void setOnBitsPerEncoder(int onBits) {
		for (int i = 0; i < allEncoders.length; i++) {
			allEncoders[i].setOnBits(onBits);
		}
	}

	public void setScale(int targetLength, int minOverlap) {
		for (int i = 0; i < allEncoders.length; i++) {
			allEncoders[i].setScale(targetLength, minOverlap);
		}
	}
	
	@Override
	public Sdr getEncodedValue(Object value) {
		Sdr r = new Sdr(getEncodeLength());
		if (value instanceof Long) {
			value = new Date((Long)value);
		}
		if (value instanceof Date) {
			r.concat(getSdrs((Date)value));
		}
		return r;
	}

	protected List<Sdr> getSdrs(Date value) {
		List<Sdr> r = new ArrayList<Sdr>();
		for (SeasonSdrEncoder encoder: getEncoders()) {
			r.add(encoder.getEncodedValue(value));
		}
		return r;
	}

	protected List<SeasonSdrEncoder> getEncoders() {
		List<SeasonSdrEncoder> encoders = new ArrayList<SeasonSdrEncoder>();
		encoders.clear();
		for (String type: encode) {
			int index = -1;
			for (int i = 0; i < types.length; i++) {
				if (types[i].equals(type)) {
					index = i;
				}
			}
			if (index>=0) {
				encoders.add(allEncoders[index]);
			}
		}
		return encoders;
	}
	
	protected void copyFrom(DateTimeSdrEncoder other) {
		this.seasonEncoder = other.seasonEncoder.copy();
		this.dayOfMonthEncoder = other.dayOfMonthEncoder.copy();
		this.dayOfWeekEncoder = other.dayOfWeekEncoder.copy();
		this.minuteEncoder = other.minuteEncoder.copy();
		this.hourEncoder = other.hourEncoder.copy();
		this.secondEncoder = other.secondEncoder.copy();
		this.encode = other.encode;
		resetAllEncoders();
	}

	protected void resetAllEncoders() {
		allEncoders[0] = seasonEncoder;
		allEncoders[1] = dayOfMonthEncoder;
		allEncoders[2] = dayOfWeekEncoder;
		allEncoders[3] = hourEncoder;
		allEncoders[4] = minuteEncoder;
		allEncoders[5] = secondEncoder;
	}
}
