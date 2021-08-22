package nl.zeesoft.zdk.app.neural.handlers.api;

import java.util.Calendar;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.str.StrUtil;

public class RegularDateTimeValuesCsvHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/data/regular.csv";
	
	public RegularDateTimeValuesCsvHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		response.setContentTypeText();
		if (request.method.equals(HttpRequest.GET)) {
			StringBuilder str = new StringBuilder();
			StrUtil.appendLine(str, "DateTime,Value");
			StrUtil.appendLine(str, "datetime,float");
			StrUtil.appendLine(str, "T,");
			StrUtil.appendLine(str, generateData(getCalendar()));
			response.setBody(str);
		}
	}
	
	protected Calendar getCalendar() {
		Calendar r = Calendar.getInstance();
		r.set(Calendar.YEAR, 2017);
		r.set(Calendar.MONTH, 0);
		r.set(Calendar.DAY_OF_MONTH, 1);
		r.set(Calendar.HOUR_OF_DAY, 0);
		r.set(Calendar.MINUTE, 0);
		r.set(Calendar.SECOND, 0);
		r.set(Calendar.MILLISECOND, 0);
		return r;
	}
	
	protected StringBuilder generateData(Calendar cal) {
		StringBuilder r = new StringBuilder();
		for (int w = 0; w < 30; w++) {
			for (int wd = 0; wd < 7; wd++) {
				for (int hr = 0; hr < 24; hr++) {
					cal.add(Calendar.HOUR,1);
					StringBuilder dtv = getDateTime(cal);
					dtv.append(",");
					dtv.append((1.0F * ((hr + 1) * 3)));
					StrUtil.appendLine(r,dtv);
				}
			}
		}
		return r;
	}
	
	protected StringBuilder getDateTime(Calendar cal) {
		StringBuilder r = new StringBuilder("" + (cal.get(Calendar.MONTH) + 1));
		r.append("/");
		r.append(cal.get(Calendar.DAY_OF_MONTH));
		r.append("/");
		r.append(("" + cal.get(Calendar.YEAR)).substring(2));
		r.append(" ");
		r.append(cal.get(Calendar.HOUR_OF_DAY));
		r.append(":");
		r.append(String.format("%02d",cal.get(Calendar.MINUTE)));
		return r;
	}
}
